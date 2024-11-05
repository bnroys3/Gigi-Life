package com.gigilife.game.game;

import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gigilife.game.game.playerInfo.GamePlayerInfoEntity;
import com.gigilife.game.game.playerInfo.GamePlayerInfoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    GamePlayerInfoRepository gamePlayerInfoRepository;

    public List<GameEntity> findGamesByUserId(Integer userId) {
        List<GameEntity> games = new ArrayList<GameEntity>();
        games.addAll(gameRepository.findByAdminId(userId));
        games.addAll(gameRepository.findByPlayerId(userId));
        return games;
    }


    public GameEntity findGameByGameId(Integer gameId){
        Optional<GameEntity> opGame = gameRepository.findById(gameId);
        if(opGame.isPresent()){
            return opGame.get();
        }
        return null;
    }

    public GameEntity getNewGame(Integer adminId, String name){
        
        List<GameEntity> games = new ArrayList<GameEntity>();

        //find any games the user is already administrating to determine whether this is a new request
        //or if an empty game has already been created under the adminId.
        games.addAll(gameRepository.findByAdminId(adminId));
        for(Iterator<GameEntity> iterator = games.iterator(); iterator.hasNext(); ) {
            GameEntity game = iterator.next();
            if (game.getPlayerId() != null) {
                iterator.remove();
            }
        }

        GameEntity game;
        //if there are more than 1 remaining games after all with players have been removed
        //return the pre-exisiting empty game.
        if(games.size() > 1){
            game = games.get(0);
            game.setName(name);
        } else {
            //otherwise generate a new game.
            game = new GameEntity();
            game.setAdminId(adminId);
            game.setPoints(0);
            game.setName(name);
        }

        gameRepository.save(game);
        return game;
    }

    public void addGamePlayerInfo(Integer gameId, String goals, String prizes) {
        GamePlayerInfoEntity gamePlayerInfoEntity = new GamePlayerInfoEntity(gameId, goals, prizes);
        gamePlayerInfoRepository.save(gamePlayerInfoEntity);
    }

    public String getGamePlayerGoals(Integer gameId) {

        Optional<GamePlayerInfoEntity> opGameInfo = gamePlayerInfoRepository.findById(gameId);
        if(opGameInfo.isPresent()){
            return opGameInfo.get().getGoals();
        }
        return null;
    }

    public boolean joinGame(Integer gameId, Integer playerId){
        GameEntity game = findGameByGameId(gameId);
        if(game==null){
            return false;
        }
        game.setPlayerId(playerId);
        Date utilDate = new Date();
        game.setStartDate(new java.sql.Date(utilDate.getTime()));
        game.setExpiryDate(new java.sql.Date(utilDate.getTime()+2629746000l));
        gameRepository.save(game);
        return true;
    }

}
