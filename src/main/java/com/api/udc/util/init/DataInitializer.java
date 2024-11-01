package com.api.udc.util.init;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(1L, RoleName.ROLE_CLIENT));
            roleRepository.save(new Role(2L, RoleName.ROLE_ADMIN));
        }
    }
}