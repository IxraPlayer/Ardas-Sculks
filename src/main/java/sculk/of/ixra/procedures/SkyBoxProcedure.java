package sculk.of.ixra.procedures;

import org.joml.Matrix4f;

import net.neoforged.neoforge.client.DimensionSpecialEffectsManager;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;

import java.util.function.Predicate;
import java.util.Set;

import java.lang.reflect.Field;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class SkyBoxProcedure {
	private static int ticks = 0;
	private static float partialTick = 0.0F;
	private static PoseStack poseStack = null;
	private static Matrix4f projectionMatrix = null;
	private static Runnable setupFog = null;
	private static VertexBuffer abyssBuffer = null;
	private static VertexBuffer deepSkyBuffer = null;
	private static VertexBuffer skyboxBuffer = null;
	private static VertexBuffer starBuffer = null;
	private static int amount = 0;
	private static int seed = 0;
	private static final Predicate<Object[]> PREDICATE = params -> {
		ticks = (Integer) params[1];
		partialTick = (Float) params[2];
		projectionMatrix = (Matrix4f) params[5];
		setupFog = (Runnable) params[7];
		FogRenderer.levelFogColor();
		setupFog.run();
		Minecraft minecraft = Minecraft.getInstance();
		Entity entity = minecraft.gameRenderer.getMainCamera().getEntity();
		if (entity != null) {
			ClientLevel level = minecraft.level;
			Vec3 pos = entity.getPosition(partialTick);
			poseStack = new PoseStack();
			poseStack.mulPose((Matrix4f) params[3]);
			execute(null, level.dimension());
			return true;
		}
		return false;
	};

	public static void renderAbyss(int color, boolean constant) {
		Minecraft minecraft = Minecraft.getInstance();
		boolean visible = minecraft.player.getEyePosition(partialTick).y() - minecraft.level.getLevelData().getHorizonHeight(minecraft.level) < 0.0D;
		if (visible || constant) {
			if (abyssBuffer == null) {
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.setShader(GameRenderer::getPositionShader);
				BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
				bufferBuilder.addVertex(0.0F, -16.0F, 0.0F);
				for (int i = 0; i <= 8; ++i) {
					bufferBuilder.addVertex(-512.0F * Mth.cos(i * 45.0F * Mth.DEG_TO_RAD), -16.0F, 512.0F * Mth.sin(i * 45.0F * Mth.DEG_TO_RAD));
				}
				abyssBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
				abyssBuffer.bind();
				abyssBuffer.upload(bufferBuilder.buildOrThrow());
			} else {
				abyssBuffer.bind();
			}
			poseStack.pushPose();
			poseStack.translate(0.0F, 12.0F, 0.0F);
			RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, (color >>> 24) / 255.0F);
			abyssBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, GameRenderer.getPositionShader());
			VertexBuffer.unbind();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			poseStack.popPose();
		}
	}

	public static void renderDeepSky(int color) {
		if (deepSkyBuffer == null) {
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShader(GameRenderer::getPositionShader);
			BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
			bufferBuilder.addVertex(0.0F, 16.0F, 0.0F);
			for (int i = 0; i <= 8; ++i) {
				bufferBuilder.addVertex(512.0F * Mth.cos(45.0F * i * Mth.DEG_TO_RAD), 16.0F, 512.0F * Mth.sin(45.0F * i * Mth.DEG_TO_RAD));
			}
			deepSkyBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
			deepSkyBuffer.bind();
			deepSkyBuffer.upload(bufferBuilder.buildOrThrow());
		} else {
			deepSkyBuffer.bind();
		}
		RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, (color >>> 24) / 255.0F);
		deepSkyBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, GameRenderer.getPositionShader());
		VertexBuffer.unbind();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public static void renderEndSky(float yaw, float pitch, float roll, int color, boolean constant) {
		Minecraft minecraft = Minecraft.getInstance();
		Vec3 pos = minecraft.gameRenderer.getMainCamera().getPosition();
		boolean invisible = minecraft.level.effects().isFoggyAt(Mth.floor(pos.x()), Mth.floor(pos.y())) || minecraft.gui.getBossOverlay().shouldCreateWorldFog();
		if (!invisible || constant) {
			poseStack.pushPose();
			poseStack.mulPose(com.mojang.math.Axis.YN.rotationDegrees(yaw));
			poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
			poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(roll));
			Matrix4f matrix4f = poseStack.last().pose();
			RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, (color >>> 24) / 255.0F);
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
			for (int i = 0; i < 6; ++i) {
				switch (i) {
					case 0 :
						bufferBuilder.addVertex(matrix4f, -100.0F, -100.0F, -100.0F).setUv(0.0F, 0.0F);
						bufferBuilder.addVertex(matrix4f, -100.0F, -100.0F, 100.0F).setUv(0.0F, 16.0F);
						bufferBuilder.addVertex(matrix4f, 100.0F, -100.0F, 100.0F).setUv(16.0F, 16.0F);
						bufferBuilder.addVertex(matrix4f, 100.0F, -100.0F, -100.0F).setUv(16.0F, 0.0F);
						break;
					case 1 :
						bufferBuilder.addVertex(matrix4f, -100.0F, -100.0F, 100.0F).setUv(0.0F, .0F);
						bufferBuilder.addVertex(matrix4f, -100.0F, 100.0F, 100.0F).setUv(0.0F, 16.0F);
						bufferBuilder.addVertex(matrix4f, 100.0F, 100.0F, 100.0F).setUv(16.0F, 16.0F);
						bufferBuilder.addVertex(matrix4f, 100.0F, -100.0F, 100.0F).setUv(16.0F, 0.0F);
						break;
					case 2 :
						bufferBuilder.addVertex(matrix4f, -100.0F, 100.0F, -100.0F).setUv(0.0F, 0.0F);
						bufferBuilder.addVertex(matrix4f, -100.0F, -100.0F, -100.0F).setUv(0.0F, 16.0F);
						bufferBuilder.addVertex(matrix4f, 100.0F, -100.0F, -100.0F).setUv(16.0F, 16.0F);
						bufferBuilder.addVertex(matrix4f, 100.0F, 100.0F, -100.0F).setUv(16.0F, 0.0F);
						break;
					case 3 :
						bufferBuilder.addVertex(matrix4f, -100.0F, 100.0F, 100.0F).setUv(0.0F, 0.0F);
						bufferBuilder.addVertex(matrix4f, -100.0F, 100.0F, -100.0F).setUv(0.0F, 16.0F);
						bufferBuilder.addVertex(matrix4f, 100.0F, 100.0F, -100.0F).setUv(16.0F, 16.0F);
						bufferBuilder.addVertex(matrix4f, 100.0F, 100.0F, 100.0F).setUv(16.0F, 0.0F);
						break;
					case 4 :
						bufferBuilder.addVertex(matrix4f, -100.0F, 100.0F, -100.0F).setUv(0.0F, 0.0F);
						bufferBuilder.addVertex(matrix4f, -100.0F, 100.0F, 100.0F).setUv(0.0F, 16.0F);
						bufferBuilder.addVertex(matrix4f, -100.0F, -100.0F, 100.0F).setUv(16.0F, 16.0F);
						bufferBuilder.addVertex(matrix4f, -100.0F, -100.0F, -100.0F).setUv(16.0F, 0.0F);
						break;
					case 5 :
						bufferBuilder.addVertex(matrix4f, 100.0F, -100.0F, -100.0F).setUv(0.0F, 0.0F);
						bufferBuilder.addVertex(matrix4f, 100.0F, -100.0F, 100.0F).setUv(0.0F, 16.0F);
						bufferBuilder.addVertex(matrix4f, 100.0F, 100.0F, 100.0F).setUv(16.0F, 16.0F);
						bufferBuilder.addVertex(matrix4f, 100.0F, 100.0F, -100.0F).setUv(16.0F, 0.0F);
						break;
				}
			}
			BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			poseStack.popPose();
		}
	}

	public static void renderMoon(float size, int color, boolean phase, boolean constant) {
		ClientLevel level = Minecraft.getInstance().level;
		float r = size / 2.0F;
		float u0 = 0.0F;
		float v0 = 0.0F;
		float u1 = 1.0F;
		float v1 = 1.0F;
		if (phase) {
			int i0 = level.getMoonPhase();
			int i1 = i0 & 3;
			int i2 = (i0 >> 2) & 1;
			u0 = i1 / 4.0F;
			v0 = i2 / 2.0F;
			u1 = (i1 + 1) / 4.0F;
			v1 = (i2 + 1) / 2.0F;
		}
		float alpha = (color >>> 24) / 255.0F;
		if (!constant)
			alpha *= (1.0F - level.getRainLevel(partialTick));
		poseStack.pushPose();
		poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));
		Matrix4f matrix4f = poseStack.last().pose();
		RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, alpha);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.addVertex(matrix4f, -r, -100.0F, -r).setUv(u1, v1);
		bufferBuilder.addVertex(matrix4f, -r, -100.0F, r).setUv(u0, v1);
		bufferBuilder.addVertex(matrix4f, r, -100.0F, r).setUv(u0, v0);
		bufferBuilder.addVertex(matrix4f, r, -100.0F, -r).setUv(u1, v0);
		BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
	}

	public static void renderSky(boolean deepSky, boolean sunlights, boolean sun, boolean moon, boolean stars, boolean abyss) {
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		if (deepSky) {
			Vec3 color = level.getSkyColor(minecraft.gameRenderer.getMainCamera().getPosition(), partialTick);
			RenderSystem.defaultBlendFunc();
			renderDeepSky(255 << 24 | (int) (color.x() * 255.0D) << 16 | (int) (color.y() * 255.0D) << 8 | (int) (color.z() * 255.0D));
		}
		if (sunlights) {
			float[] color = level.effects().getSunriseColor(level.getTimeOfDay(partialTick), partialTick);
			if (color != null) {
				RenderSystem.defaultBlendFunc();
				renderSunlights((int) (color[3] * 255.0F) << 24 | (int) (color[0] * 255.0F) << 16 | (int) (color[1] * 255.0F) << 8 | (int) (color[2] * 255.0F));
			}
		}
		if (sun) {
			RenderSystem.setShaderTexture(0, ResourceLocation.withDefaultNamespace("textures/environment/sun.png"));
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			renderSun(60.0F, 255 << 24 | 255 << 16 | 255 << 8 | 255, false);
		}
		if (moon) {
			RenderSystem.setShaderTexture(0, ResourceLocation.withDefaultNamespace("textures/environment/moon_phases.png"));
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			renderMoon(40.0F, 255 << 24 | 255 << 16 | 255 << 8 | 255, true, false);
		}
		if (stars) {
			int color = (int) (level.getStarBrightness(partialTick) * 255.0F);
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			renderStars(1500, 10842, 90.0F, level.getTimeOfDay(partialTick) * 360.0F, 0.0F, color << 24 | color << 16 | color << 8 | color, false);
		}
		if (abyss) {
			RenderSystem.defaultBlendFunc();
			renderAbyss(255 << 24 | 0 << 16 | 0 << 8 | 0, false);
		}
	}

	public static void renderSkybox(float yaw, float pitch, float roll, int color, boolean constant) {
		Minecraft minecraft = Minecraft.getInstance();
		Vec3 pos = minecraft.gameRenderer.getMainCamera().getPosition();
		boolean invisible = minecraft.level.effects().isFoggyAt(Mth.floor(pos.x()), Mth.floor(pos.y())) || minecraft.gui.getBossOverlay().shouldCreateWorldFog();
		if (!invisible || constant) {
			if (skyboxBuffer == null) {
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
				for (int i = 0; i < 6; ++i) {
					switch (i) {
						case 0 :
							bufferBuilder.addVertex(-0.5F, -0.5F, -0.5F).setUv(0.0F, 0.0F);
							bufferBuilder.addVertex(-0.5F, -0.5F, 0.5F).setUv(0.0F, 0.5F);
							bufferBuilder.addVertex(0.5F, -0.5F, 0.5F).setUv(1.0F / 3.0F, 0.5F);
							bufferBuilder.addVertex(0.5F, -0.5F, -0.5F).setUv(1.0F / 3.0F, 0.0F);
							break;
						case 1 :
							bufferBuilder.addVertex(-0.5F, 0.5F, 0.5F).setUv(1.0F / 3.0F, 0.0F);
							bufferBuilder.addVertex(-0.5F, 0.5F, -0.5F).setUv(1.0F / 3.0F, 0.5F);
							bufferBuilder.addVertex(0.5F, 0.5F, -0.5F).setUv(2.0F / 3.0F, 0.5F);
							bufferBuilder.addVertex(0.5F, 0.5F, 0.5F).setUv(2.0F / 3.0F, 0.0F);
							break;
						case 2 :
							bufferBuilder.addVertex(0.5F, 0.5F, 0.5F).setUv(2.0F / 3.0F, 0.0F);
							bufferBuilder.addVertex(0.5F, -0.5F, 0.5F).setUv(2.0F / 3.0F, 0.5F);
							bufferBuilder.addVertex(-0.5F, -0.5F, 0.5F).setUv(1.0F, 0.5F);
							bufferBuilder.addVertex(-0.5F, 0.5F, 0.5F).setUv(1.0F, 0.0F);
							break;
						case 3 :
							bufferBuilder.addVertex(-0.5F, 0.5F, 0.5F).setUv(0.0F, 0.5F);
							bufferBuilder.addVertex(-0.5F, -0.5F, 0.5F).setUv(0.0F, 1.0F);
							bufferBuilder.addVertex(-0.5F, -0.5F, -0.5F).setUv(1.0F / 3.0F, 1.0F);
							bufferBuilder.addVertex(-0.5F, 0.5F, -0.5F).setUv(1.0F / 3.0F, 0.5F);
							break;
						case 4 :
							bufferBuilder.addVertex(-0.5F, 0.5F, -0.5F).setUv(1.0F / 3.0F, 0.5F);
							bufferBuilder.addVertex(-0.5F, -0.5F, -0.5F).setUv(1.0F / 3.0F, 1.0F);
							bufferBuilder.addVertex(0.5F, -0.5F, -0.5F).setUv(2.0F / 3.0F, 1.0F);
							bufferBuilder.addVertex(0.5F, 0.5F, -0.5F).setUv(2.0F / 3.0F, 0.5F);
							break;
						case 5 :
							bufferBuilder.addVertex(0.5F, 0.5F, -0.5F).setUv(2.0F / 3.0F, 0.5F);
							bufferBuilder.addVertex(0.5F, -0.5F, -0.5F).setUv(2.0F / 3.0F, 1.0F);
							bufferBuilder.addVertex(0.5F, -0.5F, 0.5F).setUv(1.0F, 1.0F);
							bufferBuilder.addVertex(0.5F, 0.5F, 0.5F).setUv(1.0F, 0.5F);
							break;
					}
				}
				skyboxBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
				skyboxBuffer.bind();
				skyboxBuffer.upload(bufferBuilder.buildOrThrow());
			} else {
				skyboxBuffer.bind();
			}
			float size = minecraft.options.getEffectiveRenderDistance() << 6;
			poseStack.pushPose();
			poseStack.mulPose(com.mojang.math.Axis.YN.rotationDegrees(yaw));
			poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
			poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(roll));
			poseStack.scale(size, size, size);
			RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, (color >>> 24) / 255.0F);
			skyboxBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, GameRenderer.getPositionTexShader());
			VertexBuffer.unbind();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			poseStack.popPose();
		}
	}

	public static void renderStars(int amount, int seed, float yaw, float pitch, float roll, int color, boolean constant) {
		if (starBuffer == null || amount != SkyBoxProcedure.amount || seed != SkyBoxProcedure.seed) {
			SkyBoxProcedure.amount = amount;
			SkyBoxProcedure.seed = seed;
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShader(GameRenderer::getPositionShader);
			BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
			RandomSource randomsource = RandomSource.create((long) seed);
			for (int i = 0; i < amount; ++i) {
				float f0 = randomsource.nextFloat() * 2.0F - 1.0F;
				float f1 = randomsource.nextFloat() * 2.0F - 1.0F;
				float f2 = randomsource.nextFloat() * 2.0F - 1.0F;
				float f3 = 0.15F + 0.1F * randomsource.nextFloat();
				float f4 = f0 * f0 + f1 * f1 + f2 * f2;
				if (f4 < 1.0F && f4 > 0.01F) {
					f4 = 1.0F / Mth.sqrt(f4);
					f0 *= f4;
					f1 *= f4;
					f2 *= f4;
					float f5 = f0 * 100.0F;
					float f6 = f1 * 100.0F;
					float f7 = f2 * 100.0F;
					float f8 = (float) Math.atan2(f0, f2);
					float f9 = Mth.sin(f8);
					float f10 = Mth.cos(f8);
					float f11 = (float) Math.atan2(Mth.sqrt(f0 * f0 + f2 * f2), f1);
					float f12 = Mth.sin(f11);
					float f13 = Mth.cos(f11);
					float f14 = (float) randomsource.nextDouble() * Mth.PI * 2.0F;
					float f15 = Mth.sin(f14);
					float f16 = Mth.cos(f14);
					for (int j = 0; j < 4; ++j) {
						float f17 = ((j & 2) - 1) * f3;
						float f18 = ((j + 1 & 2) - 1) * f3;
						float f20 = f17 * f16 - f18 * f15;
						float f21 = f18 * f16 + f17 * f15;
						float f22 = -f20 * f13;
						float f23 = f22 * f9 - f21 * f10;
						float f24 = f20 * f12;
						float f25 = f21 * f9 + f22 * f10;
						bufferBuilder.addVertex(f5 + f23, f6 + f24, f7 + f25);
					}
				}
			}
			if (starBuffer != null)
				starBuffer.close();
			starBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
			starBuffer.bind();
			starBuffer.upload(bufferBuilder.buildOrThrow());
		} else {
			starBuffer.bind();
		}
		float alpha = (color >>> 24) / 255.0F;
		if (!constant)
			alpha *= (1.0F - Minecraft.getInstance().level.getRainLevel(partialTick));
		poseStack.pushPose();
		poseStack.mulPose(com.mojang.math.Axis.YN.rotationDegrees(yaw));
		poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
		poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(roll));
		FogRenderer.setupNoFog();
		RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, alpha);
		starBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, GameRenderer.getPositionShader());
		VertexBuffer.unbind();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		setupFog.run();
		poseStack.popPose();
	}

	public static void renderSun(float size, int color, boolean constant) {
		ClientLevel level = Minecraft.getInstance().level;
		float r = size / 2.0F;
		float alpha = (color >>> 24) / 255.0F;
		if (!constant)
			alpha *= (1.0F - level.getRainLevel(partialTick));
		poseStack.pushPose();
		poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));
		Matrix4f matrix4f = poseStack.last().pose();
		RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, alpha);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.addVertex(matrix4f, r, 100.0F, -r).setUv(0.0F, 0.0F);
		bufferBuilder.addVertex(matrix4f, r, 100.0F, r).setUv(1.0F, 0.0F);
		bufferBuilder.addVertex(matrix4f, -r, 100.0F, r).setUv(1.0F, 1.0F);
		bufferBuilder.addVertex(matrix4f, -r, 100.0F, -r).setUv(0.0F, 1.0F);
		BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
	}

	public static void renderSunlights(int color) {
		ClientLevel level = Minecraft.getInstance().level;
		float[] rawColor = level.effects().getSunriseColor(level.getTimeOfDay(partialTick), partialTick);
		if (rawColor != null) {
			int red = color >> 16 & 255;
			int green = color >> 8 & 255;
			int blue = color & 255;
			int alpha = (int) ((color >>> 24) * rawColor[3]);
			Matrix4f matrix4f = poseStack.last().pose();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShader(GameRenderer::getPositionColorShader);
			BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
			boolean flag = Mth.sin(level.getSunAngle(partialTick)) < 0.0F;
			if (flag) {
				bufferBuilder.addVertex(matrix4f, 100.0F, 0.0F, 0.0F).setColor(red, green, blue, alpha);
			} else {
				bufferBuilder.addVertex(matrix4f, -100.0F, 0.0F, 0.0F).setColor(red, green, blue, alpha);
			}
			for (int i = 0; i <= 16; ++i) {
				float deg = i * Mth.TWO_PI / 16.0F;
				float sin = Mth.sin(deg);
				float cos = Mth.cos(deg);
				if (flag) {
					bufferBuilder.addVertex(matrix4f, cos * 120.0F, cos * 40.0F * rawColor[3], -sin * 120.0F).setColor(red, green, blue, 0);
				} else {
					bufferBuilder.addVertex(matrix4f, -cos * 120.0F, cos * 40.0F * rawColor[3], sin * 120.0F).setColor(red, green, blue, 0);
				}
			}
			BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
		}
	}

	public static void renderTexture(float size, float yaw, float pitch, float roll, int color, boolean constant) {
		float r = size / 2.0F;
		float alpha = (color >>> 24) / 255.0F;
		if (!constant)
			alpha *= (1.0F - Minecraft.getInstance().level.getRainLevel(partialTick));
		poseStack.pushPose();
		poseStack.mulPose(com.mojang.math.Axis.YN.rotationDegrees(yaw));
		poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
		poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(roll));
		Matrix4f matrix4f = poseStack.last().pose();
		RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, alpha);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.addVertex(matrix4f, r, r, 100.0F).setUv(0.0F, 0.0F);
		bufferBuilder.addVertex(matrix4f, r, -r, 100.0F).setUv(0.0F, 1.0F);
		bufferBuilder.addVertex(matrix4f, -r, -r, 100.0F).setUv(1.0F, 1.0F);
		bufferBuilder.addVertex(matrix4f, -r, r, 100.0F).setUv(1.0F, 0.0F);
		BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
	}

	@SubscribeEvent
	public static void skySetup(FMLClientSetupEvent event) {
		try {
			Field field = DimensionSpecialEffectsManager.class.getDeclaredField("EFFECTS");
			field.setAccessible(true);
			for (DimensionSpecialEffects dimensionSpecialEffects : ((com.google.common.collect.ImmutableMap<ResourceLocation, DimensionSpecialEffects>) field.get(null)).values()) {
				Class<?> effects = dimensionSpecialEffects.getClass();
				((Set<Predicate<Object[]>>) effects.getField("CUSTOM_SKY").get(null)).add(PREDICATE);
			}
		} catch (Exception e) {
		}
	}

	public static void execute(ResourceKey<Level> dimension) {
		execute(null, dimension);
	}

	private static void execute(@Nullable Event event, ResourceKey<Level> dimension) {
		if (dimension == null)
			return;
		if (dimension == ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("sculks_of_arda:sculk_world"))) {
			RenderSystem.defaultBlendFunc();
			RenderSystem.setShaderTexture(0, ResourceLocation.parse(("sculks_of_arda" + ":textures/" + "csky.png" + ".png")));
			renderSkybox(0, 0, 0, 255 << 24 | 255 << 16 | 255 << 8 | 255, true);
		}
	}
}
