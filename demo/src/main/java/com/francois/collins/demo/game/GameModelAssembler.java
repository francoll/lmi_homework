package com.francois.collins.demo.game;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class GameModelAssembler implements RepresentationModelAssembler<Game, EntityModel<Game>> {
	@Override
	public EntityModel<Game> toModel(Game pGame) {

		return EntityModel.of(pGame, 
				linkTo(methodOn(GameController.class).oneGame(pGame.getGameId())).withSelfRel(),
				linkTo(methodOn(GameController.class).allGames()).withRel("games"));
	}
}
