package com.api.udc.report.service;

import com.api.udc.domain.Report;
import com.api.udc.report.dto.ReportDto;
import com.api.udc.report.repository.ReportRepository;
import com.api.udc.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportServiceImp implements ReportService {
    private final ReportRepository reportRepository;
    private final String uploadDir = "";

    public ResponseEntity<CustomApiResponse<?>> createReport(ReportDto dto) {
        try {
            List<String> imageUrls = new ArrayList<>();
            boolean noAnimal =false;
            // 이미지가 있는 경우에만 파일을 저장하고 이미지 URL 리스트 생성
            if (dto.getImage() != null && !dto.getImage().isEmpty()) {
                imageUrls = saveImages(dto.getImage());
            }
            if(dto.getAnimal().isEmpty()) {
                noAnimal = dto.getAnimal().isEmpty();
            }
            boolean noImage = imageUrls.isEmpty();

            // Report 객체 생성
            Report report = new Report(
                    dto.getAnimal(),
                    dto.getAnimal_description(),
                    imageUrls,
                    dto.getLocation(),
                    dto.getLocation_description(),
                    noImage,
                    noAnimal

            );

            reportRepository.save(report);

            // 성공 응답 반환
            return ResponseEntity.ok(CustomApiResponse.createSuccess(200, report, "신고가 정상적으로 접수되었습니다"));
        } catch (Exception e) {
            // 오류 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomApiResponse.createFailWithoutData(500, "신고 생성 중 오류가 발생했습니다."));
        }
    }

    public List<String> saveImages(List<MultipartFile> images) {
        List<String> savedFilePaths = new ArrayList<>();

        for (MultipartFile image : images) {
            if (!image.isEmpty()) {  // 이미지 파일이 비어있지 않은 경우에만 저장
                String originalFileName = StringUtils.cleanPath(image.getOriginalFilename());
                String fileName = UUID.randomUUID() + "_" + originalFileName;

                try {
                    Path uploadPath = Paths.get(uploadDir);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    Files.copy(image.getInputStream(), uploadPath.resolve(fileName));
                    savedFilePaths.add(uploadDir + fileName);
                } catch (IOException e) {
                    throw new RuntimeException("이미지 저장 실패: " + originalFileName, e);
                }
            }
        }

        return savedFilePaths;
    }
}