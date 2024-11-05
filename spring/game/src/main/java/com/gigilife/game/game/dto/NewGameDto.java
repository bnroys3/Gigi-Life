package com.gigilife.game.game.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewGameDto {

    private Integer adminId;
    private String name;
    private String goals;
    private String prizes;

}