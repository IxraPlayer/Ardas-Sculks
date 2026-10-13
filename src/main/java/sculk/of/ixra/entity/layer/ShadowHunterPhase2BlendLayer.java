package sculk.of.ixra.entity.layer;

import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import sculk.of.ixra.entity.ShadowHunterEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class ShadowHunterPhase2BlendLayer extends GeoRenderLayer<ShadowHunterEntity> {

	public ShadowHunterPhase2BlendLayer(GeoRenderer<ShadowHunterEntity> entityRenderer) {
		super(entityRenderer);
	}

	@Override
	public void render(PoseStack poseStack, ShadowHunterEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		float progress = animatable.getPhase2Progress();
		if (progress <= 0.0F || progress >= 1.0F)
			return;
		String base = animatable.getTexture();
		if (base.endsWith(ShadowHunterEntity.PHASE2_TEXTURE_SUFFIX))
			return;
		ResourceLocation texture = ResourceLocation.parse("sculks_of_arda:textures/entities/" + base + ShadowHunterEntity.PHASE2_TEXTURE_SUFFIX + ".png");
		RenderType blendType = RenderType.entityTranslucent(texture);
		int alpha = Math.round(progress * 255.0F);
		int color = (alpha << 24) | 0x00FFFFFF;
		getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, blendType, bufferSource.getBuffer(blendType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, color);
	}
}
