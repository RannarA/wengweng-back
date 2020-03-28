package com.rannar.wengweng.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rannar.wengweng.dto.Move;
import com.rannar.wengweng.dto.Position;
import com.rannar.wengweng.entity.*;
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
import java.util.stream.Collectors;

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
    public GameState startGame() {
        GameState gameState = initGameState();
        return gameStateRepository.save(gameState);
    }

    @Override
    public GameState getCurrentGame() {
        List<GameState> gameStates = this.gameStateRepository.findAll();
        if (gameStates.isEmpty()) {
            return null;
        }

        // for now there should only be one game at a time
        return gameStates.get(0);
    }

    @Override
    public GameState move(Move move) {
        GameState game = makeMove(move);
        return gameStateRepository.save(game);
    }

    GameState makeMove(Move move) {
        GameState game = getCurrentGame();
        validateMove(move, game);

        List<Player> victims = move.getVictims()
                .stream()
                .map(game::getPlayer)
                .collect(Collectors.toList());

        victims.forEach(victim -> victim.addDrinks(1));

        move.getCards().forEach(card -> game.removeCard(move.getPlayer().getName(), card));
        return game;
    }

    private void validateMove(Move move, GameState game) {
        if (move.getCards().size() != move.getVictims().size()) {
            throw new IllegalArgumentException("Wrong input");
        }

        move.getCards()
                .stream()
                .filter(card -> !game.getCurrentRound().getCurrentCard().getRank().equalsIgnoreCase(card.getRank()))
                .findFirst()
                .ifPresent(card -> {
                    throw new IllegalArgumentException("Cant play " + card.getRank());
                });
    }

    @Override
    public Player getPlayer(String name) {
        GameState currentGame = getCurrentGame();
        return currentGame.getPlayers()
                .stream()
                .filter(player -> player.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public void endGame() {
        this.gameStateRepository.deleteAll();
        this.playerRepository.deleteAll();
    }

    @Override
    public GameState roundStart() {
        GameState game = getCurrentGame();
        if (game == null) {
            throw new IllegalStateException("No game found");
        }

        Position nextPos = getNextCardToFlip(game.getPyramid());
        if (nextPos == null) {
            throw new IllegalStateException("Game over");
        }

        Card nextCard = game.getPyramid().get(nextPos.getRow()).getCards().get(nextPos.getCol());
        nextCard.setTurned(true);

        Round round = new Round();
        round.setCurrentCard(nextCard);

        game.setCurrentRound(round);

        return gameStateRepository.save(game);
    }

    GameState initGameState() {
        List<Card> deck = getDeck();

        Collections.shuffle(deck);

        List<Row> pyramid = createPyramid(deck.subList(0, 15));
        List<Player> players = createPlayers(deck.subList(15, deck.size()));

        return new GameState(pyramid, players);
    }

    Position getNextCardToFlip(List<Row> pyramid) {
        int row = pyramid.size() - 1;
        int col = 0;
        while(pyramid.get(row).getCards().get(col).isTurned()) {
            if (col == pyramid.get(row).getCards().size() - 1) {
                col = 0;

                if (row == 0) {
                    return null;
                }

                row--;
            } else {
                col++;
            }
        }

        return new Position(row, col);
    }

    private List<Player> createPlayers(List<Card> playerCards) {
        List<Player> players = playerRepository.findAll();
        int playerIndex = 1;
        for (Card playerCard : playerCards) {
            playerCard.setTurned(true);
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
