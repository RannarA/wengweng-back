package com.rannar.wengweng.rest;

import com.rannar.wengweng.entity.Player;
import com.rannar.wengweng.repository.PlayerRepository;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/player")
public class PlayerResource {

    private final PlayerRepository playerRepository;

    public PlayerResource(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @GetMapping
    public ResponseEntity<List<Player>> getAll() {
        return ResponseEntity.ok(playerRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Player> save(@RequestBody Player player) {
        return ResponseEntity.ok(playerRepository.save(player));
    }
}
