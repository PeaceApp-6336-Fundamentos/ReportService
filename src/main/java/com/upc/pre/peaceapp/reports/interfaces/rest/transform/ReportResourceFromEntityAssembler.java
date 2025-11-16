package com.upc.pre.peaceapp.reports.interfaces.rest.transform;

import com.upc.pre.peaceapp.reports.domain.model.aggregates.Report;
import com.upc.pre.peaceapp.reports.interfaces.rest.resources.ReportResource;
import org.springframework.stereotype.Component;

@Component
public class ReportResourceFromEntityAssembler {

    public static ReportResource toResourceFromEntity(Report report) {
        return new ReportResource(
                report.getId(),
                report.getTitle(),
                report.getDescription(),
                report.getLocation(),
                report.getType(),
                report.getUserId(),
                report.getImageUrl(),
                report.getLatitude(),
                report.getLongitude(),
                report.getState(),
                report.getRejectionReason(),
                report.getCreatedAt(),
                report.getUpdatedAt()
        );
    }
}
