package com.francois.collins.demo.game;

public class GameNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GameNotFoundException(Long id) {
		super("Could not find game " + id);
	}
}
