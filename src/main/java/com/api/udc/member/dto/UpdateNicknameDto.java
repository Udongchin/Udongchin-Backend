package com.api.udc.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateNicknameDto {
    @NotNull
    private String nickname;
}
