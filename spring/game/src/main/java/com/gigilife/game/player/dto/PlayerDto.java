package com.gigilife.game.player.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {

    private Integer userId;
    private Integer age;
    private String disabilities;
    private String goals;
    private String hobbies;
    private String mobility;
    private String prizeIdeas;
}
