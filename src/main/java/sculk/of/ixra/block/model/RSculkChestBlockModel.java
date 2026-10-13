package sculk.of.ixra.block.model;

import software.bernie.geckolib.model.GeoModel;

import sculk.of.ixra.block.entity.RSculkChestTileEntity;

import net.minecraft.resources.ResourceLocation;

public class RSculkChestBlockModel extends GeoModel<RSculkChestTileEntity> {
	@Override
	public ResourceLocation getAnimationResource(RSculkChestTileEntity animatable) {
		return ResourceLocation.parse("sculks_of_arda:animations/sculkchest_-_converted.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(RSculkChestTileEntity animatable) {
		return ResourceLocation.parse("sculks_of_arda:geo/sculkchest_-_converted.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(RSculkChestTileEntity animatable) {
		return ResourceLocation.parse("sculks_of_arda:textures/block/remakesculkchest.png");
	}
}
