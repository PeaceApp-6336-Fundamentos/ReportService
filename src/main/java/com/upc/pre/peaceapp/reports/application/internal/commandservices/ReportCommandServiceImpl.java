package com.upc.pre.peaceapp.reports.application.internal.commandservices;

import com.upc.pre.peaceapp.reports.domain.model.aggregates.Report;
import com.upc.pre.peaceapp.reports.domain.model.commands.CreateReportCommand;
import com.upc.pre.peaceapp.reports.domain.model.commands.DeleteReportByIdCommand;
import com.upc.pre.peaceapp.reports.domain.services.ReportCommandService;
import com.upc.pre.peaceapp.reports.infrastructure.persistence.jpa.ReportRepository;
import com.upc.pre.peaceapp.reports.application.internal.outboundservices.ExternalUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ReportCommandServiceImpl implements ReportCommandService {

    private final ReportRepository reportRepository;
    private final ExternalUserService userService;

    public ReportCommandServiceImpl(ReportRepository reportRepository,
                                    ExternalUserService userService) {
        this.reportRepository = reportRepository;
        this.userService = userService;
    }

    @Override
    public Optional<Report> handle(CreateReportCommand command) {
        log.info("Creating report for user ID: {}", command.userId());

        // Verifica que el usuario exista antes de crear el reporte
        if (!userService.existsById(command.userId())) {
            log.error("User with ID {} does not exist", command.userId());
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

    @Override
    public void handle(DeleteReportByIdCommand command) {
        log.info("Deleting report with ID: {}", command.id());

        if (!reportRepository.existsById(command.id())) {
            log.error("Report with ID {} does not exist", command.id());
            throw new IllegalArgumentException("Report not found");
        }

        reportRepository.deleteById(command.id());
        log.info("Report deleted successfully with ID: {}", command.id());
    }
}
