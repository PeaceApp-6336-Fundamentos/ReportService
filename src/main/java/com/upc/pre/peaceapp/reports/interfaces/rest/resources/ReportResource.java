package com.upc.pre.peaceapp.reports.interfaces.rest.resources;

import com.upc.pre.peaceapp.reports.domain.model.valueobjects.ReportState;
import com.upc.pre.peaceapp.reports.domain.model.valueobjects.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(description = "Report resource representation")
public record ReportResource(

        @Schema(description = "Report unique identifier", example = "1")
        Long id,

        @Schema(description = "Report title", example = "Robo en la avenida principal")
        String title,

        @Schema(description = "Report description", example = "Se reportó un robo a las 8 p.m. cerca del parque.")
        String description,

        @Schema(description = "Report location name or address", example = "Av. Primavera 123, Lima")
        String location,

        @Schema(description = "Report type", example = "ROBBERY")
        ReportType type,

        @Schema(description = "User ID who created the report", example = "101")
        Long userId,

        @Schema(description = "Image URL associated with the report", example = "https://example.com/images/report1.jpg")
        String imageUrl,

        @Schema(description = "Latitude coordinate of the report", example = "-12.046374")
        String latitude,

        @Schema(description = "Longitude coordinate of the report", example = "-77.042793")
        String longitude,

        @Schema(description = "Current state of the report", example = "APPROVED")
        ReportState state,
        String rejectionReason,
        @Schema(description = "Date when the report was created", example = "2025-10-08T20:15:37.000+00:00")
        Date createdAt,

        @Schema(description = "Date when the report was last updated", example = "2025-10-08T20:15:37.000+00:00")
        Date updatedAt
) {}
