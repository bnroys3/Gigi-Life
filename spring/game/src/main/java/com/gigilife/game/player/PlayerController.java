package com.gigilife.game.player;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gigilife.game.player.dto.PlayerDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }
    
    @GetMapping("/by-id/{id}")
    public ResponseEntity<HashMap<String, Object>> getProfile(@PathVariable("id") String playerId) {

        HashMap<String, Object> response = new HashMap<>();

        PlayerEntity player = playerService.findPlayerById(Integer.parseInt(playerId));

        response.put("player", player);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/put")
    public ResponseEntity<String> postMethodName(@RequestBody PlayerDto playerDto) {

        playerService.putPlayer(playerDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    



}
