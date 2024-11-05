package com.gigilife.game.game.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinGameDto {

    private Integer gameId;
    
    private Integer playerId;

}