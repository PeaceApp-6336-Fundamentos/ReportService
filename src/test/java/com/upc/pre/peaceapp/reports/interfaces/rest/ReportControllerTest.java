/*
package com.upc.pre.peaceapp.reports.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.pre.peaceapp.reports.domain.model.aggregates.Report;
import com.upc.pre.peaceapp.reports.domain.model.commands.CreateReportCommand;
import com.upc.pre.peaceapp.reports.domain.model.commands.DeleteReportByIdCommand;
import com.upc.pre.peaceapp.reports.domain.model.queries.GetReportByIdQuery;
import com.upc.pre.peaceapp.reports.domain.model.queries.GetReportsByUserIdQuery;
import com.upc.pre.peaceapp.reports.domain.model.queries.GetAllReportsQuery;
import com.upc.pre.peaceapp.reports.domain.model.valueobjects.ReportType;
import com.upc.pre.peaceapp.reports.domain.services.ReportCommandService;
import com.upc.pre.peaceapp.reports.domain.services.ReportQueryService;
import com.upc.pre.peaceapp.reports.interfaces.rest.resources.CreateReportResource;
import com.upc.pre.peaceapp.reports.interfaces.rest.resources.ReportResource;
import com.upc.pre.peaceapp.reports.interfaces.rest.transform.CreateReportCommandFromResourceAssembler;
import com.upc.pre.peaceapp.reports.interfaces.rest.transform.ReportResourceFromEntityAssembler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
// @AutoConfigureMockMvc(addFilters = false) // descomenta si la seguridad bloquea las pruebas
class ReportControllerTest {

    private static final String BASE = "/api/v1/reports";

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private ReportCommandService reportCommandService;
    @MockBean private ReportQueryService reportQueryService;
    @MockBean private CreateReportCommandFromResourceAssembler createAssembler;

    // --------------------- POST /reports ---------------------
    @Test
    @DisplayName("POST /reports -> 201 Created + Location + ReportResource")
    void createReport_created() throws Exception {
        var req = new CreateReportResource(
                "Robo", "Se reportó un robo", "Av. Primavera 123",
                ReportType.ROBBERY, 101L, "https://img",
                "-12.046374", "-77.042793"
        );

        // Command real (puede ser mock, pero así queda más explícito)
        var cmd = new CreateReportCommand(
                req.title(), req.description(), req.location(), req.type(),
                req.userId(), req.imageUrl(), req.latitude(), req.longitude()
        );

        // Report "entity" mockeada que devuelve un id para el header Location
        Report reportEntity = Mockito.mock(Report.class);
        when(reportEntity.getId()).thenReturn(10L);

        // Resource de salida
        var res = ackage com.upc.pre.peaceapp.messagebroker.application.listeners;

import com.upc.pre.peaceapp.messagebroker.events.ReportDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Listens for ReportDeletedEvent messages and forwards them
 * to the relevant exchanges so other bounded contexts (like
 * Location and Alert) can handle related data deletion.
        @Component
        public class ReportDeletedListener {

            private static final Logger log = LoggerFactory.getLogger(ReportDeletedListener.class);

            private final RabbitTemplate rabbitTemplate;

            @Value("${app.broker.exchange.location}")
            private String locationExchange;

            @Value("${app.broker.routing-key.location.deleted}")
            private String locationRoutingKey;

            @Value("${app.broker.exchange.alert}")
            private String alertExchange;

            @Value("${app.broker.routing-key.alert.deleted}")
            private String alertRoutingKey;

            public ReportDeletedListener(RabbitTemplate rabbitTemplate) {
                this.rabbitTemplate = rabbitTemplate;
            }

            /**
             * Handles incoming ReportDeletedEvent messages.
             * This listener is triggered whenever a report is deleted.
             * It republishes the event to Location and Alert services.

            @RabbitListener(queues = "${app.broker.queue.report.deleted}")
            public void handleReportDeleted(ReportDeletedEvent event) {
                log.info("📩 ReportDeletedEvent received: {}", event.getReportId());

                // Enviar a LocationService
                try {
                    rabbitTemplate.convertAndSend(locationExchange, locationRoutingKey, event);
                    log.info("📨 Event forwarded to LocationService ✅");
                } catch (Exception e) {
                    log.error("⚠️ Failed to forward event to LocationService", e);
                }

                // Enviar a AlertService (puede no existir)
                try {
                    rabbitTemplate.convertAndSend(alertExchange, alertRoutingKey, event);
                    log.info("📨 Event forwarded to AlertService ✅");
                } catch (Exception e) {
                    log.warn("⚠️ AlertService not available, skipping event for report {}", event.getReportId());
                }
            }

        }
        new ReportResource(
                10L, "Robo", "Se reportó un robo", "Av. Primavera 123",
                ReportType.ROBBERY, 101L, "https://img",
                "-12.046374", "-77.042793",
                new Date(), new Date()
        );

        when(createAssembler.toCommand(any(CreateReportResource.class))).thenReturn(cmd);
        when(reportCommandService.handle(any(CreateReportCommand.class)))
                .thenReturn(Optional.of(reportEntity));

        try (MockedStatic<ReportResourceFromEntityAssembler> m =
                     Mockito.mockStatic(ReportResourceFromEntityAssembler.class)) {
            m.when(() -> ReportResourceFromEntityAssembler.toResourceFromEntity(any(Report.class)))
                    .thenReturn(res);

            mockMvc.perform(post(BASE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", BASE + "/10"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(10)))
                    .andExpect(jsonPath("$.title", is("Robo")))
                    .andExpect(jsonPath("$.userId", is(101)));
        }
    }

    @Test
    @DisplayName("POST /reports -> 400 Bad Request cuando service no crea")
    void createReport_badRequest() throws Exception {
        var req = new CreateReportResource(
                "X", "desc", "loc", ReportType.ROBBERY, 1L, null, "1", "1"
        );

        when(createAssembler.toCommand(any(CreateReportResource.class)))
                .thenReturn(Mockito.mock(CreateReportCommand.class));
        when(reportCommandService.handle(any(CreateReportCommand.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // --------------------- GET /reports/{id} ---------------------
    @Test
    @DisplayName("GET /reports/{id} -> 200 OK con ReportResource")
    void getReportById_ok() throws Exception {
        Report entity = Mockito.mock(Report.class);
        when(reportQueryService.handle(any(GetReportByIdQuery.class)))
                .thenReturn(Optional.of(entity));

        var res = new ReportResource(
                5L, "Robo", "desc", "loc",
                ReportType.ROBBERY, 101L, "https://img",
                "-12", "-77", new Date(), new Date()
        );

        try (MockedStatic<ReportResourceFromEntityAssembler> m =
                     Mockito.mockStatic(ReportResourceFromEntityAssembler.class)) {
            m.when(() -> ReportResourceFromEntityAssembler.toResourceFromEntity(any(Report.class)))
                    .thenReturn(res);

            mockMvc.perform(get(BASE + "/{id}", 5))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(5)))
                    .andExpect(jsonPath("$.type", is("ROBBERY")));
        }
    }

    @Test
    @DisplayName("GET /reports/{id} -> 404 Not Found cuando no existe")
    void getReportById_notFound() throws Exception {
        when(reportQueryService.handle(any(GetReportByIdQuery.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(get(BASE + "/{id}", 999))
                .andExpect(status().isNotFound());
    }

    // --------------------- GET /reports/user/{userId} ---------------------
    @Test
    @DisplayName("GET /reports/user/{userId} -> 200 OK con lista")
    void getReportsByUserId_ok() throws Exception {
        Report e1 = Mockito.mock(Report.class);
        Report e2 = Mockito.mock(Report.class);

        when(reportQueryService.handle(any(GetReportsByUserIdQuery.class)))
                .thenReturn(List.of(e1, e2));

        var r1 = new ReportResource(1L, "R1", "d1", "l1", ReportType.ROBBERY, 101L, null, "-12", "-77", new Date(), new Date());
        var r2 = new ReportResource(2L, "R2", "d2", "l2", ReportType.ROBBERY, 101L, null, "-12", "-77", new Date(), new Date());

        try (MockedStatic<ReportResourceFromEntityAssembler> m =
                     Mockito.mockStatic(ReportResourceFromEntityAssembler.class)) {
            m.when(() -> ReportResourceFromEntityAssembler.toResourceFromEntity(any(Report.class)))
                    .thenReturn(r1, r2); // devuelve r1 para el 1er item y r2 para el 2do

            mockMvc.perform(get(BASE + "/user/{userId}", 101))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[1].id", is(2)));
        }
    }

    @Test
    @DisplayName("GET /reports/user/{userId} -> 404 Not Found si lista vacía")
    void getReportsByUserId_notFound() throws Exception {
        when(reportQueryService.handle(any(GetReportsByUserIdQuery.class)))
                .thenReturn(List.of());

        mockMvc.perform(get(BASE + "/user/{userId}", 101))
                .andExpect(status().isNotFound());
    }

    // --------------------- GET /reports ---------------------
    @Test
    @DisplayName("GET /reports -> 200 OK con lista")
    void getAllReports_ok() throws Exception {
        Report e1 = Mockito.mock(Report.class);
        when(reportQueryService.handle(any(GetAllReportsQuery.class)))
                .thenReturn(List.of(e1));

        var r1 = new ReportResource(3L, "R3", "d3", "l3", ReportType.ROBBERY, 102L, null, "-12", "-77", new Date(), new Date());

        try (MockedStatic<ReportResourceFromEntityAssembler> m =
                     Mockito.mockStatic(ReportResourceFromEntityAssembler.class)) {
            m.when(() -> ReportResourceFromEntityAssembler.toResourceFromEntity(any(Report.class)))
                    .thenReturn(r1);

            mockMvc.perform(get(BASE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(3)));
        }
    }

    @Test
    @DisplayName("GET /reports -> 204 No Content si no hay registros")
    void getAllReports_noContent() throws Exception {
        when(reportQueryService.handle(any(GetAllReportsQuery.class)))
                .thenReturn(List.of());

        mockMvc.perform(get(BASE))
                .andExpect(status().isNoContent());
    }

    // --------------------- GET /reports/{id}/exists ---------------------
    @Test
    @DisplayName("GET /reports/{id}/exists -> 200 OK con true")
    void reportExists_true() throws Exception {
        when(reportQueryService.handle(any(GetReportByIdQuery.class)))
                .thenReturn(Optional.of(Mockito.mock(Report.class)));

        mockMvc.perform(get(BASE + "/{id}/exists", 7))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    // --------------------- DELETE /reports/{id} ---------------------
    @Test
    @DisplayName("DELETE /reports/{id} -> 200 OK con mensaje")
    void deleteReport_ok() throws Exception {
        Mockito.doNothing()
                .when(reportCommandService).handle(any(DeleteReportByIdCommand.class));

        mockMvc.perform(delete(BASE + "/{id}", 7))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("deleted successfully")));
    }

    @Test
    @DisplayName("DELETE /reports/{id} -> 400 Bad Request cuando service lanza IllegalArgumentException")
    void deleteReport_badRequest() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("invalid id"))
                .when(reportCommandService).handle(any(DeleteReportByIdCommand.class));

        mockMvc.perform(delete(BASE + "/{id}", 7))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("invalid id")));
    }

    @Test
    @DisplayName("DELETE /reports/{id} -> 500 Internal Server Error para error inesperado")
    void deleteReport_internalError() throws Exception {
        Mockito.doThrow(new RuntimeException("boom"))
                .when(reportCommandService).handle(any(DeleteReportByIdCommand.class));

        mockMvc.perform(delete(BASE + "/{id}", 7))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("boom")));
    }
}
*/