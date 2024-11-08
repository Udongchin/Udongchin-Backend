package com.api.udc.report.repository;

import com.api.udc.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportRepository extends JpaRepository<Report, Long> {
}
