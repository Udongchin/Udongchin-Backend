package com.api.udc.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInReqDto {

    @NotBlank(message = "아이디는 필수값입니다.")
    private String memberId;
    @NotBlank(message = "비밀번호는 필수값입니다.")
    private String password;
    @Setter
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateMember {
        private LocalDateTime updatedAt;
        public UpdateMember(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
