package sculk.of.ixra.bossheads.client;

import sculk.of.ixra.bossheads.BossWallHeadBlock;
import sculk.of.ixra.bossheads.BossHeadBlockEntity;
import sculk.of.ixra.bossheads.BossHeadBlock;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.Util;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

/** Draws the placed (floor and wall) boss heads, including their animations (ear sway, eye blink). */
public class BossHeadBlockEntityRenderer implements BlockEntityRenderer<BossHeadBlockEntity> {
	public BossHeadBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(BossHeadBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		BlockState state = blockEntity.getBlockState();
		BossHeadModel model;
		float yRot; // same meaning as the "y" of the blockstate json (clockwise, degrees)
		if (state.getBlock() instanceof BossHeadBlock floor) {
			yRot = 180f + state.getValue(BossHeadBlock.ROTATION) * 22.5f;
			model = BossHeadModel.floor(floor.getType());
		} else if (state.getBlock() instanceof BossWallHeadBlock wall) {
			Direction facing = state.getValue(BossWallHeadBlock.FACING);
			yRot = (facing.toYRot() + 180f) % 360f;
			model = BossHeadModel.wall(wall.getType());
		} else {
			return;
		}
		poseStack.pushPose();
		poseStack.translate(0.5, 0.0, 0.5);
		poseStack.mulPose(Axis.YP.rotationDegrees(-yRot));
		poseStack.translate(-0.5, 0.0, -0.5);
		model.render(poseStack, buffer, packedLight, packedOverlay, Util.getMillis(), blockEntity.getBlockPos().hashCode());
		poseStack.popPose();
	}
}
