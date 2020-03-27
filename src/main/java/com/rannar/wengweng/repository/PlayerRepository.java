package com.rannar.wengweng.repository;

import com.rannar.wengweng.entity.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlayerRepository extends MongoRepository<Player, String> {
    Player findByName(String name);
}
