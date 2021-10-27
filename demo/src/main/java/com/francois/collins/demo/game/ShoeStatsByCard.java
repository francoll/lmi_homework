package com.francois.collins.demo.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.francois.collins.demo.game.card.Card;
import com.francois.collins.demo.game.card.Card.SUITS;
import static com.francois.collins.demo.game.card.Card.SUITS.*;
import com.francois.collins.demo.game.deck.Shoe;

public class ShoeStatsByCard {
	
	private Map<Integer, Map<String, Integer>> stats = new TreeMap<Integer, Map<String, Integer>>();
	private List<String> faces = new ArrayList<String>();
	private Shoe shoe;
	
	public ShoeStatsByCard(Shoe pShoe) {
		shoe = pShoe;
		
		// separate cards by suit, because we want to keep the same suit order,
		// then reverse the built-in Card sort on value (since they are all the same suit)
		// and add those groups in the resulting order.
		// Then, iterate over the list to get individual totals for each suit - value combination.
		// We have to use a TreeMap and assign a sequence number to each card face to keep the array order,
		// otherwise a regular HashMap does not keep the order in which we add items.
		List<Card> gameDeck = new ArrayList<Card>();
		gameDeck.addAll(getSuitSubset(HEARTS));
		gameDeck.addAll(getSuitSubset(SPADES));
		gameDeck.addAll(getSuitSubset(CLUBS));
		gameDeck.addAll(getSuitSubset(DIAMONDS));
		
		gameDeck.stream().map(c -> c.getFace()).forEach(face -> computeTotal(face));
	}
	
	public Map<Integer, Map<String, Integer>> getStats() {
		return stats;
	}

	private List<Card> getSuitSubset(SUITS pSuit) {
		return shoe.getCards().stream().filter(c -> c.getSuit().equals(pSuit))
				.sorted(Collections.reverseOrder()).toList();
	}
	
	private void computeTotal(String pFaceValue) {
		
		if (!faces.contains(pFaceValue)) {
			faces.add(pFaceValue);
		}
		
		Integer idx = faces.size();
		
		Map<String, Integer> tot = stats.get(idx);
		if (tot == null) {
			tot = new HashMap<String, Integer>();
			tot.put(pFaceValue, 1);
			stats.put(idx, tot);
		}
		else {
			Integer count = tot.get(pFaceValue);
			tot.put(pFaceValue, ++count);
		}
	}
}
