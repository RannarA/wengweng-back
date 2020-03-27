package com.rannar.wengweng.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rannar.wengweng.dto.GameStateDTO;
import com.rannar.wengweng.entity.Card;
import com.rannar.wengweng.entity.GameState;
import com.rannar.wengweng.entity.Player;
import com.rannar.wengweng.entity.Row;
import com.rannar.wengweng.repository.GameStateRepository;
import com.rannar.wengweng.repository.PlayerRepository;
import com.rannar.wengweng.service.GameService;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {
    private final PlayerRepository playerRepository;

    private final GameStateRepository gameStateRepository;

    public GameServiceImpl(PlayerRepository playerRepository,
                           GameStateRepository gameStateRepository) {
        this.playerRepository = playerRepository;
        this.gameStateRepository = gameStateRepository;
    }

    @Override
    public GameStateDTO startGame() {
        GameState gameState = initGameState();
        return new GameStateDTO(gameStateRepository.save(gameState));
    }

    GameState initGameState() {
        List<Card> deck = getDeck();

        Collections.shuffle(deck);

        List<Row> pyramid = createPyramid(deck.subList(0, 15));
        List<Player> players = createPlayers(deck.subList(15, deck.size()));

        return new GameState(pyramid, players);
    }

    private List<Player> createPlayers(List<Card> playerCards) {
        List<Player> players = playerRepository.findAll();
        int playerIndex = 1;
        for (Card playerCard : playerCards) {
            players.get(playerIndex - 1).getCards().add(playerCard);

            if (playerIndex == players.size()) {
                playerIndex = 1;
            } else {
                playerIndex++;
            }
        }
        return players;
    }

    private List<Row> createPyramid(List<Card> pyramidCards) {
        List<Row> pyramid = new ArrayList<>();

        int cardIndex = 0;
        for (int row = 0; row < 5 && cardIndex < pyramidCards.size(); row++) {
            List<Card> rowCards = new ArrayList<>();
            for (int col = 0; col <= row ; col++) {
                rowCards.add(pyramidCards.get(cardIndex++));
            }
            pyramid.add(new Row(rowCards));
        }

        return pyramid;
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
