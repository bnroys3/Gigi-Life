package com.gigilife.game.game;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "Games")
public class GameEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer gameId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer adminId;
    
    @Column(nullable = true)
    private Integer playerId;
    
    @Column(nullable = false)
    private Integer points;

    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @Column(nullable = true)
    private Date startDate;

    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @Column(nullable = true)
    private Date expiryDate;

}

