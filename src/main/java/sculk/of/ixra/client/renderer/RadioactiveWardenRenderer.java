
package sculk.of.ixra.client.renderer;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import sculk.of.ixra.entity.model.RadioactiveWardenModel;
import sculk.of.ixra.entity.layer.RadioactiveWardenLayer;
import sculk.of.ixra.entity.RadioactiveWardenEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class RadioactiveWardenRenderer extends GeoEntityRenderer<RadioactiveWardenEntity> {
	public RadioactiveWardenRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new RadioactiveWardenModel());
		this.shadowRadius = 1.1f;
		this.addRenderLayer(new RadioactiveWardenLayer(this));
	}

	@Override
	public RenderType getRenderType(RadioactiveWardenEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, RadioactiveWardenEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
		float scale = 0.8f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
	}

	@Override
	protected float getDeathMaxRotation(RadioactiveWardenEntity entityLivingBaseIn) {
		return 0.0F;
	}
}
