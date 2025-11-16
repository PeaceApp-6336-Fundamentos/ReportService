package com.upc.pre.peaceapp.reports.application.internal.queryservices;

import com.upc.pre.peaceapp.reports.domain.model.aggregates.Report;
import com.upc.pre.peaceapp.reports.domain.model.queries.GetReportByIdQuery;
import com.upc.pre.peaceapp.reports.domain.model.queries.GetReportsByUserIdQuery;
import com.upc.pre.peaceapp.reports.domain.model.queries.GetAllReportsQuery;
import com.upc.pre.peaceapp.reports.domain.model.queries.GetPublicReportsQuery;
import com.upc.pre.peaceapp.reports.domain.model.valueobjects.ReportState;
import com.upc.pre.peaceapp.reports.domain.services.ReportQueryService;
import com.upc.pre.peaceapp.reports.infrastructure.persistence.jpa.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReportQueryServiceImpl implements ReportQueryService {

    private final ReportRepository reportRepository;

    public ReportQueryServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Optional<Report> handle(GetReportByIdQuery query) {
        log.info("Fetching report by ID: {}", query.id());
        return reportRepository.findById(query.id());
    }

    @Override
    public List<Report> handle(GetReportsByUserIdQuery query) {
        log.info("Fetching reports for user ID: {}", query.userId());
        return reportRepository.findAllByUserId(query.userId());
    }

    @Override
    public List<Report> handle(GetAllReportsQuery query) {
        log.info("Fetching all reports");
        return reportRepository.findAll();
    }

    @Override
    public List<Report> handle(GetPublicReportsQuery query) {
        log.info("Fetching public (APPROVED) reports");
        return reportRepository.findAllByState(ReportState.APPROVED);
    }
}
