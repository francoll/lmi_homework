package com.francois.collins.demo.game;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ShoeStatsByCardModelAssembler implements RepresentationModelAssembler<ShoeStatsByCard, EntityModel<ShoeStatsByCard>>{
	@Override
	public EntityModel<ShoeStatsByCard> toModel(ShoeStatsByCard pStats) {

		return EntityModel.of(pStats);
	}
}
