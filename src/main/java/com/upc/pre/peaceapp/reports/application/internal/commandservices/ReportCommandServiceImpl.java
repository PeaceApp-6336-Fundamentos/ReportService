package com.upc.pre.peaceapp.reports.application.internal.commandservices;

import com.upc.pre.peaceapp.reports.domain.events.ReportDeletedEvent;
import com.upc.pre.peaceapp.reports.domain.model.aggregates.Report;
import com.upc.pre.peaceapp.reports.domain.model.commands.CreateReportCommand;
import com.upc.pre.peaceapp.reports.domain.model.commands.DeleteReportByIdCommand;
import com.upc.pre.peaceapp.reports.domain.services.ReportCommandService;
import com.upc.pre.peaceapp.reports.infrastructure.external.messaging.ReportEventPublisher;
import com.upc.pre.peaceapp.reports.infrastructure.persistence.jpa.ReportRepository;
import com.upc.pre.peaceapp.reports.application.internal.outboundservices.ExternalUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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

        var savedReport = reportRepository.save(report);
        log.info("Report created successfully with ID: {}", savedReport.getId());
        return Optional.of(savedReport);
    }

    @Transactional
    @Override
    public void handle(DeleteReportByIdCommand command) {
        log.info("Deleting report with ID: {}", command.id());

        var report = reportRepository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        reportRepository.deleteById(report.getId());
        log.info("Report deleted successfully with ID: {}", report.getId());

        // Publicar evento para que location-service borre las locaciones
        reportEventPublisher.publishReportDeleted(new ReportDeletedEvent(
                report.getId(),
                report.getUserId(),
                "Report deleted successfully",
                LocalDateTime.now().toString()
        ));
    }
}
