/*package com.upc.pre.peaceapp.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.pre.peaceapp.reports.application.dto.ReportDto;
import com.upc.pre.peaceapp.reports.domain.model.aggregates.Report;
import com.upc.pre.peaceapp.reports.domain.model.commands.CreateReportCommand;
import com.upc.pre.peaceapp.reports.domain.model.commands.DeleteReportByIdCommand;
import com.upc.pre.peaceapp.reports.domain.model.valueobjects.ReportType;
import com.upc.pre.peaceapp.reports.domain.services.ReportCommandService;
import com.upc.pre.peaceapp.reports.domain.services.ReportQueryService;
import com.upc.pre.peaceapp.reports.interfaces.rest.ReportController;
import com.upc.pre.peaceapp.reports.interfaces.rest.transform.CreateReportCommandFromResourceAssembler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(classes = ReportServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReportCommandService reportCommandService;

    @MockBean
    private ReportQueryService reportQueryService;

    @MockBean
    private CreateReportCommandFromResourceAssembler createReportCommandFromResourceAssembler;

    // ---------------------------
    // CREATE REPORT
    // ---------------------------
    @Test
    void givenValidReport_whenCreateReport_thenReturnsCreated() throws Exception {
        // Arrange
        Report mockReport = new Report("Test", "desc", "loc", ReportType.ACCIDENT, 1L, null, "lat", "long");
        Mockito.when(reportCommandService.handle(any(CreateReportCommand.class)))
                .thenReturn(Optional.of(mockReport));

        ReportDto dto = new ReportDto();
        dto.setTitle("Test");
        dto.setDescription("desc");
        dto.setLocation("loc");
        dto.setUserId(1L);
        dto.setLatitude("lat");
        dto.setLongitude("long");

        // Act & Assert
        mockMvc.perform(post("/api/v1/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    // ---------------------------
    // DELETE REPORT
    // ---------------------------
    @Test
    void givenReportId_whenDeleteReport_thenCallsService() throws Exception {
        // Arrange
        Long reportId = 42L;

        // Act
        mockMvc.perform(delete("/api/v1/reports/{id}", reportId))
                .andExpect(status().isOk());

        // Assert
        verify(reportCommandService, times(1))
                .handle(any(DeleteReportByIdCommand.class));
    }
}
*/
