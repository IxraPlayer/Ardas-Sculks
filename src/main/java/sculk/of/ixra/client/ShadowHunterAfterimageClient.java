
package sculk.of.ixra.client;

import sculk.of.ixra.entity.ShadowHunterEntity;
import sculk.of.ixra.init.SculksOfArdaModEntities;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * ShadowHunter isinlandiginda arkasinda birakilan saydam "iz"leri tutar,
 * yasla(tick) ve ekrana ciz(render). Gercek bir Entity spawn ETMEZ; sadece
 * render icin kullanilan, dunyaya hic eklenmeyen bir ShadowHunterEntity
 * "kukla"si tutar ve gercek ShadowHunterRenderer'i bu kukla uzerinde
 * manuel olarak cagirir.
 */
@EventBusSubscriber(value = {Dist.CLIENT})
public class ShadowHunterAfterimageClient {

	private static final int LIFETIME_TICKS = 14;
	private static final int TRAIL_COUNT = 7;
	private static final float MAX_ALPHA = 0.4f;

	private static final class Ghost {
		final double x, y, z;
		final float yRot, xRot;
		int age;
		final ShadowHunterEntity puppet;

		Ghost(double x, double y, double z, float yRot, float xRot, String texture, int initialAge, int surface) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.yRot = yRot;
			this.xRot = xRot;
			this.age = initialAge;
			this.puppet = new ShadowHunterEntity(SculksOfArdaModEntities.SHADOW_HUNTER.get(), Minecraft.getInstance().level);
			this.puppet.setPos(x, y, z);
			this.puppet.setYRot(yRot);
			this.puppet.setXRot(xRot);
			this.puppet.yRotO = yRot;
			this.puppet.xRotO = xRot;
			this.puppet.yBodyRot = yRot;
			this.puppet.yBodyRotO = yRot;
			this.puppet.yHeadRot = yRot;
			this.puppet.yHeadRotO = yRot;
			this.puppet.setTexture(texture);
			// Yuzey (zemin/duvar/tavan) kuklaya da uygulanir; afterimage boss ne halde ise o halde gorunur
			this.puppet.setSurface(Direction.from3DDataValue(surface));
			this.puppet.snapSurfaceQuat();
		}

		float alpha(float partialTick) {
			float t = (age + partialTick) / (float) LIFETIME_TICKS;
			return Math.max(0f, 1f - t) * MAX_ALPHA;
		}
	}

	private static final List<Ghost> GHOSTS = new ArrayList<>();

	public static void spawnTrail(double fromX, double fromY, double fromZ, double toX, double toY, double toZ, float yRot, float xRot, String texture, int count, int surface) {
		if (Minecraft.getInstance().level == null)
			return;
		int safeCount = Math.max(2, count);
		for (int i = 0; i < safeCount; i++) {
			double t = i / (double) (safeCount - 1);
			double gx = fromX + (toX - fromX) * t;
			double gy = fromY + (toY - fromY) * t;
			double gz = fromZ + (toZ - fromZ) * t;
			int initialAge = (int) ((1.0 - t) * (LIFETIME_TICKS * 0.65));
			GHOSTS.add(new Ghost(gx, gy, gz, yRot, xRot, texture, initialAge, surface));
		}
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		if (GHOSTS.isEmpty())
			return;
		Iterator<Ghost> it = GHOSTS.iterator();
		while (it.hasNext()) {
			Ghost g = it.next();
			g.age++;
			if (g.age >= LIFETIME_TICKS)
				it.remove();
		}
	}

	@SubscribeEvent
	public static void onRenderLevel(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES)
			return;
		if (GHOSTS.isEmpty())
			return;

		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null)
			return;

		float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(true);
		Vec3 camPos = event.getCamera().getPosition();
		PoseStack poseStack = event.getPoseStack();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		EntityRenderer<? super ShadowHunterEntity> renderer = mc.getEntityRenderDispatcher().getRenderer(GHOSTS.get(0).puppet);

		for (Ghost g : GHOSTS) {
			float alpha = g.alpha(partialTick);
			if (alpha <= 0.01f)
				continue;

			poseStack.pushPose();
			poseStack.translate(g.x - camPos.x, g.y - camPos.y, g.z - camPos.z);

			int packedLight = LevelRenderer.getLightColor(mc.level, BlockPos.containing(g.x, g.y, g.z));

			RenderSystem.setShaderColor(1f, 1f, 1f, alpha);
			renderer.render(g.puppet, g.yRot, partialTick, poseStack, bufferSource, packedLight);
			RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

			poseStack.popPose();
		}
		bufferSource.endBatch();
	}
}
