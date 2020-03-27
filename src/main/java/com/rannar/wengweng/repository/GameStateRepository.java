package com.rannar.wengweng.repository;

import com.rannar.wengweng.entity.GameState;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameStateRepository extends MongoRepository<GameState, Integer> {
}
