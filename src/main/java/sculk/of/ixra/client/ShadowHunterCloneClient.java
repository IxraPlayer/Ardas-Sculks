package sculk.of.ixra.client;

import sculk.of.ixra.entity.ShadowHunterCloneRing;
import sculk.of.ixra.entity.ShadowHunterEntity;
import sculk.of.ixra.init.SculksOfArdaModEntities;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Klon modu gorseli: CLONE_MODE acik olan her ShadowHunter icin, sunucunun senkronladigi hedefin (CLONE_TARGET:
 * oyuncu ya da yaratik) etrafinda 8 noktali halkada 7 saydam klon cizer (boss halkanin 8. noktasinda).
 * Hepsi hedefe bakar; halka boss yaklastikca daralir. Hedef yoksa ya da olduyse hicbir sey cizilmez.
 * Sadece gorsel: dunyaya varlik eklenmez, hasar/carpisma yok. Kuklalar boss id'sine gore tutulur.
 */
@EventBusSubscriber(value = {Dist.CLIENT})
public class ShadowHunterCloneClient {

	private static final float CLONE_ALPHA = 0.35f;
	private static final Map<Integer, ShadowHunterEntity> PUPPETS = new HashMap<>();

	@SubscribeEvent
	public static void onRenderLevel(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES)
			return;
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (mc.level == null || player == null || player.isSpectator()) {
			PUPPETS.clear();
			return;
		}

		float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(true);
		Vec3 camPos = event.getCamera().getPosition();
		PoseStack poseStack = event.getPoseStack();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		boolean drew = false;

		Map<Integer, Boolean> seen = new HashMap<>();
		for (Entity e : mc.level.entitiesForRendering()) {
			if (!(e instanceof ShadowHunterEntity boss) || !boss.isAlive() || !boss.isCloneMode())
				continue;
			seen.put(boss.getId(), true);

			Entity ringTarget = mc.level.getEntity(boss.getEntityData().get(ShadowHunterEntity.CLONE_TARGET));
			if (!(ringTarget instanceof LivingEntity) || !ringTarget.isAlive())
				continue;

			double bx = Mth.lerp(partialTick, boss.xo, boss.getX());
			double bz = Mth.lerp(partialTick, boss.zo, boss.getZ());
			double px = Mth.lerp(partialTick, ringTarget.xo, ringTarget.getX());
			double py = Mth.lerp(partialTick, ringTarget.yo, ringTarget.getY());
			double pz = Mth.lerp(partialTick, ringTarget.zo, ringTarget.getZ());

			double radius = ShadowHunterCloneRing.radiusFor(bx, bz, px, pz);
			double base = ShadowHunterCloneRing.angleOf(bx, bz, px, pz);

			ShadowHunterEntity puppet = PUPPETS.computeIfAbsent(boss.getId(), id -> new ShadowHunterEntity(SculksOfArdaModEntities.SHADOW_HUNTER.get(), mc.level));
			puppet.setTexture(boss.getRenderTexture());
			puppet.getEntityData().set(ShadowHunterEntity.PHASE, boss.getPhase());
			puppet.getEntityData().set(ShadowHunterEntity.PHASE_AGE, boss.getEntityData().get(ShadowHunterEntity.PHASE_AGE));
			puppet.tickCount = boss.tickCount;
			puppet.setSprinting(boss.isSprinting());
			puppet.walkAnimation.update(boss.walkAnimation.speed(partialTick), 1.0F);
			puppet.setSurface(Direction.UP);
			puppet.snapSurfaceQuat();
			EntityRenderer<? super ShadowHunterEntity> renderer = mc.getEntityRenderDispatcher().getRenderer(puppet);

			for (int k = 1; k < ShadowHunterCloneRing.POINTS; k++) {
				Vec3 p = ShadowHunterCloneRing.point(mc.level, px, py, pz, radius, base + k * ShadowHunterCloneRing.STEP);
				if (p == null)
					continue;
				float yaw = (float) (Mth.atan2(pz - p.z, px - p.x) * (180.0D / Math.PI)) - 90.0F;
				puppet.setPos(p.x, p.y, p.z);
				puppet.setYRot(yaw);
				puppet.yRotO = yaw;
				puppet.yBodyRot = yaw;
				puppet.yBodyRotO = yaw;
				puppet.yHeadRot = yaw;
				puppet.yHeadRotO = yaw;

				poseStack.pushPose();
				poseStack.translate(p.x - camPos.x, p.y - camPos.y, p.z - camPos.z);
				int packedLight = LevelRenderer.getLightColor(mc.level, BlockPos.containing(p.x, p.y, p.z));
				RenderSystem.setShaderColor(1f, 1f, 1f, CLONE_ALPHA);
				renderer.render(puppet, yaw, partialTick, poseStack, bufferSource, packedLight);
				RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
				poseStack.popPose();
				drew = true;
			}
		}
		PUPPETS.keySet().removeIf(id -> !seen.containsKey(id));
		if (drew)
			bufferSource.endBatch();
	}
}
