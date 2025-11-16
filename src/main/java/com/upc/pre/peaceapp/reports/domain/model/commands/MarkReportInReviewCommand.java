package com.upc.pre.peaceapp.reports.domain.model.commands;

public record MarkReportInReviewCommand(Long reportId) {
    public MarkReportInReviewCommand {
        if (reportId == null || reportId <= 0)
            throw new IllegalArgumentException("reportId must be greater than 0");
    }
}