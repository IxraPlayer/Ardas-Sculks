package sculk.of.ixra.entity.model;

import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;

import sculk.of.ixra.entity.SculkCreakingEntity;

import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;

public class SculkCreakingModel extends GeoModel<SculkCreakingEntity> {
	@Override
	public ResourceLocation getAnimationResource(SculkCreakingEntity entity) {
		return ResourceLocation.parse("sculks_of_arda:animations/sculkcreaking.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(SculkCreakingEntity entity) {
		return ResourceLocation.parse("sculks_of_arda:geo/sculkcreaking.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(SculkCreakingEntity entity) {
		return ResourceLocation.parse("sculks_of_arda:textures/entities/" + entity.getTexture() + ".png");
	}

	@Override
	public void setCustomAnimations(SculkCreakingEntity animatable, long instanceId, AnimationState animationState) {
		GeoBone head = getAnimationProcessor().getBone("head");
		if (head != null) {
			EntityModelData entityData = (EntityModelData) animationState.getData(DataTickets.ENTITY_MODEL_DATA);
			head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
			head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
		}
	}
}
