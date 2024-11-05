package com.gigilife.game.user;

import org.springframework.web.bind.annotation.RestController;

import com.gigilife.game.user.dto.RegistrationDto;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import com.gigilife.game.user.dto.LoginDto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }



    /*
     * Endpoint for login requests
     * Uses refresh + session tokens via JWT
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> postLogin(@RequestBody LoginDto loginDto, HttpServletResponse response) {

        try {
            
            //attempt to generate refresh token
            String refreshToken = userService.loginUser(loginDto);

            if(refreshToken.equals("Token_Error")){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(refreshToken.equals("Bad_Credentials")){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            //If a refresh token is generated, send the client a refresh cookie
            Cookie refreshCookie = new Cookie("refresh-token", refreshToken);
            refreshCookie.setMaxAge(2592000);
            refreshCookie.setSecure(true);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/api/user/refresh");
            response.addCookie(refreshCookie);
            
            //Generate a session token using the new refresh token
            String sessionToken = userService.generateSessionToken(refreshToken);
            HashMap<String, String> sessionTokenMap = new HashMap<>();
            sessionTokenMap.put("sessionToken", sessionToken);

            
            return new ResponseEntity<>(sessionTokenMap, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
    }


    /*
    * Endpoint for new user registration
    */
    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> postRegister(@RequestBody RegistrationDto userDto) {
        try {
            Map<String,String> response = new HashMap<>();
            
            //Attempt to create the user
            if(userService.createUser(userDto)){
                response.put("result", "success");
            } else {
                response.put("result", "Email is already associated with an account.");
            }
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch(Exception e) {

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    

    /*
     * Endpoint to return information about the user who is currently logged in via the authorization header
     */
    @GetMapping("/principal")
    public ResponseEntity<Map<String, String>> getPrincipal(@RequestHeader("Authorization") String authHeader){

        try {
            //attempt to retrieve details about the user
            Map<String, String> userDetails = userService.getPrincipalDetails(authHeader);
            return new ResponseEntity<>(userDetails, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /*
     * 
     * Endpoint to refresh the session token
     */
    @GetMapping("/refresh")
    public ResponseEntity<Map<String, String>> getRefresh(@CookieValue(name="refresh-token", required=false) String refreshToken){

        try {
        
            HashMap<String, String> sessionTokenMap = new HashMap<>();
            
            if(refreshToken==null){
                sessionTokenMap.put("outcome", "failure");
                return new ResponseEntity<>(sessionTokenMap, HttpStatus.OK); 
            }
            
            //if there is a refresh token cookie in the request, generate a new session token.
            String sessionToken = userService.generateSessionToken(refreshToken);
           
            sessionTokenMap.put("outcome", "success");
            sessionTokenMap.put("sessionToken", sessionToken);

            
            return new ResponseEntity<>(sessionTokenMap, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
    }


    /*
     * 
     * This endpoint logs out the user
     */
    @GetMapping("/logout")
    public ResponseEntity<Map<String, String>> getLogout(HttpServletResponse response) {

        //method clears the refresh cookie by setting it to null

        //create cookie
        Cookie refreshCookie = new Cookie("refresh-token", null);
        refreshCookie.setMaxAge(0);
        refreshCookie.setSecure(true);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/api/user/refresh");

        //add the cookie to the response
        response.addCookie(refreshCookie);

        HashMap<String, String> responseMap = new HashMap<>();
        responseMap.put("outcome", "success");
        
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
    
}
