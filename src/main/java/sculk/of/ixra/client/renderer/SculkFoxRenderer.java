
package sculk.of.ixra.client.renderer;

import sculk.of.ixra.entity.SculkFoxEntity;
import sculk.of.ixra.client.model.Modelsculkfox;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.Minecraft;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class SculkFoxRenderer extends MobRenderer<SculkFoxEntity, Modelsculkfox<SculkFoxEntity>> {
	public SculkFoxRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelsculkfox<SculkFoxEntity>(context.bakeLayer(Modelsculkfox.LAYER_LOCATION)), 0.5f);
		this.addLayer(new RenderLayer<SculkFoxEntity, Modelsculkfox<SculkFoxEntity>>(this) {
			final ResourceLocation LAYER_TEXTURE = ResourceLocation.parse("sculks_of_arda:textures/entities/snow_foxp.png");

			@Override
			public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, SculkFoxEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(LAYER_TEXTURE));
				EntityModel model = new Modelsculkfox(Minecraft.getInstance().getEntityModels().bakeLayer(Modelsculkfox.LAYER_LOCATION));
				this.getParentModel().copyPropertiesTo(model);
				model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
				model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
				model.renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 0));
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(SculkFoxEntity entity) {
		return ResourceLocation.parse("sculks_of_arda:textures/entities/snow_fox.png");
	}
}
