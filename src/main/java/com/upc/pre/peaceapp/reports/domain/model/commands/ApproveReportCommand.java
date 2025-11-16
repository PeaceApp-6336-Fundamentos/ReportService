package com.upc.pre.peaceapp.reports.domain.model.commands;

public record ApproveReportCommand(Long reportId) {
    public ApproveReportCommand {
        if (reportId == null || reportId <= 0)
            throw new IllegalArgumentException("reportId must be greater than 0");
    }
}
