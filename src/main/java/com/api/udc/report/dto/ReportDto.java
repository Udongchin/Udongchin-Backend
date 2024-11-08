package com.api.udc.report.dto;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDto {
    @ElementCollection
    private List<MultipartFile> image;
    @ElementCollection
    private List<String> location;
    private String location_description;
    private String animal;
    private String animal_description;
}
