package com.francois.collins.demo.game.card;

public class CardNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CardNotFoundException(Long id) {
		super("Could not find card " + id);
	}
}
