package com.gln.gateway.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gln.gateway.dto.LoginVo;
import com.gln.gateway.exception.DataNotFoundException;
import com.gln.gateway.model.Account;
import com.gln.gateway.model.Role;
import com.gln.gateway.repository.AccountRepository;
import com.gln.gateway.repository.RoleRepository;

@Service
public class AccountService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private TokenService tokenService;

    public Account save(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER")
            .orElseThrow(() -> new DataNotFoundException("Role not found: USER"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        return accountRepository.save(Account.builder()
            .username(username)
            .password(encodedPassword)
            .roles(roles)
            .build());
    }

    public LoginVo loginAccount(String username, String password) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

            String token = tokenService.generateJwt(auth);
            return LoginVo.builder()
                .account(accountRepository.findByUsername(username).get())
                .jwt(token)
                .build();
        } catch (AuthenticationException e) {
            return LoginVo.builder().build();
        }
    }
}
