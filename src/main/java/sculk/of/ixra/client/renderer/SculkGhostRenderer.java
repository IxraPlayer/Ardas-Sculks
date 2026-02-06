
package sculk.of.ixra.client.renderer;

import sculk.of.ixra.entity.SculkGhostEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class SculkGhostRenderer extends HumanoidMobRenderer<SculkGhostEntity, HumanoidModel<SculkGhostEntity>> {
	public SculkGhostRenderer(EntityRendererProvider.Context context) {
		super(context, new HumanoidModel<SculkGhostEntity>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
		this.addLayer(new HumanoidArmorLayer(this, new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
		this.addLayer(new RenderLayer<SculkGhostEntity, HumanoidModel<SculkGhostEntity>>(this) {
			final ResourceLocation LAYER_TEXTURE = ResourceLocation.parse("sculks_of_arda:textures/entities/2sculkghost.png");

			@Override
			public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, SculkGhostEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(LAYER_TEXTURE));
				this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0));
			}
		});
		this.addLayer(new RenderLayer<SculkGhostEntity, HumanoidModel<SculkGhostEntity>>(this) {
			final ResourceLocation LAYER_TEXTURE = ResourceLocation.parse("sculks_of_arda:textures/entities/sculkghostglow.png");

			@Override
			public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, SculkGhostEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(LAYER_TEXTURE));
				this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0));
			}
		});
	}

	@Override
	protected void scale(SculkGhostEntity entity, PoseStack poseStack, float f) {
		poseStack.scale(0.8f, 0.8f, 0.8f);
	}

	@Override
	public ResourceLocation getTextureLocation(SculkGhostEntity entity) {
		return ResourceLocation.parse("sculks_of_arda:textures/entities/sculkghost.png");
	}

	@Override
	protected boolean isBodyVisible(SculkGhostEntity entity) {
		return false;
	}
}
