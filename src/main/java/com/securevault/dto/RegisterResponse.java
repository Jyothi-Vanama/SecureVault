package com.securevault.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponse {

    private Long userId;
    private String name;
    private String email;
}