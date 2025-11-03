package com.upc.pre.peaceapp.reports.domain.events;
import java.io.Serializable;
import java.time.LocalDateTime;
public class ReportDeletedEvent implements Serializable {
    private Long reportId;
    private Long userId;
    private String message;
    private String timestamp;

    public ReportDeletedEvent(Long reportId, Long userId, String message, String timestamp) {
        this.reportId = reportId;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ReportDeletedEvent() {}

    // Getters y Setters
    public Long getReportId() { return reportId; }
    public void setReportId(Long reportId) { this.reportId = reportId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
