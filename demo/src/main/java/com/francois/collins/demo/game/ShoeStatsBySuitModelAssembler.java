package com.francois.collins.demo.game;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ShoeStatsBySuitModelAssembler implements RepresentationModelAssembler<ShoeStatsBySuit, EntityModel<ShoeStatsBySuit>> {
	@Override
	public EntityModel<ShoeStatsBySuit> toModel(ShoeStatsBySuit pStats) {

		return EntityModel.of(pStats);
	}
}
