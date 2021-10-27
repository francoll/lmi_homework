package com.francois.collins.demo.game.player;

import com.francois.collins.demo.game.card.Card;

public class PlayerScore implements Comparable<PlayerScore> {
	private Player player;
	Integer score = 0;
	
	public PlayerScore(Player pPlayer) {
		player = pPlayer;
		for (Card c : player.getHand()) {
			score += c.getValue();
		}
	}
	
	public Long getPlayerId() {
		return player.getId();
	}
	
	public String getName() {
		return player.getName();
	}
	
	public Long getGameId() {
		return player.getGameId();
	}
	
	public Integer getScore() {
		return score;
	}

	@Override
	public int compareTo(PlayerScore pScore) {
		return -this.getScore().compareTo(pScore.getScore());
	}

}
