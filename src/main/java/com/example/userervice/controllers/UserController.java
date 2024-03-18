package com.example.userervice.controllers;

import com.example.userervice.dtos.LoginRequestDto;
import com.example.userervice.dtos.LogoutRequestDto;
import com.example.userervice.dtos.SignupRequestDto;
import com.example.userervice.dtos.UserDto;
import com.example.userervice.models.Token;
import com.example.userervice.models.User;
import com.example.userervice.repositories.TokenRepository;
import com.example.userervice.services.UserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

private UserService userService;


    @Autowired
    public UserController(UserService userService){
    this.userService = userService;
    }
    @PostMapping("/signUp")
    public UserDto signUp(@RequestBody SignupRequestDto request){
       String email = request.getEmail();
       String password = request.getPassword();
       String name = request.getName();

        return UserDto.from(userService.signUp(email,password,name));
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto request){

    return userService.logIn(request.getEmail(),request.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(@RequestBody LogoutRequestDto request){

    userService.logout(request.getToken());
    return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable("token") @NonNull String token){
        return userService.validateToken(token);
    }
}
