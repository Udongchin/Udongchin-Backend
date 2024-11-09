package com.api.udc.domain;

import com.api.udc.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Table(name="POST")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String title;
    private String content;
    private String imageUrl;
    private String mode;
    private String type;
    private boolean urgent;
    private int likes;
    private String nickname;
    private int commentCount=0;
    @ElementCollection
    private List<String> location;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public Post(String title, String content, String mode, String imageUrl, String type, String nickname) {
        this.title = title;
        this.content = content;
        this.mode = mode;
        this.imageUrl = imageUrl;
        this.urgent = false;
        this.nickname = nickname;
        this.type=type;
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

    public void incrementLikes() {
        this.likes += 1;
    }

    public void decrementLikes() {
        if (this.likes > 0) {
            this.likes -= 1;
        }
    }
    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);// 양방향 연관 관계 설정
        this.commentCount++;
    }
    public void setLocation(List<String> location) {
        this.location = location;
    }
}
