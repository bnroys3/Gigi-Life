package com.gigilife.game.game;

import org.springframework.web.bind.annotation.RestController;

import com.gigilife.game.game.dto.JoinGameDto;
import com.gigilife.game.game.dto.NewGameDto;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    public GameController(GameService gameService){
        this.gameService = gameService;
    }

    //find game by userId
    @GetMapping("/by-user/{id}")
    public ResponseEntity<List<GameEntity>> getGamesByUserId(@PathVariable("id") String userId) {

        List<GameEntity> games = gameService.findGamesByUserId(Integer.parseInt(userId));

        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    //find game by gameId
    @GetMapping("/by-game/{id}")
    public ResponseEntity<GameEntity> getGameByGameId(@PathVariable("id") String gameId) {

        GameEntity game = gameService.findGameByGameId(Integer.parseInt(gameId));
        
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    //create new game
    @PostMapping("/create")
    public ResponseEntity<GameEntity> postNewGame(@RequestBody NewGameDto newGameDto) {

        GameEntity game = gameService.getNewGame(newGameDto.getAdminId(), newGameDto.getName());

        gameService.addGamePlayerInfo(game.getGameId(), newGameDto.getGoals(), newGameDto.getPrizes());
        
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    //join existing game
    @PostMapping("/join")
    public ResponseEntity<Boolean> postJoinGame(@RequestBody JoinGameDto joinGameDto) {

        Boolean success = gameService.joinGame(joinGameDto.getGameId(), joinGameDto.getPlayerId());
        
        return new ResponseEntity<>(success, HttpStatus.OK);
    }
    
}
