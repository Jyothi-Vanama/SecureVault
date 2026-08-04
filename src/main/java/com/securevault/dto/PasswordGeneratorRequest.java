package com.securevault.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordGeneratorRequest {

    @Min(value = 8, message = "Password length must be at least 8")
    @Max(value = 32, message = "Password length must not exceed 32")
    private int length;

    private boolean uppercase;
    private boolean lowercase;
    private boolean numbers;
    private boolean symbols;
}