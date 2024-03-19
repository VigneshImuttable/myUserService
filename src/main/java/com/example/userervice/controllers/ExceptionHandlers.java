package com.example.userervice.controllers;

import com.example.userervice.Exceptions.PasswordNotMatchingException;
import com.example.userervice.Exceptions.TokenNotExistException;
import com.example.userervice.Exceptions.UserNotExistException;
import com.example.userervice.dtos.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<Void> handleArithmeticException(){
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<ExceptionDto> passwordNotMatchingException(PasswordNotMatchingException exception){
        ExceptionDto dto = new ExceptionDto();
        dto.setMessage(exception.getMessage());
        dto.setDetail("The password is not matching");
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    public ResponseEntity<ExceptionDto> userNotExistException(UserNotExistException exception){
        ExceptionDto dto = new ExceptionDto();
        dto.setMessage(exception.getMessage());
        dto.setDetail("The user does not exist");
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    public ResponseEntity<ExceptionDto> tokenNotExistException(TokenNotExistException exception){
        ExceptionDto dto = new ExceptionDto();
        dto.setMessage(exception.getMessage());
        dto.setDetail("The token does not exist or Token expired");
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
}
