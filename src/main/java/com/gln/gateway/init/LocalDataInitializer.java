package com.gln.gateway.init;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.gln.gateway.model.Account;
import com.gln.gateway.model.Role;
import com.gln.gateway.repository.AccountRepository;
import com.gln.gateway.repository.RoleRepository;

@Component
public class LocalDataInitializer {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Profile("prod")
    @Bean
    private void initProd() throws Exception {
        saveBasicData();
    }

    @Profile("local")
    @Bean
    private void initLocal() throws Exception {
        saveBasicData();
    }

    private void saveBasicData() {
        saveAdminAccount();
    }

    private void saveAdminAccount() {
        Role adminRole = Role.builder().authority("ADMIN").build();
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        roleRepository.save(adminRole);

        Role userRole = Role.builder().authority("USER").build();
        roles.add(userRole);
        roleRepository.save(userRole);

        Account admin = Account.builder()
                .username("admin")
                .password(passwordEncoder.encode("12345678"))
                .roles(roles)
                .build();
        accountRepository.save(admin);

        Account testUser = Account.builder()
                .username("test")
                .password(passwordEncoder.encode("12345678"))
                .roles(roles)
                .build();
        accountRepository.save(testUser);
    }
}
