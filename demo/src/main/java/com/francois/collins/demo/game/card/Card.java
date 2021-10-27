package com.francois.collins.demo.game.card;

import java.util.Date;
import java.util.Objects;

/**
 * This class represents a playing card, and the information to display it (name, suit, color, etc).
 * Each card has a unique id, since there can be several copies with the same value 
 * if multiple decks were added to the game stack.
 * 
 * @author fran_coll
 *
 */
public class Card implements Comparable<Card>{
	private Long id;
	private Long gameId;
	private SUITS suit;
	private Integer value;
	
	public final static String[] displayValues = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
	
	public enum COLOR {BLACK, RED};
	
	public enum SUITS {
		HEARTS("Hearts", COLOR.RED, "♥", "&hearts;"), 
		SPADES("Spades", COLOR.BLACK, "♠", "&spades;"), 
		CLUBS("Clubs", COLOR.BLACK, "♣", "&clubs;"), 
		DIAMONDS("Diamonds", COLOR.RED, "♦", "&diams;");
		
		private final String suitname;
		private COLOR color;
		private final String symbol;
		private final String entity;

		SUITS(String pName, COLOR pColor, String pSymbol, String pHTMLEntity) {
			suitname = pName;
			color = pColor;
			symbol = pSymbol;
			entity = pHTMLEntity;
		}

		public String suitname() {
			return suitname;
		}

		public COLOR color() {
			return color;
		}

		public String symbol() {
			return symbol;
		}

		public String entity() {
			return entity;
		}
		
	};

	public Card (long pGameId, SUITS pSuit, int pValue) {
		id = new Date().getTime();
		gameId = pGameId;
		suit = pSuit;
		value = pValue;
	}

	public Long getId() {
		return this.id;
	}
	
	public Long getGameId() {
		return gameId;
	}
	
	public SUITS getSuit() {
		return suit;
	}

	public Integer getValue() {
		return value;
	}
	
	public String getDisplayValue() {
		return displayValues[value -1] + " of " + suit.suitname();
	}
	
	public String getFace() {
		return suit.symbol() + displayValues[value -1];
	}
	
	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (!(o instanceof Card))
			return false;
		Card card = (Card) o;
		return Objects.equals(this.id, card.getId()) 
				&& Objects.equals(this.suit, card.getSuit())
				&& Objects.equals(this.value, card.getValue());
	}
	
	@Override
	public int compareTo(Card pCard) {
		int result = 0;
		
		if(!this.equals(pCard)) {
			result = this.suit.compareTo(pCard.getSuit());
			if (result == 0) {
				result = this.value.compareTo(pCard.getValue());
			}
		}
		
		return result;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.suit, this.value);
	}
	
	@Override
	public String toString() {
		return "Card{" + "id=" + this.id + ", value='" + this.getFace() + '\'' + '}';
	}
	
}
