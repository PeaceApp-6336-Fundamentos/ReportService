package com.upc.pre.peaceapp.reports.infrastructure.persistence.jpa;

import com.upc.pre.peaceapp.reports.domain.model.aggregates.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByUserId(Long userId);
    Report findById(long reportId);
}
