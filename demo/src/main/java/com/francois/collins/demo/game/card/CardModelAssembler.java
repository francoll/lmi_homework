package com.francois.collins.demo.game.card;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.francois.collins.demo.game.GameController;

@Component
public class CardModelAssembler implements RepresentationModelAssembler<Card, EntityModel<Card>> {
	@Override
	public EntityModel<Card> toModel(Card pCard) {

		//TODO: add link to game and play card action
		return EntityModel.of(pCard, //
				linkTo(methodOn(GameController.class).oneCard(pCard.getGameId(), pCard.getId())).withSelfRel());
	}
}
