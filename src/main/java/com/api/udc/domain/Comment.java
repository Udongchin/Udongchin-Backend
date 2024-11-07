package com.api.udc.domain;

import com.api.udc.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String commenter;
    private String content;

    @ManyToOne
    @JoinColumn(name = "qa_id")
    private QA qa;

    @ManyToOne
    @JoinColumn(name = "free_id")
    private Free free;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;



    public Comment(String commenter, String content) {
        this.commenter = commenter;
        this.content = content;

    }
    public void setPost(Post post) {
        this.post = post;
    }
}
