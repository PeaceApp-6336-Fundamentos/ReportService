package com.upc.pre.peaceapp.reports.domain.services;

import com.upc.pre.peaceapp.reports.domain.model.aggregates.Report;
import com.upc.pre.peaceapp.reports.domain.model.queries.GetReportByIdQuery;
import com.upc.pre.peaceapp.reports.domain.model.queries.GetReportsByUserIdQuery;
import com.upc.pre.peaceapp.reports.domain.model.queries.GetAllReportsQuery;
import com.upc.pre.peaceapp.reports.domain.model.queries.GetPublicReportsQuery;

import java.util.List;
import java.util.Optional;

public interface ReportQueryService {

    Optional<Report> handle(GetReportByIdQuery query);

    List<Report> handle(GetReportsByUserIdQuery query);

    List<Report> handle(GetAllReportsQuery query);

    List<Report> handle(GetPublicReportsQuery query);
}
