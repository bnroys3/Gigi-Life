package com.gigilife.game.game.playerInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GamePlayerInfo")
public class GamePlayerInfoEntity {

    @Id
    private Integer gameId;
    
    @Column(nullable = true)
    private String goals;
    
    @Column(nullable = true)
    private String prizes;

}
