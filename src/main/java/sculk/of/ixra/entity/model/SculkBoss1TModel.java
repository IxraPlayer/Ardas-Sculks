package sculk.of.ixra.entity.model;

import software.bernie.geckolib.model.GeoModel;

import sculk.of.ixra.entity.SculkBoss1TEntity;

import net.minecraft.resources.ResourceLocation;

public class SculkBoss1TModel extends GeoModel<SculkBoss1TEntity> {
	@Override
	public ResourceLocation getAnimationResource(SculkBoss1TEntity entity) {
		return ResourceLocation.parse("sculks_of_arda:animations/sculkgolembossreloaded.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(SculkBoss1TEntity entity) {
		return ResourceLocation.parse("sculks_of_arda:geo/sculkgolembossreloaded.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(SculkBoss1TEntity entity) {
		return ResourceLocation.parse("sculks_of_arda:textures/entities/" + entity.getTexture() + ".png");
	}
}
