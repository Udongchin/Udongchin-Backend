package com.api.udc.domain;

import com.api.udc.member.dto.SignUpDto;
import com.api.udc.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="MEMBERS")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="nickname", nullable=false, unique=true)
    private String nickname;

    @Column(name="memberId",nullable=false, unique=true)
    private String memberId;

    @Column(name="password", nullable=false, unique=true)
    private String password;

    @OneToMany(mappedBy = "member",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<MemberRole> memberRoles;

    public static Member toEntity(SignUpDto dto, List<Role> roles) {
        Member member = Member.builder()
                .memberId(dto.getMemberId())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .build();

        List<MemberRole> memberRoles = dto.getMemberRoles().stream()
                .map(roleName -> roles.stream()
                        .filter(role -> role.getRoleName().name().equals(roleName))
                        .findFirst()
                        .map(role -> new MemberRole(null, member, role))
                        .orElseThrow(() -> new IllegalArgumentException(roleName + "는 존재하지 않는 역할입니다.")))
                .collect(Collectors.toList());

        member.memberRoles = memberRoles;

        return member;
    }
    // 닉네임 업데이트 메서드
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
