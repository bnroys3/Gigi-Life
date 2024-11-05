package com.gigilife.game.game;

import org.springframework.data.repository.CrudRepository;
import java.util.List;


public interface GameRepository extends CrudRepository<GameEntity, Integer> {

    List<GameEntity> findByAdminId(Integer adminId);
    
    List<GameEntity> findByPlayerId(Integer playerId);

}