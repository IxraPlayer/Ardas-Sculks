
package sculk.of.ixra.client.renderer;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import sculk.of.ixra.entity.model.SculkCreakingModel;
import sculk.of.ixra.entity.layer.SculkCreakingLayer;
import sculk.of.ixra.entity.SculkCreakingEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class SculkCreakingRenderer extends GeoEntityRenderer<SculkCreakingEntity> {
	public SculkCreakingRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new SculkCreakingModel());
		this.shadowRadius = 0.5f;
		this.addRenderLayer(new SculkCreakingLayer(this));
	}

	@Override
	public RenderType getRenderType(SculkCreakingEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, SculkCreakingEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
		float scale = 1.1f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
	}

	@Override
	protected float getDeathMaxRotation(SculkCreakingEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
