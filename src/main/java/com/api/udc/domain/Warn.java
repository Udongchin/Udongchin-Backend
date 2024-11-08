package com.api.udc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="WARN")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long post_id;
    private String reason;
    private String customReason;
    public Warn(Long post_id,String reason, String customReason) {
        this.reason = reason;
        this.customReason = customReason;
        this.post_id = post_id;
    }

}
