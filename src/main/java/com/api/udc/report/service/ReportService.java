package com.api.udc.report.service;

import com.api.udc.domain.Report;
import com.api.udc.report.dto.ReportDto;
import com.api.udc.util.response.CustomApiResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public interface ReportService  {
    ResponseEntity<CustomApiResponse<?>> createReport(ReportDto Dto);
}
