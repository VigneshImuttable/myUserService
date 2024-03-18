package com.example.userervice.dtos;

import com.example.userervice.models.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    private String email;
    private String password;
}
