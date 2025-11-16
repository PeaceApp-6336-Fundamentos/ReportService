package com.upc.pre.peaceapp.reports.domain.model.aggregates;

import com.upc.pre.peaceapp.reports.domain.model.valueobjects.ReportState;
import com.upc.pre.peaceapp.reports.domain.model.valueobjects.ReportType;
import com.upc.pre.peaceapp.shared.documentation.models.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reports")
public class Report extends AuditableAbstractAggregateRoot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "location", nullable = false, length = 100)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private ReportType type;

    @Column(name = "id_user", nullable = false)
    private Long userId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "latitude", nullable = false, length = 30)
    private String latitude;

    @Column(name = "longitude", nullable = false, length = 30)
    private String longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 20)
    private ReportState state;

    // ⭐ NUEVO — Motivo del rechazo (nullable)
    @Column(name = "rejection_reason", length = 255)
    private String rejectionReason;


    // Constructor
    public Report(String title,
                  String description,
                  String location,
                  ReportType type,
                  Long userId,
                  String imageUrl,
                  String latitude,
                  String longitude) {

        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = ReportState.PENDING;
        this.rejectionReason = null;
    }


    // ---------------------------
    // DOMAIN BEHAVIOR (DDD)
    // ---------------------------

    public void markInReview() {
        if (state != ReportState.PENDING)
            throw new IllegalStateException("Only pending reports can move to in_review.");
        this.state = ReportState.IN_REVIEW;
    }

    public void approve() {
        if (state != ReportState.IN_REVIEW)
            throw new IllegalStateException("Only reports in review can be approved.");
        this.state = ReportState.APPROVED;
        this.rejectionReason = null; // por si acaso
    }

    public void reject(String reason) {
        if (state != ReportState.IN_REVIEW)
            throw new IllegalStateException("Only reports in review can be rejected.");

        this.state = ReportState.REJECTED;
        this.rejectionReason = reason;
    }
}
