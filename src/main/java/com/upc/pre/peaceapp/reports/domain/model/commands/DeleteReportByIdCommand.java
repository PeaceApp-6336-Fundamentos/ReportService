package com.upc.pre.peaceapp.reports.domain.model.commands;

public record DeleteReportByIdCommand(Long id) {
    public DeleteReportByIdCommand {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("id must be greater than 0");
    }
}
