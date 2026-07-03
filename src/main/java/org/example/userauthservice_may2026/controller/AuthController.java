package org.example.userauthservice_may2026.controller;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice_may2026.dtos.LoginRequestDto;
import org.example.userauthservice_may2026.dtos.SignUpRequestDto;
import org.example.userauthservice_may2026.dtos.UserDto;
import org.example.userauthservice_may2026.dtos.ValidateTokenRequestDto;
import org.example.userauthservice_may2026.exceptions.PasswordMismatchException;
import org.example.userauthservice_may2026.exceptions.UserAlreadyException;
import org.example.userauthservice_may2026.exceptions.UserNotSignedUpException;
import org.example.userauthservice_may2026.models.Role;
import org.example.userauthservice_may2026.models.User;
import org.example.userauthservice_may2026.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        try {
            User user = authService.signUp(signUpRequestDto.getName(), signUpRequestDto.getEmail(), signUpRequestDto.getPassword());
            UserDto userDto=from(user);
            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        }
        catch(Exception exception){
            throw exception;
            //return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }


    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto){
        try {
            Pair<User,String> pair = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            User user=pair.a;
            String token=pair.b;
            MultiValueMap<String,String> header=new LinkedMultiValueMap<>();
            header.add(HttpHeaders.SET_COOKIE,token);
            header.add("name","aish ammu");

            UserDto userDto = from(user);
            return new ResponseEntity<>(userDto,header
                    ,HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }

    }


    @PostMapping("/validateToken")
    public Boolean validateToken(ValidateTokenRequestDto validateTokenRequestDto){
    Boolean flag=authService.validateToken(validateTokenRequestDto.getToken());
    return flag;

    }

    public UserDto from(User user){
        UserDto userDto=new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        List<Role> roles=user.getRoles();
        List<String> uroles=new ArrayList<>();
        for(Role r:roles){
            String value=r.getValue();
            uroles.add(value);
        }
        userDto.setRoles(uroles);
        return userDto;
    }
}
