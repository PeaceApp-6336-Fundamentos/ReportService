package com.upc.pre.peaceapp.reports.domain.model.aggregates;

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

    // Constructor actualizado para tu CreateReportCommand
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
    }
}
