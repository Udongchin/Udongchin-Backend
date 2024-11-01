package com.api.udc.member.controller;

import com.api.udc.member.dto.SignInReqDto;
import com.api.udc.member.dto.SignUpDto;
import com.api.udc.member.dto.UpdateNicknameDto;
import com.api.udc.member.setvice.MemberService;
import com.api.udc.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/signUp")
    public ResponseEntity<CustomApiResponse<?>> signUp(@RequestBody @Valid SignUpDto dto) {
        ResponseEntity<CustomApiResponse<?>> result = memberService.signUp(dto);
        return result;
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInReqDto dto) {
        ResponseEntity<CustomApiResponse<?>> result = memberService.signIn(dto);
        return result;
    }
    @PutMapping("/nickname")
    public ResponseEntity<CustomApiResponse<?>> updateNicknmae(@RequestBody UpdateNicknameDto dto){
        ResponseEntity<CustomApiResponse<?>> result =memberService.updateNickname(dto);
        return result;
    }
}
