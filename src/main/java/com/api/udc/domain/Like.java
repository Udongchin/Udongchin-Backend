package com.api.udc.like.domain;

import com.api.udc.domain.Member;
import com.api.udc.domain.Post;
import com.api.udc.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Like(Post post, Member member) {
        this.post = post;
        this.member = member;
    }
}
