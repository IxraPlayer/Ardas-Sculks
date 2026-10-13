
package sculk.of.ixra.client;

import sculk.of.ixra.entity.ShadowHunterEntity;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.Minecraft;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

/**
 * Nyxar faz 2'ye gecerken etrafinda beliren mor lazer halka.
 * Particle degil, gercek geometri: dairesel bir tup (kare kesitli) olarak cizilir.
 * Parlak cekirdek + yumusak hale katmanlari additive "lightning" render tipiyle karisir.
 */
@EventBusSubscriber({Dist.CLIENT})
public class ShadowHunterTransitionRingClient {
	private static final int SEGMENTS = 128;
	private static final double MAX_VIEW_DISTANCE_SQR = 96.0D * 96.0D;

	private static final int FADE_IN_TICKS = 4;
	private static final int FADE_OUT_TICKS = 8;
	private static final int GROW_TICKS = 6;

	// {yaricap-genislik (yari), alpha, r, g, b}: icten disa. Cekirdek once cizilir (derinlik yuzunden).
	private static final float[][] LAYERS = {
			{0.035F, 1.00F, 0.96F, 0.88F, 1.00F}, // beyazimsi cekirdek
			{0.090F, 0.55F, 0.78F, 0.40F, 1.00F}, // acik mor
			{0.200F, 0.22F, 0.55F, 0.15F, 1.00F}, // mor hale
	};

	@SubscribeEvent
	public static void onRenderLevel(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES)
			return;
		Minecraft mc = Minecraft.getInstance();
		ClientLevel level = mc.level;
		if (level == null)
			return;
		float pt = event.getPartialTick().getGameTimeDeltaPartialTick(false);
		Vec3 cam = event.getCamera().getPosition();

		MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
		boolean drew = false;
		for (Entity e : level.entitiesForRendering()) {
			if (!(e instanceof ShadowHunterEntity boss) || !boss.isAlive() || !boss.isPhase2TransitionVisual())
				continue;
			Vec3 c = boss.bodyCenter();
			if (c.distanceToSqr(cam) > MAX_VIEW_DISTANCE_SQR)
				continue;
			drawRing(event.getPoseStack(), buffers, cam, c.x, boss.getTransitionRingY(), c.z, boss.getPhase2AgeTicks() + pt);
			drew = true;
		}
		if (drew)
			buffers.endBatch(RenderType.lightning());
	}

	private static void drawRing(PoseStack poseStack, MultiBufferSource buffers, Vec3 cam, double cx, double cy, double cz, float t) {
		float fadeIn = Mth.clamp(t / FADE_IN_TICKS, 0.0F, 1.0F);
		float fadeOut = Mth.clamp((ShadowHunterEntity.PHASE2_TRANSITION_TICKS - t) / FADE_OUT_TICKS, 0.0F, 1.0F);
		float pulse = 0.85F + 0.15F * Mth.sin(t * 0.9F);
		float alphaMul = fadeIn * fadeOut * pulse;
		if (alphaMul <= 0.0F)
			return;
		// Baslangicta kucukten buyuyerek acilir (ease-out)
		float g = Mth.clamp(t / GROW_TICKS, 0.0F, 1.0F);
		g = 1.0F - (1.0F - g) * (1.0F - g);
		double radius = ShadowHunterEntity.TRANSITION_RING_RADIUS_PUBLIC * (0.55D + 0.45D * g);

		poseStack.pushPose();
		poseStack.translate(cx - cam.x, cy - cam.y, cz - cam.z);
		Matrix4f m = poseStack.last().pose();
		VertexConsumer vc = buffers.getBuffer(RenderType.lightning());

		for (float[] layer : LAYERS)
			drawTube(vc, m, radius, layer[0], layer[1] * alphaMul, layer[2], layer[3], layer[4]);

		poseStack.popPose();
	}

	/** Yatay daire boyunca kare kesitli tup: 4 yuz x SEGMENTS parca. */
	private static void drawTube(VertexConsumer vc, Matrix4f m, double radius, float half, float alpha, float r, float g, float b) {
		for (int i = 0; i < SEGMENTS; i++) {
			double a0 = (Math.PI * 2.0D) * i / SEGMENTS;
			double a1 = (Math.PI * 2.0D) * (i + 1) / SEGMENTS;
			float c0 = (float) Math.cos(a0), s0 = (float) Math.sin(a0);
			float c1 = (float) Math.cos(a1), s1 = (float) Math.sin(a1);
			// Kesit koseleri: (disa, yukari) = (+,+) (-,+) (-,-) (+,-)
			float[] ro = {half, -half, -half, half};
			float[] yo = {half, half, -half, -half};
			for (int k = 0; k < 4; k++) {
				int n = (k + 1) % 4;
				float ra = (float) radius + ro[k], rb = (float) radius + ro[n];
				vc.addVertex(m, c0 * ra, yo[k], s0 * ra).setColor(r, g, b, alpha);
				vc.addVertex(m, c0 * rb, yo[n], s0 * rb).setColor(r, g, b, alpha);
				vc.addVertex(m, c1 * rb, yo[n], s1 * rb).setColor(r, g, b, alpha);
				vc.addVertex(m, c1 * ra, yo[k], s1 * ra).setColor(r, g, b, alpha);
			}
		}
	}
}
