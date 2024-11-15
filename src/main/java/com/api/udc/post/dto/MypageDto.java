package com.api.udc.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MypageDto {
    private String member_id;
    private String nickname;
}
