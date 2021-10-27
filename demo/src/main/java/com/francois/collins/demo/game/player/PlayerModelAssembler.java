package com.francois.collins.demo.game.player;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.francois.collins.demo.game.GameController;

@Component
public class PlayerModelAssembler implements RepresentationModelAssembler<Player, EntityModel<Player>> {
	@Override
	public EntityModel<Player> toModel(Player pPlayer) {

		return EntityModel.of(pPlayer, 
				linkTo(methodOn(GameController.class).onePlayer(pPlayer.getGameId(),pPlayer.getId())).withSelfRel());
	}
}
