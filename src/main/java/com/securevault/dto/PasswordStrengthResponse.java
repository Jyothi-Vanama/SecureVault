package com.securevault.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordStrengthResponse {

    private int score;
    private String strength;
    private List<String> feedback;

}