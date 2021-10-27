package com.francois.collins.demo.game.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.francois.collins.demo.game.card.Card;

public class Player {
	private Long playerId;
	private Long gameId;
	private String name;
	
	private List<Card> hand = new ArrayList<Card>();
	
	public Player() {
		playerId = new Date().getTime();
	}
	public Player(String pName) {
		super();
		setName(pName);
	}
	
	public Player(String pName, Long pGameId) {
		super();
		gameId = pGameId;
		setName(pName);
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Long getId() {
		return playerId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Card> getHand() {
		return hand;
	}

	public Long getGameId() {
		return gameId;
	}
	
}
