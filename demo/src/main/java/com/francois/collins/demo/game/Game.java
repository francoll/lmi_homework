package com.francois.collins.demo.game;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.francois.collins.demo.game.card.Card;
import com.francois.collins.demo.game.deck.Deck;
import com.francois.collins.demo.game.deck.Shoe;
import com.francois.collins.demo.game.player.Player;

public class Game {
	private Long gameId;
	private String name;
	private List<Player> players = new ArrayList<Player>();
	private Shoe shoe;
	private List<Card> discardPile = new ArrayList<Card>();
	
	public Game () {
		gameId = new Date().getTime();
		shoe = new Shoe();
	}
	
	public Game(String pName) {
		super();
		name = pName;
	}
	
	public Long getGameId() {
		return this.gameId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * This method has package visibility in order to hide it from Game entity representation model assembler.
	 * 
	 * @return cards from the game deck
	 */
	List<Card> getCards() {
		return shoe.getCards();
	}
	
	public Integer getGameDeckSize() {
		return getCards().size();
	}
	
	public List<Card> getDiscardPile() {
		return discardPile;
	}

	public List<Card> deal(int pNumber) {
		return shoe.deal(pNumber);
	}
	
	/**
	 * Create a new deck, shuffle it and add it to the end of the current game deck.
	 */
	public void addDeck() {
		shoe.addDeck(new Deck(gameId).shuffle());
	}
	
	/**
	 * Add a deck that was created separately.
	 * 
	 * @param pDeck
	 */
	public void addDeck(Deck pDeck) {
		shoe.addDeck(pDeck.shuffle());
	}
	
	public void shuffle() {
		shoe.shuffle();
	}
	
	public void addPlayer(Player pPlayer) {
		players.add(pPlayer);
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	/**
	 * This method has package visibility in order to hide it from Game entity representation model assembler.
	 * 
	 * @return number of cards per suit in game deck
	 */
	ShoeStatsBySuit getStatsBySuit() {
		return new ShoeStatsBySuit(shoe);
	}
	
	/**
	 * This method has package visibility in order to hide it from Game entity representation model assembler.
	 * 
	 * @return number of each card in game deck
	 */
	ShoeStatsByCard getStatsByCard() {
		return new ShoeStatsByCard(shoe);
	}
	
	public String toString() {
		return "Game{" + "id=" + this.gameId +  ", name='" + this.getName() + '\'' +  '}';
	}

}
