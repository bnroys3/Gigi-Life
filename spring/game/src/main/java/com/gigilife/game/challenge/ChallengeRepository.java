package com.gigilife.game.challenge;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


public interface ChallengeRepository extends CrudRepository<ChallengeEntity, Integer> {

    List<ChallengeEntity> findByGameId(Integer gameId);
    
}
