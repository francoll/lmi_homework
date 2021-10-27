package com.francois.collins.demo.game.deck;

import java.util.ArrayList;
import java.util.List;

import com.francois.collins.demo.game.card.Card;
import static com.francois.collins.demo.game.card.Card.SUITS;

public class Deck {
	
	private long gameId;
	private List<Card> cards = new ArrayList<Card>();

	/**
	 * Initialize deck on creation with all cards in order
	 * @param pGameId
	 */
	public Deck(long pGameId) {
		gameId = pGameId;
		SUITS suit = SUITS.HEARTS;
		int val = 1;
		for(int i = 0; i < 52; i++) {
			
			Card card = new Card(gameId, suit, val);
			cards.add(card);
			val++;
			if (val == 14 && i < 51) {
				val = 1;
				suit = SUITS.values()[suit.ordinal() + 1];
			}
		}
	}
	
	public List<Card> getCards() {
		return cards;
	}
	
	public Deck shuffle() {
		DeckUtil.shuffle(cards);
		return this;
	}
}
