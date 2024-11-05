package com.gigilife.game.player;


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
@Table(name = "Players")
public class PlayerEntity {

    @Id
    private Integer userId;

    @Column(nullable = true)
    private Integer age;

    @Column(nullable = true)
    private String hobbies;
    
    @Column(nullable = true)
    private String mobility;

    @Column(nullable = true)
    private String diabilties;
    
    @Column(nullable = true)
    private String goals;

    @Column(nullable = true)
    private String prizeIdeas;

}