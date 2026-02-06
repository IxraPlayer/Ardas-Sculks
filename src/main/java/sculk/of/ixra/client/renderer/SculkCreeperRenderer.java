
package sculk.of.ixra.client.renderer;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import sculk.of.ixra.entity.model.SculkCreeperModel;
import sculk.of.ixra.entity.layer.SculkCreeperLayer;
import sculk.of.ixra.entity.SculkCreeperEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class SculkCreeperRenderer extends GeoEntityRenderer<SculkCreeperEntity> {
	public SculkCreeperRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new SculkCreeperModel());
		this.shadowRadius = 0.5f;
		this.addRenderLayer(new SculkCreeperLayer(this));
	}

	@Override
	public RenderType getRenderType(SculkCreeperEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, SculkCreeperEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
		float scale = 1f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
	}
}
