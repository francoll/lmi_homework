package com.francois.collins.demo.game.deck;

import java.util.ArrayList;
import java.util.List;

import com.francois.collins.demo.game.card.Card;

public class Shoe {
	private List<Card> cards = new ArrayList<Card>();
	
	public Shoe() {
		
	}
	
	public List<Card> getCards() {
		return cards;
	}

	public void addDeck(Deck pDeck) {
		cards.addAll(pDeck.getCards());
	}
	
	public List<Card> deal(int pNumber) {
		List<Card> newCards = new ArrayList<Card>();
		if (!cards.isEmpty()) {
			do {
				newCards.add(cards.remove(0));
			} while (!cards.isEmpty() && newCards.size() < pNumber);
		}
		return newCards;
	}
	
	public void shuffle() {
		DeckUtil.shuffle(cards);
	}
	
}
