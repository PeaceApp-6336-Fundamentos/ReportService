package com.upc.pre.peaceapp.reports.domain.events;

import java.io.Serializable;

public class ReportCreatedEvent implements Serializable {

    private Long reportId;
    private Long userId;
    private String title;
    private String timestamp;

    public ReportCreatedEvent(Long reportId, Long userId, String title, String timestamp) {
        this.reportId = reportId;
        this.userId = userId;
        this.title = title;
        this.timestamp = timestamp;
    }

    public ReportCreatedEvent() {}

    public Long getReportId() { return reportId; }
    public void setReportId(Long reportId) { this.reportId = reportId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
