package com.example.userervice.dtos;

import com.example.userervice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserDto implements Serializable {

    private String name;
    private String email;

    public static UserDto from(User user){
        if(user==null) return null;

        UserDto userDto = new UserDto();

        userDto.email=user.getEmail();
        userDto.name=user.getName();

        return userDto;
    }

}
