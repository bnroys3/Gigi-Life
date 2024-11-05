package com.gigilife.game.user;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gigilife.game.security.jwt.JwtUtil;
import com.gigilife.game.user.dto.RegistrationDto;

import io.jsonwebtoken.security.InvalidKeyException;

import com.gigilife.game.user.dto.LoginDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /*
     * This function attempts to create a new user
     * input: registrationDto
     * returns: true if user is created, false otherwise.
     */
    public boolean createUser(RegistrationDto userDto){

        UserEntity user = new UserEntity();
        user.setEmail(userDto.getEmail());
        user.setFirst(userDto.getFirst());
        user.setLast(userDto.getLast());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        if(userRepository.findByEmail(user.getEmail())==null){
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /*
     * This function generates a session token based on a refresh token
     * input: refresh token
     * output: new session token
     */
    public String generateSessionToken(String refreshToken) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, IOException {

        return jwtUtil.getRefreshedToken(refreshToken, 600000);
        
    }


    /*
     * 
     * This funciton logs in a user based on the login DTO
     * input: login dto
     * output: result (String)
     */
    public String loginUser(LoginDto userLoginDto) {

        if(!validateCredentials(userLoginDto.getEmail(), userLoginDto.getPassword())){
            return "Bad_Credentials";
        }
        List<String> roles = new ArrayList<>();
        roles.add("User");

        try {
            //return refresh token
            return jwtUtil.generateAccessToken(userLoginDto.getEmail(), roles, 2592000000l);

        } catch(Exception e) {
            System.out.println(e);
            return "Token_Error";
        }
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean validateCredentials(String email, String password){
        UserEntity userEntity = findUserByEmail(email);

        return BCrypt.checkpw(password, userEntity.getPassword());
    }


    /*
     * This function returns data regarding the current user
     * input: authorization header (String)
     * output: Map containing email, first, last, & id
     */
    public Map<String, String> getPrincipalDetails(String authHeader){

        Map<String, String> details = new HashMap<>();

        String subject = getPrincipalSubject(authHeader);
        details.put("email", subject);

        UserEntity userEntity = findUserByEmail(subject);
        
        if(userEntity!=null) {
            details.put("first", userEntity.getFirst());
            details.put("last", userEntity.getLast());
            details.put("id", userEntity.getId().toString());
        }

        return details; 
    }

    /*
     * Function to get current users name
     * input: authheader string
     * Returns: subject extracted the JWT
     */
    public String getPrincipalSubject(String authHeader) {

        if (authHeader==null || authHeader.indexOf("Bearer ")!=0) {
            return "Anonymous";
        }

        String token = authHeader.substring(7);

        return jwtUtil.extractClaims(token).getSubject();
    }

}
