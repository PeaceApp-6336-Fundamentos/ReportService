package com.upc.pre.peaceapp.reports.interfaces.rest;

import com.upc.pre.peaceapp.reports.domain.model.aggregates.Report;
import com.upc.pre.peaceapp.reports.domain.model.commands.*;
import com.upc.pre.peaceapp.reports.domain.model.queries.GetPublicReportsQuery;
import com.upc.pre.peaceapp.reports.domain.model.queries.GetReportByIdQuery;
import com.upc.pre.peaceapp.reports.domain.model.queries.GetReportsByUserIdQuery;
import com.upc.pre.peaceapp.reports.domain.model.queries.GetAllReportsQuery;
import com.upc.pre.peaceapp.reports.domain.services.ReportCommandService;
import com.upc.pre.peaceapp.reports.domain.services.ReportQueryService;
import com.upc.pre.peaceapp.reports.interfaces.rest.resources.RejectReportResource;
import com.upc.pre.peaceapp.reports.interfaces.rest.resources.ReportResource;
import com.upc.pre.peaceapp.reports.interfaces.rest.resources.CreateReportResource;
import com.upc.pre.peaceapp.reports.interfaces.rest.transform.ReportResourceFromEntityAssembler;
import com.upc.pre.peaceapp.reports.interfaces.rest.transform.CreateReportCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/reports", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Reports", description = "Operations related to Report Management")
@Slf4j
public class ReportController {

    private final ReportCommandService reportCommandService;
    private final ReportQueryService reportQueryService;
    private final CreateReportCommandFromResourceAssembler createReportCommandFromResourceAssembler;

    public ReportController(ReportCommandService reportCommandService,
                            ReportQueryService reportQueryService,
                            CreateReportCommandFromResourceAssembler createReportCommandFromResourceAssembler) {
        this.reportCommandService = reportCommandService;
        this.reportQueryService = reportQueryService;
        this.createReportCommandFromResourceAssembler = createReportCommandFromResourceAssembler;
    }
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> reportExists(@PathVariable Long id) {
        boolean exists = reportQueryService.handle(new GetReportByIdQuery(id)).isPresent();
        return ResponseEntity.ok(exists);
    }
    // ----------------------------------------------------------------------
// GET PUBLIC REPORTS (STATE = APPROVED)
// ----------------------------------------------------------------------
    @Operation(summary = "Get all approved public reports",
            description = "Returns all reports that are marked as APPROVED.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Approved reports retrieved successfully",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ReportResource.class))),
            @ApiResponse(responseCode = "204", description = "No approved reports found")
    })
    @GetMapping("/public")
    public ResponseEntity<List<ReportResource>> getPublicReports() {

        log.info("Fetching all APPROVED public reports");

        var reports = reportQueryService.handle(new GetPublicReportsQuery());

        if (reports.isEmpty()) return ResponseEntity.noContent().build();

        var reportResources = reports.stream()
                .map(ReportResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(reportResources);
    }

    // ----------------------------------------------------------------------
    // CREATE REPORT
    // ----------------------------------------------------------------------
    @Operation(summary = "Create a new report",
            description = "Registers a new report for a specific user. The user must exist before creating the report.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Report created successfully",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ReportResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or user not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createReport(@RequestBody CreateReportResource resource) {
        log.info("Creating new report for user ID: {}", resource.userId());

        try {
            CreateReportCommand command = createReportCommandFromResourceAssembler.toCommand(resource);
            Optional<Report> reportOptional = reportCommandService.handle(command);

            return reportOptional
                    .map(report -> {
                        ReportResource reportResource = ReportResourceFromEntityAssembler.toResourceFromEntity(report);
                        URI location = URI.create("/api/v1/reports/" + report.getId());
                        return ResponseEntity.created(location).body(reportResource);
                    })
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException e) {
            log.error("Error creating report: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error creating report: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // ----------------------------------------------------------------------
    // GET REPORT BY ID
    // ----------------------------------------------------------------------
    @Operation(summary = "Get report by ID", description = "Retrieve a specific report by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report found",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ReportResource.class))),
            @ApiResponse(responseCode = "404", description = "Report not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReportResource> getReportById(@PathVariable Long id) {
        var report = reportQueryService.handle(new GetReportByIdQuery(id));
        return report
                .map(r -> ResponseEntity.ok(ReportResourceFromEntityAssembler.toResourceFromEntity(r)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    // ----------------------------------------------------------------------
// CHANGE REPORT STATE: IN REVIEW
// ----------------------------------------------------------------------
    @Operation(summary = "Mark report as In Review")
    @PutMapping("/{id}/review")
    public ResponseEntity<?> markReportInReview(@PathVariable Long id) {

        try {
            reportCommandService.handle(new MarkReportInReviewCommand(id));

            var updated = reportQueryService.handle(new GetReportByIdQuery(id))
                    .orElseThrow();

            return ResponseEntity.ok(
                    ReportResourceFromEntityAssembler.toResourceFromEntity(updated)
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // ----------------------------------------------------------------------
// CHANGE REPORT STATE: APPROVED
// ----------------------------------------------------------------------
    @Operation(summary = "Approve a report")
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveReport(@PathVariable Long id) {

        try {
            reportCommandService.handle(new ApproveReportCommand(id));

            var updated = reportQueryService.handle(new GetReportByIdQuery(id))
                    .orElseThrow();

            return ResponseEntity.ok(
                    ReportResourceFromEntityAssembler.toResourceFromEntity(updated)
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // ----------------------------------------------------------------------
// CHANGE REPORT STATE: REJECTED (with reason)
// ----------------------------------------------------------------------

    @Operation(summary = "Reject a report", description = "Rejects the report with an explanation for the user.")
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectReport(@PathVariable Long id,
                                          @RequestBody RejectReportResource resource) {

        try {
            reportCommandService.handle(new RejectReportCommand(id, resource.reason()));

            var updated = reportQueryService.handle(new GetReportByIdQuery(id))
                    .orElseThrow();

            return ResponseEntity.ok(
                    ReportResourceFromEntityAssembler.toResourceFromEntity(updated)
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // ----------------------------------------------------------------------
    // GET REPORTS BY USER ID
    // ----------------------------------------------------------------------
    @Operation(summary = "Get reports by user ID",
            description = "Retrieve all reports created by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports found",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ReportResource.class))),
            @ApiResponse(responseCode = "404", description = "No reports found for the specified user")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportResource>> getReportsByUserId(
            @Parameter(description = "User ID to search reports for", required = true)
            @PathVariable Long userId) {

        log.info("Fetching reports for user ID: {}", userId);

        var query = new GetReportsByUserIdQuery(userId);
        var reports = reportQueryService.handle(query);

        if (reports.isEmpty()) return ResponseEntity.notFound().build();

        var reportResources = reports.stream()
                .map(ReportResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(reportResources);
    }

    // ----------------------------------------------------------------------
    // GET ALL REPORTS
    // ----------------------------------------------------------------------
    @Operation(summary = "Get all reports", description = "Retrieve all reports registered in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports retrieved successfully",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ReportResource.class))),
            @ApiResponse(responseCode = "204", description = "No reports found")
    })
    @GetMapping
    public ResponseEntity<List<ReportResource>> getAllReports() {
        log.info("Fetching all reports");

        var reports = reportQueryService.handle(new GetAllReportsQuery());
        if (reports.isEmpty()) return ResponseEntity.noContent().build();

        var reportResources = reports.stream()
                .map(ReportResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(reportResources);
    }

    // ----------------------------------------------------------------------
    // DELETE REPORT BY ID
    // ----------------------------------------------------------------------
    @Operation(summary = "Delete report by ID",
            description = "Deletes a specific report by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Report not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReportById(@PathVariable Long id) {
        log.warn("Deleting report with ID: {}", id);

        try {
            reportCommandService.handle(new DeleteReportByIdCommand(id));
            return ResponseEntity.ok("Report with ID " + id + " deleted successfully");
        } catch (IllegalArgumentException e) {
            log.error("Invalid report ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error deleting report with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
