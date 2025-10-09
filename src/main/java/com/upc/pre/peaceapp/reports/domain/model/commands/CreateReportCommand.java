package com.upc.pre.peaceapp.reports.domain.model.commands;

import com.upc.pre.peaceapp.reports.domain.model.valueobjects.ReportType;

public record CreateReportCommand(
        String title,
        String description,
        String location,
        ReportType type,
        Long userId,
        String imageUrl,
        String latitude,
        String longitude
) {
    public CreateReportCommand {
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("title cannot be null or empty");
        if (location == null || location.isBlank())
            throw new IllegalArgumentException("location cannot be null or empty");
        if (type == null)
            throw new IllegalArgumentException("type cannot be null");
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("userId must be greater than 0");
        if (latitude == null || latitude.isBlank())
            throw new IllegalArgumentException("latitude cannot be null or empty");
        if (longitude == null || longitude.isBlank())
            throw new IllegalArgumentException("longitude cannot be null or empty");
    }
}
