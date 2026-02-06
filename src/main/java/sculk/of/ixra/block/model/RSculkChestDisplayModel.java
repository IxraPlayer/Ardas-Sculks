package sculk.of.ixra.block.model;

import software.bernie.geckolib.model.GeoModel;

import sculk.of.ixra.block.display.RSculkChestDisplayItem;

import net.minecraft.resources.ResourceLocation;

public class RSculkChestDisplayModel extends GeoModel<RSculkChestDisplayItem> {
	@Override
	public ResourceLocation getAnimationResource(RSculkChestDisplayItem animatable) {
		return ResourceLocation.parse("sculks_of_arda:animations/sculkchest_-_converted.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(RSculkChestDisplayItem animatable) {
		return ResourceLocation.parse("sculks_of_arda:geo/sculkchest_-_converted.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(RSculkChestDisplayItem entity) {
		return ResourceLocation.parse("sculks_of_arda:textures/block/remakesculkchest.png");
	}
}
