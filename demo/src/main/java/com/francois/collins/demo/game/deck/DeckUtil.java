package com.francois.collins.demo.game.deck;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.francois.collins.demo.game.card.Card;

public class DeckUtil {
	
	/**
	 * Rearranges cards from existing list in random order
	 * 
	 * @param pCards list of Cards
	 */
	public static void shuffle(List<Card> pCards) {
		if (pCards != null) {
			Random random = new Random();
			List<Card> shuffled = new ArrayList<Card>();
			
			// transfer cards from one list to the other in random order
			while (!pCards.isEmpty()) {
				int idx = random.nextInt(pCards.size());
				shuffled.add(pCards.remove(idx));
			}
			
			// now that we burned through the original pile, re-add the shuffled ones from temporary list
			pCards.addAll(shuffled);
		}
	}
	
}
