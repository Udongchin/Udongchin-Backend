package com.api.udc.post.dto;

import lombok.Data;

@Data
public class WarnDto {
    private Long id;
    private String reason;
    private String customReason;
    public WarnDto(Long id,String reason, String message) {
        this.id = id;
        this.reason = reason;
        this.customReason = customReason;
    }
}
