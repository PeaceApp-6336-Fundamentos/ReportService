package com.upc.pre.peaceapp.reports.domain.model.queries;

public record GetReportByIdQuery(Long id) {
    public GetReportByIdQuery {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("id must be greater than 0");
    }
}
