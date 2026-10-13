package sculk.of.ixra.client;

import sculk.of.ixra.entity.ShadowHunterEntity;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.vertex.PoseStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@EventBusSubscriber(value = {Dist.CLIENT})
public class ShadowHunterRunShakeClient {

	private static final double MIN_SPEED_PER_TICK = 0.18D;
	private static final int SHAKE_TICKS = 12;
	private static final double FOOTPRINT = 0.6D;
	private static final float MAX_SHIFT_XZ = 0.035F;
	private static final float MAX_SHIFT_Y = 0.045F;
	private static final Map<BlockPos, Long> SHAKING = new HashMap<>();

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) {
			SHAKING.clear();
			return;
		}
		long now = mc.level.getGameTime();
		for (Entity e : mc.level.entitiesForRendering()) {
			if (!(e instanceof ShadowHunterEntity boss) || !boss.isAlive() || !boss.isPhase2() || boss.isCloneMode())
				continue;
			if (!boss.onGround() || boss.getSurface() != Direction.UP)
				continue;
			double dx = boss.getX() - boss.xo;
			double dz = boss.getZ() - boss.zo;
			if (Math.sqrt(dx * dx + dz * dz) < MIN_SPEED_PER_TICK)
				continue;
			int y = Mth.floor(boss.getY() - 0.2D);
			for (int x = Mth.floor(boss.getX() - FOOTPRINT); x <= Mth.floor(boss.getX() + FOOTPRINT); x++) {
				for (int z = Mth.floor(boss.getZ() - FOOTPRINT); z <= Mth.floor(boss.getZ() + FOOTPRINT); z++) {
					BlockPos pos = new BlockPos(x, y, z);
					if (!mc.level.getBlockState(pos).isAir())
						SHAKING.put(pos, now + SHAKE_TICKS);
				}
			}
		}
		Iterator<Map.Entry<BlockPos, Long>> it = SHAKING.entrySet().iterator();
		while (it.hasNext()) {
			if (it.next().getValue() < now)
				it.remove();
		}
	}

	@SubscribeEvent
	public static void onRenderLevel(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES || SHAKING.isEmpty())
			return;
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null)
			return;
		float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(true);
		Vec3 camPos = event.getCamera().getPosition();
		PoseStack poseStack = event.getPoseStack();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		long now = mc.level.getGameTime();
		float time = now + partialTick;
		boolean drew = false;
		for (Map.Entry<BlockPos, Long> entry : SHAKING.entrySet()) {
			BlockPos pos = entry.getKey();
			BlockState state = mc.level.getBlockState(pos);
			if (state.isAir() || state.getRenderShape() != RenderShape.MODEL)
				continue;
			float left = Mth.clamp((entry.getValue() - time) / SHAKE_TICKS, 0.0F, 1.0F);
			float seed = (pos.getX() * 7 + pos.getY() * 13 + pos.getZ() * 17) * 0.37F;
			float sx = Mth.sin(time * 2.9F + seed) * MAX_SHIFT_XZ * left;
			float sy = Math.abs(Mth.sin(time * 3.3F + seed * 1.7F)) * MAX_SHIFT_Y * left;
			float sz = Mth.sin(time * 3.1F + seed * 2.3F) * MAX_SHIFT_XZ * left;
			poseStack.pushPose();
			poseStack.translate(pos.getX() - camPos.x + sx, pos.getY() - camPos.y + sy, pos.getZ() - camPos.z + sz);
			poseStack.translate(0.5D, 0.5D, 0.5D);
			poseStack.scale(1.003F, 1.003F, 1.003F);
			poseStack.translate(-0.5D, -0.5D, -0.5D);
			int light = LevelRenderer.getLightColor(mc.level, pos.above());
			mc.getBlockRenderer().renderSingleBlock(state, poseStack, bufferSource, light, OverlayTexture.NO_OVERLAY);
			poseStack.popPose();
			drew = true;
		}
		if (drew)
			bufferSource.endBatch();
	}
}
