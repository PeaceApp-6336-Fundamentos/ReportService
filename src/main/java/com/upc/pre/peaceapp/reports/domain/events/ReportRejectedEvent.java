package com.upc.pre.peaceapp.reports.domain.events;

import java.io.Serializable;

public class ReportRejectedEvent implements Serializable {

    private Long reportId;
    private Long userId;
    private String reason;
    private String timestamp;

    public ReportRejectedEvent(Long reportId, Long userId, String reason, String timestamp) {
        this.reportId = reportId;
        this.userId = userId;
        this.reason = reason;
        this.timestamp = timestamp;
    }

    public ReportRejectedEvent() {}

    public Long getReportId() { return reportId; }
    public void setReportId(Long reportId) { this.reportId = reportId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
