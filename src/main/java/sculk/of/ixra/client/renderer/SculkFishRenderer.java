
package sculk.of.ixra.client.renderer;

import sculk.of.ixra.entity.SculkFishEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.SalmonModel;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class SculkFishRenderer extends MobRenderer<SculkFishEntity, SalmonModel<SculkFishEntity>> {
	public SculkFishRenderer(EntityRendererProvider.Context context) {
		super(context, new SalmonModel<SculkFishEntity>(context.bakeLayer(ModelLayers.SALMON)), 0.5f);
		this.addLayer(new RenderLayer<SculkFishEntity, SalmonModel<SculkFishEntity>>(this) {
			final ResourceLocation LAYER_TEXTURE = ResourceLocation.parse("sculks_of_arda:textures/entities/sculkfishglow.png");

			@Override
			public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, SculkFishEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(LAYER_TEXTURE));
				this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0));
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(SculkFishEntity entity) {
		return ResourceLocation.parse("sculks_of_arda:textures/entities/sculkfish.png");
	}
}
