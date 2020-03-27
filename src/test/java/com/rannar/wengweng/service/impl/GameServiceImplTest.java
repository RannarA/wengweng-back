package com.rannar.wengweng.service.impl;

import com.rannar.wengweng.entity.GameState;
import com.rannar.wengweng.entity.Player;
import com.rannar.wengweng.repository.GameStateRepository;
import com.rannar.wengweng.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class GameServiceImplTest {
    private GameServiceImpl gameServiceImpl;

    private PlayerRepository playerRepository;

    private GameStateRepository gameStateRepository;

    @BeforeEach
    void setUp() {
        playerRepository = mock(PlayerRepository.class);
        gameStateRepository = mock(GameStateRepository.class);
        gameServiceImpl = new GameServiceImpl(playerRepository, gameStateRepository);

        doReturn(initialPlayers()).when(playerRepository).findAll();
    }

    @Test
    void startGame() {
    }

    @Test
    void initGameState_shouldNotReturnNull() {
        assertNotNull(gameServiceImpl.initGameState());
    }

    @Test
    void initGameState_shouldReturnPyramidWithCorrectRows() {
        GameState result = gameServiceImpl.initGameState();
        assertEquals(5, result.getPyramid().size());
    }

    @Test
    void initGameState_shouldHaveCorrectAmountOfCardsInPyramidRows() {
        GameState result = gameServiceImpl.initGameState();
        assertEquals(1, result.getPyramid().get(0).getCards().size());
        assertEquals(2, result.getPyramid().get(1).getCards().size());
        assertEquals(3, result.getPyramid().get(2).getCards().size());
        assertEquals(4, result.getPyramid().get(3).getCards().size());
        assertEquals(5, result.getPyramid().get(4).getCards().size());
    }

    @Test
    void initGameState_shouldHaveCorrectAmountOfPlayers() {
        GameState result = gameServiceImpl.initGameState();
        assertEquals(4, result.getPlayers().size());
    }

    @Test
    void initGameState_playersCardsShouldTotalRemains() {
        GameState result = gameServiceImpl.initGameState();

        int total = result.getPlayers().stream().mapToInt(player -> player.getCards().size()).sum();
        assertEquals(37, total);
    }

    private List<Player> initialPlayers() {
        return Arrays.asList(
                new Player("Mart"),
                new Player("Kati"),
                new Player("Peeter"),
                new Player("Malle")
        );
    }

}
