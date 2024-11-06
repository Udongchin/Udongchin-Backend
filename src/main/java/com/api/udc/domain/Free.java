package com.api.udc.domain;

import com.api.udc.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Free extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String title;
    private String content;
    private String imageUrl;
    private String type = "Free";
    private int likes;

    @OneToMany(mappedBy = "free", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Free(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.likes = 0;
    }

    public void update(String title, String content, String imageUrl) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title;
        }
        if (content != null && !content.trim().isEmpty()) {
            this.content = content;
        }
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
    }
}
