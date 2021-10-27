package com.francois.collins.demo.game;

import com.francois.collins.demo.game.card.Card.SUITS;
import com.francois.collins.demo.game.deck.Shoe;

import static com.francois.collins.demo.game.card.Card.SUITS.*;

public class ShoeStatsBySuit {
	private long heartsTotal = 0;
	private long spadesTotal = 0;
	private long clubsTotal = 0;
	private long diamsTotal = 0;
	private Shoe shoe;
	
	public ShoeStatsBySuit(Shoe pShoe) {
		shoe = pShoe;
		
		heartsTotal = getSuitTotal(HEARTS);
		spadesTotal = getSuitTotal(SPADES);
		clubsTotal = getSuitTotal(CLUBS);
		diamsTotal = getSuitTotal(DIAMONDS);
	}
	
	private long getSuitTotal(SUITS pSuit) {
		return shoe.getCards().stream().filter(c -> c.getSuit().equals(pSuit)).count();
	}

	public long getHeartsTotal() {
		return heartsTotal;
	}

	public long getSpadesTotal() {
		return spadesTotal;
	}

	public long getClubsTotal() {
		return clubsTotal;
	}

	public long getDiamsTotal() {
		return diamsTotal;
	}
}
