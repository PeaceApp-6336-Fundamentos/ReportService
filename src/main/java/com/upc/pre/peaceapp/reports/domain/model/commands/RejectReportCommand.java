package com.upc.pre.peaceapp.reports.domain.model.commands;

public record RejectReportCommand(Long reportId, String reason) {
    public RejectReportCommand {
        if (reportId == null || reportId <= 0)
            throw new IllegalArgumentException("reportId must be greater than 0");
        if (reason == null || reason.isBlank())
            throw new IllegalArgumentException("reason cannot be null or empty");
    }
}
