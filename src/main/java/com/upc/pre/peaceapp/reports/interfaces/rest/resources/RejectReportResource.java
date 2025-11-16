package com.upc.pre.peaceapp.reports.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request payload for rejecting a report")
public record RejectReportResource(

        @NotBlank
        @Schema(description = "Reason why the report is being rejected", example = "Insufficient evidence")
        String reason
) {}
