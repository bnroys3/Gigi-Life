package com.gigilife.game.challenge;

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
@Table(name = "Challenges")
public class ChallengeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer challengeId;

    @Column(nullable = false)
    private Integer gameId;

    @Column(nullable = false)
    private String challenge;

    @Column(nullable = false)
    private String difficulty;

    @DateTimeFormat(pattern = "dd-MM-YYYY")
    @Column(nullable = true)
    private Date expiryDate;

    @Column(nullable = false)
    private boolean complete;

    @Column(nullable = false)
    private boolean expired;

}
