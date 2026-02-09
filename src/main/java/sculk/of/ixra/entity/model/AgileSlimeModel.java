package sculk.of.ixra.entity.model;

import software.bernie.geckolib.model.GeoModel;

import sculk.of.ixra.entity.AgileSlimeEntity;

import net.minecraft.resources.ResourceLocation;

public class AgileSlimeModel extends GeoModel<AgileSlimeEntity> {
	@Override
	public ResourceLocation getAnimationResource(AgileSlimeEntity entity) {
		return ResourceLocation.parse("sculks_of_arda:animations/sculkslime.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(AgileSlimeEntity entity) {
		return ResourceLocation.parse("sculks_of_arda:geo/sculkslime.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(AgileSlimeEntity entity) {
		return ResourceLocation.parse("sculks_of_arda:textures/entities/" + entity.getTexture() + ".png");
	}
}
