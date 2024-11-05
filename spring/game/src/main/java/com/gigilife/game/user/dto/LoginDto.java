package com.gigilife.game.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * DTO for login API (/api/user/login endpoint)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    private String email;

    private String password;

}
