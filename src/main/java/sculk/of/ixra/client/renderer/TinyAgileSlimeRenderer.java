
package sculk.of.ixra.client.renderer;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;

import sculk.of.ixra.entity.model.TinyAgileSlimeModel;
import sculk.of.ixra.entity.TinyAgileSlimeEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.Optional;

public class TinyAgileSlimeRenderer extends GeoEntityRenderer<TinyAgileSlimeEntity> {
	public TinyAgileSlimeRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new TinyAgileSlimeModel());
		this.shadowRadius = 0.5f;
	}

	@Override
	public RenderType getRenderType(TinyAgileSlimeEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, TinyAgileSlimeEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
		float scale = 1.2f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
	}

	/**
	 * AgileSlimeRenderer'daki ile aynı sebep/çözüm: iç detay küpü (gözler/ağız) opak,
	 * dış jöle kabuğu saydam olarak iki ayrı geçişte çiziliyor. Bkz. AgileSlimeRenderer.
	 */
	@Override
	public void render(TinyAgileSlimeEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		this.animatable = entity;

		BakedGeoModel bakedModel = getGeoModel().getBakedModel(getGeoModel().getModelResource(entity));
		Optional<GeoBone> innerBone = bakedModel.getBone("inner");
		Optional<GeoBone> outerBone = bakedModel.getBone("outer");

		if (innerBone.isPresent() && outerBone.isPresent()) {
			ResourceLocation texture = getTextureLocation(entity);

			outerBone.get().setHidden(true);
			innerBone.get().setHidden(false);
			defaultRender(poseStack, entity, bufferSource, RenderType.entityCutoutNoCull(texture), null, entityYaw, partialTick, packedLight);

			innerBone.get().setHidden(true);
			outerBone.get().setHidden(false);
			defaultRender(poseStack, entity, bufferSource, RenderType.entityTranslucent(texture), null, entityYaw, partialTick, packedLight);

			innerBone.get().setHidden(false);
			outerBone.get().setHidden(false);
		} else {
			defaultRender(poseStack, entity, bufferSource, null, null, entityYaw, partialTick, packedLight);
		}

		this.animatable = null;
	}
}
