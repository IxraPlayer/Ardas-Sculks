package sculk.of.ixra.entity.layer;

import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import sculk.of.ixra.entity.ShadowHunterEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class ShadowHunterLayer extends GeoRenderLayer<ShadowHunterEntity> {
	private static final ResourceLocation LAYER = ResourceLocation.parse("sculks_of_arda:textures/entities/shadowhuntertextureglowing.png");
	private static final ResourceLocation LAYER_PHASE2 = ResourceLocation.parse("sculks_of_arda:textures/entities/shadowhuntertextureglowing_phase2.png");

	// Kirpma animasyonu kareleri (GIF'teki 1. - 7. kareler). 0. ve 8. kare = normal acik goz (LAYER / LAYER_PHASE2).
	private static final int BLINK_FRAMES = 7;
	private static final ResourceLocation[] BLINK = new ResourceLocation[BLINK_FRAMES];
	private static final ResourceLocation[] BLINK_PHASE2 = new ResourceLocation[BLINK_FRAMES];
	static {
		for (int i = 0; i < BLINK_FRAMES; i++) {
			BLINK[i] = ResourceLocation.parse("sculks_of_arda:textures/entities/shadowhuntertextureglowing_blink" + (i + 1) + ".png");
			BLINK_PHASE2[i] = ResourceLocation.parse("sculks_of_arda:textures/entities/shadowhuntertextureglowing_phase2_blink" + (i + 1) + ".png");
		}
	}
	// GIF'teki kare sureleri (ms): 40, 40, 40, 60, 40, 40, 40 -> kumulatif bitis zamanlari
	private static final float[] FRAME_END_MS = {40f, 80f, 120f, 180f, 220f, 260f, 300f};
	private static final float BLINK_MS = 300f;

	// Kirpmalar arasi bekleme (tick, 20 tick = 1 sn). Bir dongu icinde rastgele bir noktada kirpar.
	private static final int BLINK_CYCLE = 140; // dongu uzunlugu
	private static final int BLINK_MIN_WAIT = 40; // dongu basindan en erken kirpma
	private static final int BLINK_LENGTH_TICKS = 6; // 300 ms

	public ShadowHunterLayer(GeoRenderer<ShadowHunterEntity> entityRenderer) {
		super(entityRenderer);
	}

	/** -1 = kirpmiyor (acik goz), 0..6 = GIF'teki 1..7. kare. Durumsuz: her boss farkli zamanda kirpar. */
	private static int blinkFrame(ShadowHunterEntity entity, float partialTick) {
		if (entity.isDeadOrDying())
			return -1;
		int id = entity.getId();
		int base = (int) (entity.level().getGameTime() & 0x3FFFFFFFL) + id * 37;
		int cycle = Math.floorDiv(base, BLINK_CYCLE);
		int pos = Math.floorMod(base, BLINK_CYCLE);
		int h = cycle * 0x9E3779B1 ^ id * 0x85EBCA6B;
		h ^= h >>> 15;
		h *= 0x2C1B3C6D;
		h ^= h >>> 12;
		int start = BLINK_MIN_WAIT + Math.floorMod(h, BLINK_CYCLE - BLINK_MIN_WAIT - BLINK_LENGTH_TICKS);
		float localMs = (pos - start + partialTick) * 50f;
		if (localMs < 0f || localMs >= BLINK_MS)
			return -1;
		for (int i = 0; i < BLINK_FRAMES; i++) {
			if (localMs < FRAME_END_MS[i])
				return i;
		}
		return -1;
	}

	@Override
	public void render(PoseStack poseStack, ShadowHunterEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
		boolean phase2 = animatable.getPhase2Progress() >= 0.5F;
		int frame = blinkFrame(animatable, partialTick);
		ResourceLocation texture = frame >= 0 ? (phase2 ? BLINK_PHASE2[frame] : BLINK[frame]) : (phase2 ? LAYER_PHASE2 : LAYER);
		RenderType glowRenderType = RenderType.eyes(texture);
		getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType, bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, -1);
	}
}
