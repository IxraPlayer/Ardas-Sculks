
package sculk.of.ixra.client.renderer;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import sculk.of.ixra.entity.model.SculkEndermanModel;
import sculk.of.ixra.entity.layer.SculkEndermanLayer;
import sculk.of.ixra.entity.SculkEndermanEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class SculkEndermanRenderer extends GeoEntityRenderer<SculkEndermanEntity> {
	public SculkEndermanRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new SculkEndermanModel());
		this.shadowRadius = 0.5f;
		this.addRenderLayer(new SculkEndermanLayer(this));
	}

	@Override
	public RenderType getRenderType(SculkEndermanEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, SculkEndermanEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
		float scale = 1f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
	}
}
