package com.api.udc.domain;

import com.api.udc.util.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QA extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String imageUrl;

    private String mode;

    private String type = "Q&A";


    public QA(String title, String content, String mode, String imageUrl) {
        this.title = title;
        this.content = content;
        this.mode = mode;
        this.imageUrl = imageUrl;
    }
}
