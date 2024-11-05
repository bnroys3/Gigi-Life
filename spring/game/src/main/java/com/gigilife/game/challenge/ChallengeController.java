package com.gigilife.game.challenge;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/challenge")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService){
        this.challengeService = challengeService;
    }

    @GetMapping("/generate-weekly/{id}")
    public ResponseEntity<Map<String, String>> getNewWeekly(@PathVariable("id") String gameId) {

        Map<String, String> response = challengeService.getWeekly(Integer.parseInt(gameId));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/current/{id}")
    public ResponseEntity<Map<String, Object>> getCurrentChallenges(@PathVariable("id") String gameId) {

        Map<String, Object> response = challengeService.getCurrentChallenges(Integer.parseInt(gameId));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    

}
