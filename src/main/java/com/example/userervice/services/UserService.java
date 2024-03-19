package com.example.userervice.services;

import com.example.userervice.Exceptions.PasswordNotMatchingException;
import com.example.userervice.Exceptions.TokenNotExistException;
import com.example.userervice.Exceptions.UserNotExistException;
import com.example.userervice.dtos.UserDto;
import com.example.userervice.models.Token;
import com.example.userervice.models.User;
import com.example.userervice.repositories.TokenRepository;
import com.example.userervice.repositories.UserRepository;
import com.example.userervice.utils.JwtUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private TokenRepository tokenRepository;

    @Autowired
    private UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenRepository tokenRepository){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public User signUp(String email, String password, String name){

        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setHashedPassword(bCryptPasswordEncoder.encode(password));
        User user = userRepository.save(u);
        return user;
    }

    public Token logIn(String email, String password) throws UserNotExistException ,PasswordNotMatchingException {
        Optional<User> userOptional = userRepository.findFirstByEmail(email);
        if(userOptional.isEmpty()) {
            //throw user not exist exception
            throw new UserNotExistException("Please try with valid email");
        }

        User user = userOptional.get();

        //Validating password
        if(!bCryptPasswordEncoder.matches(password, user.getHashedPassword())){
            //Throw password not matching exception
            throw new PasswordNotMatchingException("Please try with valid password");
        }

        LocalDate today = LocalDate.now();
        LocalDate thirtyDateLater = today.plus(30, ChronoUnit.DAYS);

        //Convert LocalDate to date
        Date expiryDate = Date.from(thirtyDateLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setUser(user);
        token.setExpiryDate(expiryDate);
        token.setValue(JwtUtils.generateToken(email,user.getName()));
        return  tokenRepository.save(token);
    }

    public void logout(String value) throws TokenNotExistException {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedEquals(value,false);
        if(optionalToken.isEmpty()){
            //throw Token Not Exist
            throw new TokenNotExistException("The Token does not exist");
        }

        Token tkn = optionalToken.get();
        tkn.setDeleted(true);
        tokenRepository.save(tkn);
        return;
    }

    public UserDto validateToken(String token) throws TokenNotExistException {
       Optional <Token> tkn = tokenRepository.findByValueAndDeletedEqualsAndExpiryDateGreaterThan(token,false,new Date());
       if(tkn.isEmpty()) {
           throw new TokenNotExistException("The token does not exist");
       }

       //Self validating
      else if(!JwtUtils.validateToken(token)) return null;
       return UserDto.from(tkn.get().getUser());
    }
}
