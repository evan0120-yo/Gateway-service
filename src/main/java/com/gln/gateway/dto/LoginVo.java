package com.gln.gateway.dto;

import com.gln.gateway.model.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginVo {
    private Account account;
    private String jwt;
}
