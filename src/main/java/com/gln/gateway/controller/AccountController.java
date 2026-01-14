package com.gln.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gln.gateway.dto.LoginDto;
import com.gln.gateway.model.Account;
import com.gln.gateway.service.AccountService;

@CrossOrigin(value = "*")
@RestController
@RequestMapping(value = "/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(accountService.loginAccount(loginDto.getUsername(), loginDto.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        accountService.save(account.getUsername(), account.getPassword());
        return ResponseEntity.ok().build();
    }
}
