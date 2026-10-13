package sculk.of.ixra.bossheads.client;

import sculk.of.ixra.SculksOfArdaMod;
import sculk.of.ixra.bossheads.BossHeadType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.Minecraft;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.io.Reader;
import java.io.InputStreamReader;

/**
 * Draws a boss head from the same block model json that the game already has (models/block/*.json), but through code,
 * so parts of it can move:
 *  - Radioactive Warden: both ears sway very slightly;
 *  - Nyxar: the glowing eyes blink exactly like the Shadow Hunter (same glow + blink textures).
 * Used by the placed block (BossHeadBlockEntityRenderer) and by the item (BossHeadItemRenderer: hotbar, inventory, hand, head).
 */
public final class BossHeadModel {
	/** 1.0 = the very light sway. Raise it if the ears should move more. */
	private static final float EAR_STRENGTH = 1.0f;

	// ----- eye blink (same frames and timing as ShadowHunterLayer) -----
	private static final int BLINK_FRAMES = 7;
	private static final ResourceLocation EYES_OPEN = ResourceLocation.parse("sculks_of_arda:textures/entities/shadowhuntertextureglowing.png");
	private static final ResourceLocation[] EYES_BLINK = new ResourceLocation[BLINK_FRAMES];
	// phase 2 (purple) eyes
	private static final ResourceLocation EYES_OPEN_P2 = ResourceLocation.parse("sculks_of_arda:textures/entities/shadowhuntertextureglowing_phase2.png");
	private static final ResourceLocation[] EYES_BLINK_P2 = new ResourceLocation[BLINK_FRAMES];
	static {
		for (int i = 0; i < BLINK_FRAMES; i++) {
			EYES_BLINK[i] = ResourceLocation.parse("sculks_of_arda:textures/entities/shadowhuntertextureglowing_blink" + (i + 1) + ".png");
			EYES_BLINK_P2[i] = ResourceLocation.parse("sculks_of_arda:textures/entities/shadowhuntertextureglowing_phase2_blink" + (i + 1) + ".png");
		}
	}
	private static final float[] BLINK_FRAME_END_MS = {40f, 80f, 120f, 180f, 220f, 260f, 300f};
	private static final long BLINK_CYCLE_MS = 6500L;
	private static final long BLINK_EARLIEST_MS = 1500L;
	private static final long BLINK_LENGTH_MS = 300L;

	/** corner picks (0 = "from", 1 = "to") for the 4 vertices of every face, in Direction order: DOWN, UP, NORTH, SOUTH, WEST, EAST */
	private static final int[][][] CORNERS = {
			{{0, 0, 1}, {0, 0, 0}, {1, 0, 0}, {1, 0, 1}},
			{{0, 1, 0}, {0, 1, 1}, {1, 1, 1}, {1, 1, 0}},
			{{1, 1, 0}, {1, 0, 0}, {0, 0, 0}, {0, 1, 0}},
			{{0, 1, 1}, {0, 0, 1}, {1, 0, 1}, {1, 1, 1}},
			{{0, 1, 0}, {0, 0, 0}, {0, 0, 1}, {0, 1, 1}},
			{{1, 1, 1}, {1, 0, 1}, {1, 0, 0}, {1, 1, 0}}};

	private static final Map<String, BossHeadModel> CACHE = new HashMap<>();

	private static final class Element {
		float[] from = new float[3];
		float[] to = new float[3];
		float[][] uv = new float[6][];
		/** 0 = not an ear, -1 = left ear, +1 = right ear */
		int earSide;
		float pivotX, pivotY, pivotZ;
	}

	private final List<Element> elements = new ArrayList<>();
	private ResourceLocation texture;
	private final boolean ears;
	private final boolean blink;
	/** true = purple phase 2 eyes */
	private final boolean phase2;

	private BossHeadModel(boolean ears, boolean blink, boolean phase2) {
		this.ears = ears;
		this.blink = blink;
		this.phase2 = phase2;
	}

	public static BossHeadModel floor(BossHeadType type) {
		return get(type.floorId + "_a0", type);
	}

	public static BossHeadModel wall(BossHeadType type) {
		return get(type.wallId, type);
	}

	/** corner (model pixels) of the first element = the head cube / face; used to place the model when it is worn */
	public float[] faceFrom() {
		return elements.isEmpty() ? new float[]{4, 0, 4} : elements.get(0).from;
	}

	public float[] faceTo() {
		return elements.isEmpty() ? new float[]{12, 8, 12} : elements.get(0).to;
	}

	public static void clearCache() {
		CACHE.clear();
	}

	private static BossHeadModel get(String modelName, BossHeadType type) {
		BossHeadModel model = CACHE.get(modelName);
		if (model == null) {
			model = new BossHeadModel(type == BossHeadType.RADIOACTIVE_WARDEN, type == BossHeadType.NYXAR || type == BossHeadType.NYXAR_PHASE2, type == BossHeadType.NYXAR_PHASE2);
			model.load(modelName);
			CACHE.put(modelName, model);
		}
		return model;
	}

	private void load(String modelName) {
		ResourceLocation location = ResourceLocation.fromNamespaceAndPath(SculksOfArdaMod.MODID, "models/block/" + modelName + ".json");
		try (Reader reader = new InputStreamReader(Minecraft.getInstance().getResourceManager().open(location), StandardCharsets.UTF_8)) {
			JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
			texture = ResourceLocation.parse(root.getAsJsonObject("textures").get("tex").getAsString());
			// Nyxar: the normal texture has the glowing eyes painted in, so while blinking the open eyes would stay visible behind the lids.
			// Use the copy without eyes as the base layer; the eye layer below draws them (open or blinking).
			if (blink && !phase2)
				texture = ResourceLocation.parse("sculks_of_arda:block/nyxar_head_noeyes");
			for (JsonElement je : root.getAsJsonArray("elements")) {
				JsonObject o = je.getAsJsonObject();
				Element e = new Element();
				JsonArray from = o.getAsJsonArray("from");
				JsonArray to = o.getAsJsonArray("to");
				for (int i = 0; i < 3; i++) {
					e.from[i] = from.get(i).getAsFloat();
					e.to[i] = to.get(i).getAsFloat();
				}
				for (Map.Entry<String, JsonElement> face : o.getAsJsonObject("faces").entrySet()) {
					Direction d = Direction.byName(face.getKey());
					if (d == null)
						continue;
					JsonArray uv = face.getValue().getAsJsonObject().getAsJsonArray("uv");
					e.uv[d.ordinal()] = new float[]{uv.get(0).getAsFloat(), uv.get(1).getAsFloat(), uv.get(2).getAsFloat(), uv.get(3).getAsFloat()};
				}
				elements.add(e);
			}
		} catch (Exception ex) {
			SculksOfArdaMod.LOGGER.error("Could not load boss head model {}", modelName, ex);
		}
		if (ears)
			findEars();
	}

	/** ears = the flat (only north/south face) pieces that stick out left and right of the head cube (element 0) */
	private void findEars() {
		if (elements.isEmpty())
			return;
		Element head = elements.get(0);
		float headMidX = (head.from[0] + head.to[0]) / 2f;
		for (int i = 1; i < elements.size(); i++) {
			Element e = elements.get(i);
			boolean flat = e.uv[Direction.NORTH.ordinal()] != null && e.uv[Direction.SOUTH.ordinal()] != null && e.uv[Direction.UP.ordinal()] == null && e.uv[Direction.EAST.ordinal()] == null;
			if (!flat)
				continue;
			boolean right = (e.from[0] + e.to[0]) / 2f > headMidX;
			e.earSide = right ? 1 : -1;
			e.pivotX = right ? head.to[0] : head.from[0];
			e.pivotY = (head.from[1] + head.to[1]) / 2f;
			e.pivotZ = e.from[2];
		}
	}

	/** Draws the head with its feet at the origin of the current pose (block space, 0..16 pixels = 0..1). */
	public void render(PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, long millis, int seed) {
		if (elements.isEmpty() || texture == null)
			return;
		TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(texture);
		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TextureAtlas.LOCATION_BLOCKS));
		for (Element e : elements) {
			poseStack.pushPose();
			if (e.earSide != 0)
				sway(poseStack, e, millis);
			drawElement(poseStack.last(), consumer, e, sprite, light, overlay);
			poseStack.popPose();
		}
		if (blink) {
			// glowing eyes: always full bright, open or in one of the 7 blink frames
			VertexConsumer eyes = buffer.getBuffer(RenderType.eyes(eyeTexture(millis, seed, phase2)));
			for (Element e : elements)
				drawElement(poseStack.last(), eyes, e, null, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
		}
	}

	/** very small, slow, never-repeating-looking flap of one ear around the point where it is attached to the head */
	private static void sway(PoseStack poseStack, Element e, long millis) {
		double t = millis / 1000.0;
		double phase = e.earSide > 0 ? 0.0 : 2.1;
		float flap = (float) (Math.sin(t * 1.55 + phase) * 1.7 + Math.sin(t * 0.83 + phase * 1.9 + 0.6) * 1.1) * EAR_STRENGTH; // degrees, up/down
		float turn = (float) (Math.sin(t * 1.1 + phase + 1.3) * 1.2 + Math.sin(t * 2.3 + phase * 0.7) * 0.4) * EAR_STRENGTH; // degrees, forward/back
		float px = e.pivotX / 16f, py = e.pivotY / 16f, pz = e.pivotZ / 16f;
		poseStack.translate(px, py, pz);
		poseStack.mulPose(Axis.ZP.rotationDegrees(flap));
		poseStack.mulPose(Axis.YP.rotationDegrees(turn));
		poseStack.translate(-px, -py, -pz);
	}

	private static ResourceLocation eyeTexture(long millis, int seed, boolean phase2) {
		ResourceLocation open = phase2 ? EYES_OPEN_P2 : EYES_OPEN;
		ResourceLocation[] frames = phase2 ? EYES_BLINK_P2 : EYES_BLINK;
		long cycle = Math.floorDiv(millis, BLINK_CYCLE_MS);
		long pos = Math.floorMod(millis, BLINK_CYCLE_MS);
		int h = (int) (cycle * 0x9E3779B1L) ^ (seed * 0x85EBCA6B);
		h ^= h >>> 15;
		h *= 0x2C1B3C6D;
		h ^= h >>> 12;
		long start = BLINK_EARLIEST_MS + Math.floorMod(h, (int) (BLINK_CYCLE_MS - BLINK_EARLIEST_MS - BLINK_LENGTH_MS));
		float local = pos - start;
		if (local < 0f || local >= BLINK_LENGTH_MS)
			return open;
		for (int i = 0; i < BLINK_FRAMES; i++) {
			if (local < BLINK_FRAME_END_MS[i])
				return frames[i];
		}
		return open;
	}

	/** sprite == null: the texture is a standalone one, uv 0..16 -> 0..1 */
	private static void drawElement(PoseStack.Pose pose, VertexConsumer consumer, Element e, TextureAtlasSprite sprite, int light, int overlay) {
		for (Direction d : Direction.values()) {
			float[] uv = e.uv[d.ordinal()];
			if (uv == null)
				continue;
			int[][] corners = CORNERS[d.ordinal()];
			for (int k = 0; k < 4; k++) {
				float x = (corners[k][0] == 1 ? e.to[0] : e.from[0]) / 16f;
				float y = (corners[k][1] == 1 ? e.to[1] : e.from[1]) / 16f;
				float z = (corners[k][2] == 1 ? e.to[2] : e.from[2]) / 16f;
				float u = k < 2 ? uv[0] : uv[2];
				float v = (k == 0 || k == 3) ? uv[1] : uv[3];
				float uu, vv;
				if (sprite != null) {
					uu = sprite.getU0() + (sprite.getU1() - sprite.getU0()) * (u / 16f);
					vv = sprite.getV0() + (sprite.getV1() - sprite.getV0()) * (v / 16f);
				} else {
					uu = u / 16f;
					vv = v / 16f;
				}
				consumer.addVertex(pose, x, y, z).setColor(255, 255, 255, 255).setUv(uu, vv).setOverlay(overlay).setLight(light).setNormal(pose, d.getStepX(), d.getStepY(), d.getStepZ());
			}
		}
	}
}
