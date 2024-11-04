package com.api.udc.domain;

import com.api.udc.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QA extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String title;
    private String content;
    private String imageUrl;
    private String mode;
    private String type = "Q&A";
    private boolean urgent;
    private int likes;
    private String nickname;

    @OneToMany(mappedBy = "qa", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public QA(String title, String content, String mode, String imageUrl) {
        this.title = title;
        this.content = content;
        this.mode = mode;
        this.imageUrl = imageUrl;
        this.urgent = false;
        this.likes = 0;
    }
}
