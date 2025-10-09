package com.upc.pre.peaceapp.reports.domain.services;

import com.upc.pre.peaceapp.reports.domain.model.aggregates.Report;
import com.upc.pre.peaceapp.reports.domain.model.commands.CreateReportCommand;
import com.upc.pre.peaceapp.reports.domain.model.commands.DeleteReportByIdCommand;

import java.util.Optional;

public interface ReportCommandService {
    Optional<Report> handle(CreateReportCommand command);
    void handle(DeleteReportByIdCommand command);
}
