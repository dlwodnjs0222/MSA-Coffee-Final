package com.example.gatewayservice.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginRequest {

    @JsonAlias({"userId", "username"})
    private String username;
    private String password;
} 