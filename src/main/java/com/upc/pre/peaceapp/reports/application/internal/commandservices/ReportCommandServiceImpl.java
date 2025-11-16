package com.upc.pre.peaceapp.reports.application.internal.commandservices;

import com.upc.pre.peaceapp.reports.domain.events.ReportApprovedEvent;
import com.upc.pre.peaceapp.reports.domain.events.ReportCreatedEvent;
import com.upc.pre.peaceapp.reports.domain.events.ReportDeletedEvent;
import com.upc.pre.peaceapp.reports.domain.events.ReportRejectedEvent;
import com.upc.pre.peaceapp.reports.domain.model.aggregates.Report;
import com.upc.pre.peaceapp.reports.domain.model.commands.*;
import com.upc.pre.peaceapp.reports.domain.model.valueobjects.ReportState;
import com.upc.pre.peaceapp.reports.domain.services.ReportCommandService;
import com.upc.pre.peaceapp.reports.infrastructure.external.messaging.ReportEventPublisher;
import com.upc.pre.peaceapp.reports.infrastructure.persistence.jpa.ReportRepository;
import com.upc.pre.peaceapp.reports.application.internal.outboundservices.ExternalUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class ReportCommandServiceImpl implements ReportCommandService {

    private final ReportRepository reportRepository;
    private final ExternalUserService userService;
    private final ReportEventPublisher reportEventPublisher;

    public ReportCommandServiceImpl(ReportRepository reportRepository,
                                    ExternalUserService userService,
                                    ReportEventPublisher reportEventPublisher) {
        this.reportRepository = reportRepository;
        this.userService = userService;
        this.reportEventPublisher = reportEventPublisher;
    }

    // ----------------------------------------------------
    // CREATE
    // ----------------------------------------------------
    @Override
    public Optional<Report> handle(CreateReportCommand command) {
        log.info("Creating report for user ID: {}", command.userId());

        if (!userService.existsById(command.userId())) {
            throw new IllegalArgumentException("User not found");
        }

        var report = new Report(
                command.title(),
                command.description(),
                command.location(),
                command.type(),
                command.userId(),
                command.imageUrl(),
                command.latitude(),
                command.longitude()
        );

        report.setState(ReportState.PENDING);

        var savedReport = reportRepository.save(report);
        log.info("Report created with ID: {}", savedReport.getId());

        reportEventPublisher.publishReportCreated(
                new ReportCreatedEvent(
                        savedReport.getId(),
                        savedReport.getUserId(),
                        savedReport.getTitle(),
                        LocalDateTime.now().toString()
                )
        );

        return Optional.of(savedReport);
    }

    // ----------------------------------------------------
    // DELETE
    // ----------------------------------------------------
    @Transactional
    @Override
    public void handle(DeleteReportByIdCommand command) {
        log.info("Deleting report with ID: {}", command.id());

        var report = reportRepository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        reportRepository.deleteById(report.getId());
        log.info("Report deleted successfully: {}", report.getId());

        reportEventPublisher.publishReportDeleted(
                new ReportDeletedEvent(
                        report.getId(),
                        report.getUserId(),
                        "Report deleted successfully",
                        LocalDateTime.now().toString()
                )
        );
    }

    // ----------------------------------------------------
    // MARK IN REVIEW
    // ----------------------------------------------------
    @Transactional
    @Override
    public void handle(MarkReportInReviewCommand command) {
        log.info("Marking report ID {} as IN_REVIEW", command.reportId());

        var report = reportRepository.findById(command.reportId())
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        if (report.getState() != ReportState.PENDING) {
            throw new IllegalStateException("Report must be PENDING to enter review");
        }

        report.setState(ReportState.IN_REVIEW);
        reportRepository.save(report);

        log.info("Report {} marked as IN_REVIEW", report.getId());
    }
    @Transactional
    @Override
    public void handle(ApproveReportCommand command) {
        log.info("Approving report ID {}", command.reportId());

        var report = reportRepository.findById(command.reportId())
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        report.setState(ReportState.APPROVED);
        reportRepository.save(report);

        log.info("Report {} approved", report.getId());

        reportEventPublisher.publishReportApproved(
                new ReportApprovedEvent(
                        report.getId(),
                        report.getUserId(),
                        report.getLatitude(),
                        report.getLongitude(),
                        LocalDateTime.now().toString()
                )
        );
    }
    @Transactional
    @Override
    public void handle(RejectReportCommand command) {
        log.info("Rejecting report ID {}", command.reportId());

        var report = reportRepository.findById(command.reportId())
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        // ⭐ AQUÍ se actualiza bien el modelo
        report.reject(command.reason());

        var saved = reportRepository.save(report);

        log.info("Report {} rejected with reason: {}", saved.getId(), saved.getRejectionReason());

        reportEventPublisher.publishReportRejected(
                new ReportRejectedEvent(
                        saved.getId(),
                        saved.getUserId(),
                        saved.getRejectionReason(),   // ⭐ consistente con lo guardado
                        LocalDateTime.now().toString()
                )
        );
    }

}
