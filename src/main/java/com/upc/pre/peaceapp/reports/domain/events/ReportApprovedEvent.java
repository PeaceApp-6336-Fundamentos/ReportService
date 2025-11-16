package com.upc.pre.peaceapp.reports.domain.events;

import java.io.Serializable;

public class ReportApprovedEvent implements Serializable {

    private Long reportId;
    private Long userId;
    private String latitude;
    private String longitude;
    private String timestamp;

    public ReportApprovedEvent(Long reportId, Long userId, String latitude, String longitude, String timestamp) {
        this.reportId = reportId;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public ReportApprovedEvent() {}

    public Long getReportId() { return reportId; }
    public void setReportId(Long reportId) { this.reportId = reportId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getLatitude() { return latitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
