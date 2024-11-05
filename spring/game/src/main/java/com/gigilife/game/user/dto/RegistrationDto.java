package com.gigilife.game.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/*
 * 
 * Registration DTO for new users
 * /api/user/register endpoint
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {

    private String email;

    private String first;

    private String last;

    private String password;
    
}
