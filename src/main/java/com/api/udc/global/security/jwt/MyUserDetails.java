package com.api.udc.global.security.jwt;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyUserDetails implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String Membername) throws UsernameNotFoundException {

        Member member = memberRepository.findByMemberId(Membername).orElseThrow(RuntimeException::new);

        List<GrantedAuthority> authorities = member.getMemberRoles().stream()
                .map(memberRole -> new SimpleGrantedAuthority(memberRole.getRole().getRoleName().name()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                member.getMemberId(), member.getPassword(), authorities
        );

    }


}