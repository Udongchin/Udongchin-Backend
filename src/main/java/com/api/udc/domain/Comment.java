package com.api.udc.domain;

import com.api.udc.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commenter;
    private String content;

    @ManyToOne
    @JoinColumn(name = "qa_id")
    private QA qa;
}
