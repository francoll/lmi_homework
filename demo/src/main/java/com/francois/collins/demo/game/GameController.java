package com.francois.collins.demo.game;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.francois.collins.demo.game.card.Card;
import com.francois.collins.demo.game.card.CardModelAssembler;
import com.francois.collins.demo.game.card.CardNotFoundException;
import com.francois.collins.demo.game.deck.DeckUtil;
import com.francois.collins.demo.game.player.Player;
import com.francois.collins.demo.game.player.PlayerModelAssembler;
import com.francois.collins.demo.game.player.PlayerNotFoundException;
import com.francois.collins.demo.game.player.PlayerScore;
import com.francois.collins.demo.game.player.PlayerScoreAssembler;

@RestController
public class GameController {

	private final GameModelAssembler gameAssembler;
	private final PlayerModelAssembler playerAssembler;
	private final PlayerScoreAssembler scoreAssembler;
	private final CardModelAssembler cardAssembler;
	private final ShoeStatsBySuitModelAssembler suitStatsAssblr;
	private final ShoeStatsByCardModelAssembler cardStatsAssblr;
	private final Map<Long, Game> gameMap;

	public GameController(GameModelAssembler pGameAssembler, 
						  PlayerModelAssembler pPlayerAssembler,
						  PlayerScoreAssembler pScoreAssembler,
						  CardModelAssembler pCardAssembler,
						  ShoeStatsBySuitModelAssembler pSuitStatsAssblr, 
						  ShoeStatsByCardModelAssembler cardStatsAssblr) {
		super();
		this.gameAssembler = pGameAssembler;
		this.playerAssembler = pPlayerAssembler;
		this.scoreAssembler = pScoreAssembler;
		this.cardAssembler = pCardAssembler;
		this.suitStatsAssblr = pSuitStatsAssblr;
		this.cardStatsAssblr = cardStatsAssblr;
		this.gameMap = new HashMap<Long, Game>();
	}

	/**
	 * Get list of all games
	 * 
	 * @return list of games
	 */
	@GetMapping("/games")
	public CollectionModel<EntityModel<Game>> allGames() {

		List<EntityModel<Game>> games = gameMap.values().stream().map(gameAssembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(games, linkTo(methodOn(GameController.class).allGames()).withSelfRel());
	}

	/**
	 * Get a single game
	 * 
	 * @param id
	 * @return game details
	 */
	@GetMapping("/games/{id}")
	public EntityModel<Game> oneGame(@PathVariable Long id) {

		Game game = getGame(id);
		return gameAssembler.toModel(game);
	}
	
	/**
	 * Get games matching given name
	 * 
	 * @param name
	 * @return list of games
	 */
	// All games by name search
	@GetMapping("/games/names/{name}")
	public CollectionModel<EntityModel<Game>> gamesByName(@PathVariable String name) {

		List<EntityModel<Game>> games = gameMap.values().stream().filter(g -> (g.getName().equals(name))).map(gameAssembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(games, linkTo(methodOn(GameController.class).allGames()).withSelfRel());
	}

	/**
	 * Create new game.
	 * 
	 * @param pNewGame
	 * @return new game
	 */
	@PostMapping("/games")
	ResponseEntity<?> newGame(@RequestBody Game pNewGame) {

		//TODO: validate name unicity?
		gameMap.put(pNewGame.getGameId(), pNewGame);
		EntityModel<Game> entityModel = gameAssembler.toModel(pNewGame);

		return ResponseEntity //
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}
	
	/**
	 * Get list of players and their respective scores, ranked from high to low.
	 * 
	 * @param gameId
	 * @return list of player scores
	 */
	@GetMapping("/games/{gameId}/scores")
	public CollectionModel<EntityModel<PlayerScore>> allScores(@PathVariable Long gameId) {

		Game game = getGame(gameId);
		List<PlayerScore> scores = new ArrayList<PlayerScore>();
		for (Player p : game.getPlayers()) {
			scores.add(new PlayerScore(p));
		}
		
		List<EntityModel<PlayerScore>> scoreboard = scores.stream().sorted().map(scoreAssembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(scoreboard, linkTo(methodOn(GameController.class).allScores(gameId)).withSelfRel());
	}
	
	/**
	 * Get a single player
	 * 
	 * @param gameId
	 * @param playerId
	 * @return player
	 */
	@GetMapping("/games/{gameId}/players/{playerId}")
	public EntityModel<Player> onePlayer(@PathVariable Long gameId, @PathVariable Long playerId) {
		
		Game game = getGame(gameId);
		Player player = getPlayer(playerId, game);
		
		return playerAssembler.toModel(player);
	}
	
	/**
	 * Add new player to game.
	 * 
	 * @param id game id
	 * @param pNewPlayer
	 * @return new player
	 */
	@PostMapping("/games/{id}/players")
	ResponseEntity<?> newPlayer(@PathVariable Long id, @RequestBody Player pNewPlayer) {

		Game game = getGame(id);
		//TODO: Should we validate that if player already has a game id, it matches?
		pNewPlayer.setGameId(id);
		game.getPlayers().add(pNewPlayer);
		EntityModel<Player> entityModel = playerAssembler.toModel(pNewPlayer);

		return ResponseEntity //
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}
	
	/**
	 * Remove given player from game and put their cards in the discard pile.
	 * 
	 * @param gameId
	 * @param playerId
	 * @return
	 */
	@DeleteMapping("games/{gameId}/players/{playerId}")
	EntityModel<Game> deletePlayer(@PathVariable Long gameId, @PathVariable Long playerId) {
		Game game = getGame(gameId);
		Player player = getPlayer(playerId, game);
		game.getDiscardPile().addAll(player.getHand());
		game.getPlayers().remove(player);
		
		return gameAssembler.toModel(game);
	}

	/**
	 * Get all cards from the game deck.
	 * 
	 * @param gameId
	 * @return list of cards in the game deck
	 */
	@GetMapping("games/{gameId}/shoe")
	public CollectionModel<EntityModel<Card>> shoeCards(@PathVariable Long gameId) {

		Game game = getGame(gameId);

		List<EntityModel<Card>> cards = game.getCards().stream().map(cardAssembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(cards, linkTo(methodOn(GameController.class).shoeCards(gameId)).withSelfRel());
	}
	
	/**
	 * Shuffle game deck.
	 * 
	 * @param gameId
	 * @return the game deck contents
	 */
	@PutMapping("games/{gameId}/shuffle")
	public ResponseEntity<?> shuffle(@PathVariable Long gameId) {

		Game game = getGame(gameId);
		DeckUtil.shuffle(game.getCards());

		return ResponseEntity.ok().build();
	}

	/**
	 * Get card from anywhere in the game.
	 * 
	 * @param gameId
	 * @param cardId
	 * @return Card
	 */
	@GetMapping("games/{gameId}/cards/{cardId}")
	public EntityModel<Card> oneCard(@PathVariable Long gameId, @PathVariable Long cardId) {

		Game game = getGame(gameId);

		Card card = null;
		List<Card> cards = game.getCards();
		for (Card c : cards) {
			if (c.getId().equals(cardId)) {
				card = c;
			}
		}

		if (card == null) {
			throw new CardNotFoundException(cardId);
		}

		return cardAssembler.toModel(card);
	}

	/**
	 * Create new deck, sort it and add it to the end of the game deck.
	 * 
	 * @param id game id
	 * @return game details
	 */
	@PutMapping("/games/{id}/add_deck")
	ResponseEntity<?> addDeck(@PathVariable Long id) {

		Game game = getGame(id);
		game.addDeck();

		return ResponseEntity.ok(gameAssembler.toModel(game));
	}

	/**
	 *  Delete game.
	 *  
	 * @param id
	 * @return 204 HTTP status
	 */
	@DeleteMapping("/games/{id}")
	ResponseEntity<?> deleteGame(@PathVariable Long id) {
		gameMap.remove(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Get hand for given player.
	 * 
	 * @param gameId
	 * @param playerId
	 * @return
	 */
	@GetMapping("games/{gameId}/players/{playerId}/cards")
	public CollectionModel<EntityModel<Card>> onePlayerCards(@PathVariable Long gameId, @PathVariable Long playerId) {

		Game game = getGame(gameId);
		Player player = getPlayer(playerId, game);
		
		List<EntityModel<Card>> cards = player.getHand().stream().map(cardAssembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(cards);
	}

	/**
	 * Deal specified number of cards from shoe to given player.
	 * 
	 * @param gameId
	 * @param playerId
	 * @param number the number of cards to pull from the game deck
	 * @return the list of cards dealt
	 */
	@PutMapping("games/{gameId}/players/{playerId}/deal/{number}")
	public CollectionModel<EntityModel<Card>> dealCards(@PathVariable Long gameId, 
													    @PathVariable Long playerId, 
													    @PathVariable Integer number) {

		if (number < 1) {
			throw new RuntimeException("Number to deal must be positive");
		}
		
		Game game = getGame(gameId);
		Player player = getPlayer(playerId, game);
		
		List<Card> cards = game.deal(number);
		if (cards.isEmpty()) {
			//TODO: should we output some kind of feedback that stack was empty?
		}
		else {
			player.getHand().addAll(cards);
			Collections.sort(player.getHand());
		}

		List<EntityModel<Card>> hand = cards.stream().sorted().map(cardAssembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(hand, 
				linkTo(methodOn(GameController.class).onePlayerCards(gameId, playerId)).withRel("player_hand"));
	}
	
	/**
	 * Play given card from given player's hand. The played card is placed in the discard pile.
	 * 
	 * @param gameId
	 * @param playerId
	 * @param cardId
	 * @return the player's hand after playing
	 */
	@PutMapping("games/{gameId}/players/{playerId}/play/{cardId}")
	public CollectionModel<EntityModel<Card>> playCard(@PathVariable Long gameId, 
													   @PathVariable Long playerId, 
													   @PathVariable Long cardId) {

		Game game = getGame(gameId);
		Player player = getPlayer(playerId, game);
		
		Card card = player.getHand().stream().filter(c -> (c.getId().equals(cardId))).findFirst()
				.orElseThrow(() -> new CardNotFoundException(cardId));
		
		player.getHand().remove(card);
		game.getDiscardPile().add(card);

		List<EntityModel<Card>> cards = player.getHand().stream().map(cardAssembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(cards);
	}
	
	/**
	 * Get structure representing how many cards of each suit are in the game deck.
	 * 
	 * @param gameId
	 * @return suit statistics model
	 */
	@GetMapping("games/{gameId}/statsBySuit")
	public EntityModel<ShoeStatsBySuit> getShoeStatsBySuit(@PathVariable Long gameId) {
		Game game = getGame(gameId);
		ShoeStatsBySuit stats = game.getStatsBySuit();
		
		return suitStatsAssblr.toModel(stats);
	}
	
	/**
	 * Get structure representing how many copies of each card are in the game deck.
	 * 
	 * @param gameId
	 * @return card statistics model
	 */
	@GetMapping("games/{gameId}/statsByCard")
	public EntityModel<ShoeStatsByCard> getShoeStatsByCard(@PathVariable Long gameId) {
		Game game = getGame(gameId);
		ShoeStatsByCard stats = game.getStatsByCard();
		
		return cardStatsAssblr.toModel(stats);
	}
	
	private Game getGame(Long gameId) {

		Game game = gameMap.get(gameId);
		if (game == null) {
			throw new GameNotFoundException(gameId);
		}
		return game;
	}
	
	private Player getPlayer(Long playerId, Game game) {
		Player player = game.getPlayers().stream().filter(p -> (p.getId().equals(playerId))).findFirst()
				.orElseThrow(() -> new PlayerNotFoundException(playerId));
		return player;
	}

}
