package com.gigilife.game.player;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gigilife.game.game.GameService;
import com.gigilife.game.player.dto.PlayerDto;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GameService gameService;

    public void putPlayer(PlayerDto playerDto){

        PlayerEntity player = new PlayerEntity();
        player.setAge(playerDto.getAge());
        player.setDiabilties(playerDto.getDisabilities());
        player.setGoals(playerDto.getGoals());
        player.setHobbies(playerDto.getHobbies());
        player.setMobility(playerDto.getMobility());
        player.setPrizeIdeas(playerDto.getPrizeIdeas());
        player.setUserId(playerDto.getUserId());

        playerRepository.save(player);
    }

    public PlayerEntity findPlayerById(Integer userId) {
        
        Optional<PlayerEntity> opPlayer = playerRepository.findById(userId);
        if(opPlayer.isPresent()){
            return opPlayer.get();
        }
        return null;
    }

    public String getPlayerProfile(Integer userId) {

        PlayerEntity playerEntity = findPlayerById(userId);
        String playerProfile = "USER: {";
        playerProfile += "age: " + playerEntity.getAge() + ";";
        playerProfile += "hobbies: " + playerEntity.getHobbies() + ";";
        if(playerEntity.getDiabilties()!="N/A") {
            playerProfile += "disabilities: " + playerEntity.getDiabilties() + ";";
        }
        if(playerEntity.getMobility()!="N/A") {
            playerProfile += "mobiltiy limitations: " + playerEntity.getMobility() + ";";
        }
        playerProfile += "goals: " + playerEntity.getGoals() + "}";
        
        return playerProfile;
    }

}
