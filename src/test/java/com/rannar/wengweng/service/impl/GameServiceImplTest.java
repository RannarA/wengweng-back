package com.rannar.wengweng.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rannar.wengweng.dto.Move;
import com.rannar.wengweng.dto.Position;
import com.rannar.wengweng.entity.*;
import com.rannar.wengweng.repository.GameStateRepository;
import com.rannar.wengweng.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void makeMove_shouldThrowExceptionIfCardsAndVictimsDontMatch() {
        Move move = new Move();
        move.setCards(Collections.singletonList(new Card()));
        move.setVictims(Arrays.asList("Mati", "Kalle"));

        assertThrows(IllegalArgumentException.class, () -> {
            gameServiceImpl.makeMove(move);
        });
    }

    @Test
    void makeMove_shouldThrowExceptionIfMoveInvalid() {
        Card card1 = new Card();
        Card card2 = new Card();
        card1.setRank("rank1");
        card2.setRank("rank4");
        Move move = new Move();
        move.setCards(Arrays.asList(card1, card2));
        move.setVictims(Arrays.asList("Mati", "Kalle"));

        Card currentCard = new Card();
        currentCard.setRank("rank1");
        Round currentRound = new Round();
        currentRound.setCurrentCard(currentCard);
        GameState state = new GameState(Collections.emptyList(), Collections.emptyList());
        state.setCurrentRound(currentRound);

        doReturn(Collections.singletonList(state)).when(gameStateRepository).findAll();

        assertThrows(IllegalArgumentException.class, () -> {
            gameServiceImpl.makeMove(move);
        });
    }

    @Test
    void makeMove_giveOutDrinks() {
        Player player = new Player();
        player.setName("Mati");
        Card card = new Card();
        card.setRank("rank1");
        Move move = new Move();
        move.setCards(Collections.singletonList(card));
        move.setPlayer(player);
        move.setVictims(Collections.singletonList("Malle"));

        Player p1 = new Player();
        Player p2 = new Player();
        p1.setName("Mati");
        p2.setName("Malle");
        Card currentCard = new Card();
        currentCard.setRank("rank1");
        Round currentRound = new Round();
        currentRound.setCurrentCard(currentCard);
        GameState current = new GameState(Collections.emptyList(), Arrays.asList(p1, p2));
        current.setCurrentRound(currentRound);

        doReturn(Collections.singletonList(current)).when(gameStateRepository).findAll();

        GameState result = gameServiceImpl.makeMove(move);

        assertEquals(1, getPlayerByName(result, "Malle").getDrinkCount());
    }

    @Test
    void makeMove_removePlayedCards() {
        Player player = new Player();
        player.setName("Mati");
        Card card = new Card();
        card.setRank("rank1");
        card.setId(5);
        Move move = new Move();
        move.setCards(Collections.singletonList(card));
        move.setPlayer(player);
        move.setVictims(Collections.singletonList("Malle"));

        Player p1 = new Player();
        Player p2 = new Player();
        Card c2 = new Card();
        c2.setId(10);
        c2.setRank("rank6");
        p1.setName("Mati");
        p1.setCards(new ArrayList<>(Arrays.asList(card, c2)));
        p2.setName("Malle");
        Card currentCard = new Card();
        currentCard.setRank("rank1");
        Round currentRound = new Round();
        currentRound.setCurrentCard(currentCard);
        GameState current = new GameState(Collections.emptyList(), Arrays.asList(p1, p2));
        current.setCurrentRound(currentRound);

        doReturn(Collections.singletonList(current)).when(gameStateRepository).findAll();

        GameState result = gameServiceImpl.makeMove(move);

        assertEquals(1, getPlayerByName(result, "Mati").getCards().size());
        assertEquals("rank6", getPlayerByName(result, "Mati").getCards().get(0).getRank());
    }

    @Test
    void roundStart_shouldThrowExceptionIfNoGame() {
        doReturn(new ArrayList<>()).when(gameStateRepository).findAll();
        assertThrows(IllegalStateException.class, () -> gameServiceImpl.roundStart());
    }

    @Test
    void roundStart_shouldThrowExceptionIfGameOver() {
        List<Card> currentPyramid = getDeck().subList(0, 3);
        currentPyramid.forEach(card -> card.setTurned(true));

        Row row1 = new Row();
        row1.setCards(Collections.singletonList(currentPyramid.get(0)));
        Row row2 = new Row();
        row2.setCards(Arrays.asList(currentPyramid.get(1), currentPyramid.get(2)));

        doReturn(Collections.singletonList(new GameState(Arrays.asList(row1, row2), Collections.emptyList())))
                .when(gameStateRepository).findAll();
        assertThrows(IllegalStateException.class, () -> gameServiceImpl.roundStart());
    }

    @Test
    void roundStart_shouldStartNextRound() {
        List<Card> currentPyramid = getDeck().subList(0, 3);

        Row row1 = new Row();
        row1.setCards(Collections.singletonList(currentPyramid.get(0)));
        Row row2 = new Row();
        row2.setCards(Arrays.asList(currentPyramid.get(1), currentPyramid.get(2)));

        doReturn(Collections.singletonList(new GameState(Arrays.asList(row1, row2), Collections.emptyList())))
                .when(gameStateRepository).findAll();

        GameState result = gameServiceImpl.roundStart();

        verify(gameStateRepository, times(1)).save(any(GameState.class));
    }

    @Test
    void getNextCardToFlip_shouldReturnNullIfAllTurned() {
        List<Card> currentPyramid = getDeck().subList(0, 3);
        currentPyramid.forEach(card -> card.setTurned(true));

        Row row1 = new Row();
        row1.setCards(Collections.singletonList(currentPyramid.get(0)));
        Row row2 = new Row();
        row2.setCards(Arrays.asList(currentPyramid.get(1), currentPyramid.get(2)));

        Position result = gameServiceImpl.getNextCardToFlip(Arrays.asList(row1, row2));

        assertNull(result);
    }

    @Test
    void getNextCardToFlip_shouldFlipFirst() {
        List<Card> currentPyramid = getDeck().subList(0, 3);

        Row row1 = new Row();
        row1.setCards(Collections.singletonList(currentPyramid.get(0)));
        Row row2 = new Row();
        row2.setCards(Arrays.asList(currentPyramid.get(1), currentPyramid.get(2)));

        Position result = gameServiceImpl.getNextCardToFlip(Arrays.asList(row1, row2));

        assertEquals(1, result.getRow());
        assertEquals(0, result.getCol());
    }

    @Test
    void getNextCardToFlip_shouldFlipLast() {
        List<Card> currentPyramid = getDeck().subList(0, 6);
        currentPyramid.get(1).setTurned(true);
        currentPyramid.get(2).setTurned(true);
        currentPyramid.get(3).setTurned(true);
        currentPyramid.get(4).setTurned(true);
        currentPyramid.get(5).setTurned(true);

        Row row1 = new Row();
        row1.setCards(Collections.singletonList(currentPyramid.get(0)));
        Row row2 = new Row();
        row2.setCards(Arrays.asList(currentPyramid.get(1), currentPyramid.get(2)));
        Row row3 = new Row();
        row3.setCards(Arrays.asList(currentPyramid.get(3), currentPyramid.get(4), currentPyramid.get(5)));

        Position result = gameServiceImpl.getNextCardToFlip(Arrays.asList(row1, row2, row3));

        assertEquals(0, result.getRow());
        assertEquals(0, result.getCol());
    }

    @Test
    void getNextCardToFlip_shouldFlipMiddle() {
        List<Card> currentPyramid = getDeck().subList(0, 6);
        currentPyramid.get(1).setTurned(true);
        currentPyramid.get(3).setTurned(true);
        currentPyramid.get(4).setTurned(true);
        currentPyramid.get(5).setTurned(true);

        Row row1 = new Row();
        row1.setCards(Collections.singletonList(currentPyramid.get(0)));
        Row row2 = new Row();
        row2.setCards(Arrays.asList(currentPyramid.get(1), currentPyramid.get(2)));
        Row row3 = new Row();
        row3.setCards(Arrays.asList(currentPyramid.get(3), currentPyramid.get(4), currentPyramid.get(5)));

        Position result = gameServiceImpl.getNextCardToFlip(Arrays.asList(row1, row2, row3));

        assertEquals(1, result.getRow());
        assertEquals(1, result.getCol());
    }

    private List<Player> initialPlayers() {
        return Arrays.asList(
                new Player("Mart"),
                new Player("Kati"),
                new Player("Peeter"),
                new Player("Malle")
        );
    }

    private Player getPlayerByName(GameState state, String name) {
        return state.getPlayers()
                .stream()
                .filter(player -> player.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(new Player());
    }

    private List<Card> getDeck() {
        ObjectMapper jsonMapper = new ObjectMapper();
        try {
            File cards = ResourceUtils.getFile("classpath:static/cards.json");
            return jsonMapper.readValue(cards, new TypeReference<>() {});
        } catch (IOException e) {
            throw new IllegalStateException("Failed to fetch cards");
        }
    }

}
