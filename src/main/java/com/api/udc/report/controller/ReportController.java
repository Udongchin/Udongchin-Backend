package com.api.udc.report.controller;

import com.api.udc.domain.Report;
import com.api.udc.report.dto.ReportDto;
import com.api.udc.report.service.ReportService;
import com.api.udc.util.response.CustomApiResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/post")
public class ReportController {
    private final ReportService reportService;

    @PostMapping(value = "/report", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomApiResponse<?>> report(
            @RequestPart(required = false) List<MultipartFile> image,
            @RequestParam List<String> location,
            @RequestParam String location_description,
            @RequestParam String animal,
            @RequestParam String animal_description
    ) {
        // ReportDto 객체 생성
        ReportDto dto = ReportDto.builder()
                .image(image)
                .location(location)
                .location_description(location_description)
                .animal(animal)
                .animal_description(animal_description)
                .build();

        // 서비스로 Dto 전송
        ResponseEntity<CustomApiResponse<?>> response = reportService.createReport(dto);
        return response;
    }
}