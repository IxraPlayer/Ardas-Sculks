package sculk.of.ixra.client.renderer;

import sculk.of.ixra.entity.SculkArrowEntity;
import sculk.of.ixra.client.model.Modelsculkarrow;

import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class SculkArrowRenderer extends EntityRenderer<SculkArrowEntity> {
	private static final ResourceLocation texture = ResourceLocation.parse("sculks_of_arda:textures/entities/arrow.png");
	private final Modelsculkarrow model;

	public SculkArrowRenderer(EntityRendererProvider.Context context) {
		super(context);
		model = new Modelsculkarrow(context.bakeLayer(Modelsculkarrow.LAYER_LOCATION));
	}

	@Override
	public void render(SculkArrowEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
		VertexConsumer vb = bufferIn.getBuffer(RenderType.entityCutout(this.getTextureLocation(entityIn)));
		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) - 90));
		poseStack.mulPose(Axis.ZP.rotationDegrees(90 + Mth.lerp(partialTicks, entityIn.xRotO, entityIn.getXRot())));
		model.renderToBuffer(poseStack, vb, packedLightIn, OverlayTexture.NO_OVERLAY);
		poseStack.popPose();
		super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(SculkArrowEntity entity) {
		return texture;
	}
}
