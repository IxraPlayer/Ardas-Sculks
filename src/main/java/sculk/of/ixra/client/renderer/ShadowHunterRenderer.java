
package sculk.of.ixra.client.renderer;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import sculk.of.ixra.entity.model.ShadowHunterModel;
import sculk.of.ixra.entity.layer.ShadowHunterLayer;
import sculk.of.ixra.entity.ShadowHunterEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class ShadowHunterRenderer extends GeoEntityRenderer<ShadowHunterEntity> {
	public ShadowHunterRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new ShadowHunterModel());
		this.shadowRadius = 0.8f;
		this.addRenderLayer(new ShadowHunterLayer(this));
	}

	@Override
	public RenderType getRenderType(ShadowHunterEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, ShadowHunterEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
		float scale = 1f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
	}

	@Override
	protected float getDeathMaxRotation(ShadowHunterEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
