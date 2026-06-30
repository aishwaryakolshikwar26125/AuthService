package org.example.userauthservice_may2026.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    private String name;

    private String email;

    private String password;
}
