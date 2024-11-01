package com.api.udc.member.setvice;

import com.api.udc.domain.Member;
import com.api.udc.domain.MemberRole;
import com.api.udc.domain.Role;
import com.api.udc.domain.RoleName;
import com.api.udc.global.security.jwt.JwtTokenProvider;
import com.api.udc.member.dto.SignInReqDto;
import com.api.udc.member.dto.SignUpDto;
import com.api.udc.member.repository.MemberRepository;
import com.api.udc.member.repository.RoleRepository;
import com.api.udc.util.response.CustomApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ResponseEntity<CustomApiResponse<?>> signUp(SignUpDto dto) {

        Optional<Member> optionalUser = memberRepository.findByMemberId(dto.getMemberId());
        if (optionalUser.isPresent()) {
            CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "중복되는 아이디가 존재합니다");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Role adminRole = roleRepository.findByRoleName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new IllegalArgumentException("ROLE_ADMIN은 존재하지 않는 역할입니다."));

        // memberRoles가 null일 경우 기본 역할로 ROLE_ADMIN 설정
        List<Role> roles = (dto.getMemberRoles() == null || dto.getMemberRoles().isEmpty())
                ? Collections.singletonList(adminRole)
                : dto.getMemberRoles().stream()
                .map(roleName -> roleRepository.findByRoleName(RoleName.valueOf(roleName))
                        .orElseThrow(() -> new IllegalArgumentException(roleName + "은 존재하지 않는 역할입니다.")))
                .collect(Collectors.toList());


        /*List<Role> roles = dto.getMemberRoles().stream()
                .map(roleName -> roleRepository.findByRoleName(RoleName.valueOf(roleName))
                        .orElseThrow(() -> new IllegalArgumentException(roleName + "은 존재하지 않는 역할입니다.")))
                .collect(Collectors.toList());*/

        Member member = Member.toEntity(dto,roles);
        memberRepository.save(member);

        // 디버깅용 코드
        member.getMemberRoles().forEach(memberRole -> System.out.println("Saved role for ,member: " + memberRole.getRole().getRoleName()));
        CustomApiResponse<?> response=CustomApiResponse.createSuccess(HttpStatus.OK.value(), null,"회원가입에 성공하였습니다");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<CustomApiResponse<?>> signIn(SignInReqDto dto) {
        Member member = memberRepository.findByMemberId(dto.getMemberId()).orElseThrow(RuntimeException::new);
        List<Role> roles = member.getMemberRoles().stream()
                .map(MemberRole::getRole)
                .collect(Collectors.toList());


        Optional<Member> findMember = memberRepository.findByMemberId(dto.getMemberId());
        if (findMember.isPresent()) {
            Member member1 = findMember.get();
            // 비밀번호 일치 여부 확인

            if (member1.getPassword().equals(dto.getPassword())) {
                // 비밀번호가 일치하면 로그인 성공 처리
                String login_token = jwtTokenProvider.createToken(dto.getMemberId(),roles);
                SignInReqDto.UpdateMember updateMemberResponse = new SignInReqDto.UpdateMember(member1.getUpdatedAt());
                CustomApiResponse<SignInReqDto.UpdateMember> login = CustomApiResponse.createSuccessLogin(HttpStatus.OK.value(), null, "로그인에 성공하였습니다.",login_token);

                System.out.println("login_token: " + login_token);
                return ResponseEntity.ok(login);

            } else {
                // 비밀번호가 일치하지 않으면 에러 메시지 반환
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "비밀번호 또는 아이디가 잘못되었습니다."));
            }

        } else {
            // 사용자 아이디가 존재하지 않으면 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "아이디가 잘못되었습니다."));
        }
    }
}
