
package sculk.of.ixra.client.renderer;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import sculk.of.ixra.entity.model.SculkBoss1TModel;
import sculk.of.ixra.entity.layer.SculkBoss1TLayer;
import sculk.of.ixra.entity.SculkBoss1TEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class SculkBoss1TRenderer extends GeoEntityRenderer<SculkBoss1TEntity> {
	public SculkBoss1TRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new SculkBoss1TModel());
		this.shadowRadius = 0.5f;
		this.addRenderLayer(new SculkBoss1TLayer(this));
	}

	@Override
	public RenderType getRenderType(SculkBoss1TEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, SculkBoss1TEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
		float scale = 3.7f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
	}

	@Override
	protected float getDeathMaxRotation(SculkBoss1TEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
