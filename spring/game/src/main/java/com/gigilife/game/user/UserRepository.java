package com.gigilife.game.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    UserEntity findByEmail(String email);

}