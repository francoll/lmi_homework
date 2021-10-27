package com.francois.collins.demo.game.player;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.francois.collins.demo.game.GameController;

@Component
public class PlayerScoreAssembler implements RepresentationModelAssembler<PlayerScore, EntityModel<PlayerScore>>  {
	@Override
	public EntityModel<PlayerScore> toModel(PlayerScore pScore) {

		return EntityModel.of(pScore, 
				linkTo(methodOn(GameController.class).onePlayer(pScore.getGameId(),pScore.getPlayerId())).withSelfRel());
	}
}
