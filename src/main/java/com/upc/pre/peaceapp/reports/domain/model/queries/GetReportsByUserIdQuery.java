package com.upc.pre.peaceapp.reports.domain.model.queries;

public record GetReportsByUserIdQuery(Long userId) {
    public GetReportsByUserIdQuery {
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("userId must be greater than 0");
    }
}
