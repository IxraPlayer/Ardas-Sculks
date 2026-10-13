package sculk.of.ixra.bossheads.client;

import sculk.of.ixra.bossheads.BossHeadType;
import sculk.of.ixra.bossheads.BossHeadItem;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.Util;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

/**
 * Draws a boss head item with the same animated model that is used in the world (BossHeadModel):
 *
 *  - every head is centred on its own middle and scaled with BossHeadType.guiScale so it fits the slot exactly;
 *  - in the inventory / hotbar (GUI) it slowly turns around its vertical axis, like the Twilight Forest trophies;
 *  - on the player's head it is shown bigger (BossHeadType.wornScale), so it covers the head like a helmet;
 *  - in the hand / on the ground / in item frames it is shown still.
 *
 * Ear sway and eye blink run in every one of those places.
 */
public class BossHeadItemRenderer extends BlockEntityWithoutLevelRenderer {
	/** milliseconds for one full turn in the inventory */
	private static final long SPIN_MILLIS = 7000L;

	public BossHeadItemRenderer() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		if (!(stack.getItem() instanceof BossHeadItem item))
			return;
		BossHeadType type = item.getType();
		long millis = Util.getMillis();

		if (context == ItemDisplayContext.HEAD) {
			renderWorn(type, poseStack, buffer, packedLight, packedOverlay, millis);
			return;
		}

		poseStack.pushPose();
		// the item renderer has already moved us to the block corner (0,0,0): go to the middle of the cell
		poseStack.translate(0.5, 0.5, 0.5);
		float s = type.guiScale;
		if (context == ItemDisplayContext.GUI) {
			poseStack.mulPose(Axis.XP.rotationDegrees(type.guiPitch));
			float angle = (millis % SPIN_MILLIS) / (float) SPIN_MILLIS * 360.0f;
			poseStack.mulPose(Axis.YP.rotationDegrees(angle));
		}
		poseStack.scale(s, s, s);
		// move the middle of the head (not its feet) onto the rotation axis
		poseStack.translate(-0.5, -type.centerY / 16.0, -0.5);

		BossHeadModel.floor(type).render(poseStack, buffer, packedLight, packedOverlay, millis, 0);
		poseStack.popPose();
	}

	/**
	 * On the player's head: the player's own head is hidden (BossHeadHidePlayerHead), so the boss head simply takes its place:
	 * the face (first element of the model) is centred on the middle of the player's head, scaled by BossHeadType.wornScale.
	 * The item json has no "head" rotation: with 0 the model's north side (its face) looks the way the player looks.
	 */
	private static void renderWorn(BossHeadType type, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, long millis) {
		BossHeadModel model = BossHeadModel.floor(type);
		float s = type.wornScale();
		float[] from = model.faceFrom();
		float[] to = model.faceTo();
		float cx = (from[0] + to[0]) / 32f;
		float cy = (from[1] + to[1]) / 32f;
		float dz = (from[2] + to[2]) / 32f;
		poseStack.pushPose();
		poseStack.translate(0.5, 0.5, 0.5);
		poseStack.scale(s, s, s);
		poseStack.translate(-cx, -cy, -dz);
		model.render(poseStack, buffer, packedLight, packedOverlay, millis, 0);
		poseStack.popPose();
	}
}
