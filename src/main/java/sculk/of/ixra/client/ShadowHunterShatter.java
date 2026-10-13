package sculk.of.ixra.client;

import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.cache.object.GeoVertex;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoBone;

import sculk.of.ixra.entity.ShadowHunterEntity;

import org.joml.Vector4f;
import org.joml.Vector3f;
import org.joml.Quaternionf;
import org.joml.Matrix4f;
import org.joml.Matrix3f;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.util.Mth;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.texture.OverlayTexture;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.WeakHashMap;
import java.util.Random;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;

public final class ShadowHunterShatter {
	private static final float DT = 1.0F / 60.0F;
	private static final int MAX_CATCHUP_STEPS = 600;
	private static final float GRAVITY = 24.0F;
	private static final float SWORD_SETTLE_TIME = 0.9F;
	private static final String SWORD_BONE = "sword";
	// Kk: kafa bone'unun altinda sarkan atki kupleri (model pikseli, Blockbench'teki Y). Bu degerin altina inen kupler
	// fizik govdesine katilmaz -> kafa dikdortgen gibi ayakta durmaz, duz yere yatar.
	private static final String HEAD_BONE = "head";
	private static final float HEAD_SCARF_BELOW_Y = 41.0F;
	// true: sarkan atki parcalari olum aninda hic cizilmez (yere gomulmesin diye). false: cizilir ama fizige katilmaz.
	private static final boolean HIDE_HANGING_SCARF = true;
	// Parcalar CORPSE_TICKS - FADE_TICKS tick (= 6 sn) tam gorunur kalir, son FADE_TICKS tick'te saydamlasip yok olur.
	private static final float FADE_TICKS = 30.0F;
	// Oyuncu itince yuvarlanma ayarlari
	private static final float ROLL_FRICTION = 2.4F;
	private static final float ROLL_STOP_SPEED = 0.3F;
	private static final float PUSH_MIN_SPEED = 0.6F;
	private static final float PUSH_MAX_SPEED = 7.0F;
	private static final int ALIGN_STEPS = 8;
	private static final int REST_DONE = ALIGN_STEPS + 1;
	private static final Map<ShadowHunterEntity, ShadowHunterShatter> STATES = new WeakHashMap<>();

	private static final class Piece {
		boolean empty;
		boolean sword;
		Matrix4f frozen;
		Vector3f center;
		Vector3f[] relative;
		Vector3f[] normals;
		Vector3f[] faceCenters;
		Vector3f[][] faceCorners;
		float[] faceAreas;
		boolean[] skipCube;
		float radius;
		// canli simulasyon durumu
		final Vector3f c = new Vector3f();
		final Vector3f prevC = new Vector3f();
		final Vector3f v = new Vector3f();
		final Vector3f w = new Vector3f();
		final Quaternionf q = new Quaternionf();
		final Quaternionf prevQ = new Quaternionf();
		final Vector3f alignAxis = new Vector3f();
		float alignAngle;
		boolean hasAlign;
		boolean pushed;
		int rest;
		float simTime;
		// kilic
		Vector3f swordMid;
		Vector3f swordTarget;
		Vector3f swordAxis;
		float swordAngle;
		float swordLength;
	}

	private final Matrix4f linear = new Matrix4f();
	private final double originX;
	private final double originY;
	private final double originZ;
	private final float startAge;
	private final float scale;
	private final Map<String, Piece> pieces = new HashMap<>();

	private ShadowHunterShatter(ShadowHunterEntity entity, Matrix4f base, float partialTick) {
		this.linear.set(base).m30(0.0F).m31(0.0F).m32(0.0F);
		this.originX = Mth.lerp(partialTick, entity.xo, entity.getX());
		this.originY = Mth.lerp(partialTick, entity.yo, entity.getY());
		this.originZ = Mth.lerp(partialTick, entity.zo, entity.getZ());
		this.startAge = entity.deathTime + partialTick;
		this.scale = Math.max(0.01F, entity.getPhaseScale());
	}

	public static void render(PoseStack poseStack, ShadowHunterEntity entity, GeoBone bone, GeoRenderer<ShadowHunterEntity> renderer, VertexConsumer buffer, float partialTick, int packedLight, int colour) {
		float age = entity.deathTime + partialTick;
		float alpha = Mth.clamp((ShadowHunterEntity.CORPSE_TICKS - age) / FADE_TICKS, 0.0F, 1.0F);
		if (alpha <= 0.0F)
			return;
		ShadowHunterShatter state = STATES.get(entity);
		if (state == null) {
			state = new ShadowHunterShatter(entity, poseStack.last().pose(), partialTick);
			STATES.put(entity, state);
		}
		Piece piece = state.pieces.get(bone.getName());
		if (piece == null) {
			piece = state.build(entity.level(), bone);
			state.pieces.put(bone.getName(), piece);
		}
		if (piece.empty)
			return;
		float tau = Math.max(0.0F, (age - state.startAge) / 20.0F);
		Matrix4f delta;
		if (piece.sword) {
			delta = state.swordDelta(piece, tau);
		} else {
			state.advance(entity.level(), piece, tau);
			delta = state.pieceDelta(piece, tau);
		}
		// Kk: yok olurken saydamlasma (alfa kanalini carp)
		int baseAlpha = colour >>> 24;
		if (baseAlpha == 0)
			baseAlpha = 255;
		colour = (Math.round(baseAlpha * alpha) << 24) | (colour & 0x00FFFFFF);
		double ex = Mth.lerp(partialTick, entity.xo, entity.getX());
		double ey = Mth.lerp(partialTick, entity.yo, entity.getY());
		double ez = Mth.lerp(partialTick, entity.zo, entity.getZ());
		Matrix4f current = poseStack.last().pose();
		Matrix4f full = new Matrix4f().translation(current.m30() + (float) (state.originX - ex), current.m31() + (float) (state.originY - ey), current.m32() + (float) (state.originZ - ez));
		full.mul(delta).mul(state.linear).mul(piece.frozen);
		poseStack.pushPose();
		PoseStack.Pose pose = poseStack.last();
		pose.pose().set(full);
		Matrix3f normal = new Matrix3f(full);
		normal.scale(1.0F / state.scale);
		pose.normal().set(normal);
		drawCubes(poseStack, piece, bone, renderer, buffer, packedLight, colour);
		poseStack.popPose();
	}

	private static void drawCubes(PoseStack poseStack, Piece piece, GeoBone bone, GeoRenderer<ShadowHunterEntity> renderer, VertexConsumer buffer, int packedLight, int colour) {
		if (piece.skipCube == null) {
			renderer.renderCubesOfBone(poseStack, bone, buffer, packedLight, OverlayTexture.NO_OVERLAY, colour);
			return;
		}
		if (bone.isHidden())
			return;
		List<GeoCube> cubes = bone.getCubes();
		for (int i = 0; i < cubes.size(); i++) {
			if (i < piece.skipCube.length && piece.skipCube[i])
				continue;
			poseStack.pushPose();
			renderer.renderCube(poseStack, cubes.get(i), buffer, packedLight, OverlayTexture.NO_OVERLAY, colour);
			poseStack.popPose();
		}
	}

	/** Simulasyonu gercek zamana (tau) yetistirir; ayni kare icinde birden fazla cagrilsa da ayni sonucu verir. */
	private void advance(Level level, Piece piece, float tau) {
		int steps = 0;
		while (piece.simTime < tau && steps < MAX_CATCHUP_STEPS) {
			piece.prevC.set(piece.c);
			piece.prevQ.set(piece.q);
			this.step(level, piece);
			piece.simTime += DT;
			steps++;
		}
		if (steps >= MAX_CATCHUP_STEPS)
			piece.simTime = tau;
	}

	private Matrix4f pieceDelta(Piece piece, float tau) {
		float t = Mth.clamp((tau - (piece.simTime - DT)) / DT, 0.0F, 1.0F);
		Vector3f c = new Vector3f(piece.prevC).lerp(piece.c, t);
		Quaternionf q = new Quaternionf(piece.prevQ).slerp(piece.q, t);
		return new Matrix4f().translate(c).rotate(q).translate(-piece.center.x, -piece.center.y, -piece.center.z);
	}

	private Matrix4f swordDelta(Piece piece, float tau) {
		float u = Mth.clamp(tau / SWORD_SETTLE_TIME, 0.0F, 1.0F);
		u = u * u * (3.0F - 2.0F * u);
		Quaternionf ru = new Quaternionf();
		if (piece.swordAngle * u > 1.0E-6F)
			ru.rotationAxis(piece.swordAngle * u, piece.swordAxis.x, piece.swordAxis.y, piece.swordAxis.z);
		Vector3f ctr = new Vector3f(piece.swordMid).lerp(piece.swordTarget, u);
		ctr.y += 4.0F * u * (1.0F - u) * 1.6F;
		Matrix4f m = new Matrix4f().translate(ctr).rotate(ru).translate(-piece.swordMid.x, -piece.swordMid.y, -piece.swordMid.z);
		if (tau > SWORD_SETTLE_TIME) {
			float dt = tau - SWORD_SETTLE_TIME;
			float wob = 0.09F * (float) Math.exp(-6.0F * dt) * (float) Math.sin(26.0F * dt);
			Vector3f wobAxis = new Vector3f(1.0F, 0.0F, 0.3F).normalize();
			Vector3f tip = new Vector3f(piece.swordTarget.x, piece.swordTarget.y - piece.swordLength * 0.5F, piece.swordTarget.z);
			Matrix4f wm = new Matrix4f().translate(tip).rotate(new Quaternionf().rotationAxis(wob, wobAxis.x, wobAxis.y, wobAxis.z)).translate(-tip.x, -tip.y, -tip.z);
			wm.mul(m);
			m = wm;
		}
		return m;
	}

	private Piece build(Level level, GeoBone bone) {
		Piece piece = new Piece();
		PoseStack boneStack = new PoseStack();
		boneStack.translate(-bone.getPosX() / 16.0F, bone.getPosY() / 16.0F, bone.getPosZ() / 16.0F);
		boneStack.translate(bone.getPivotX() / 16.0F, bone.getPivotY() / 16.0F, bone.getPivotZ() / 16.0F);
		if (bone.getRotZ() != 0.0F)
			boneStack.mulPose(new Quaternionf().rotationXYZ(0.0F, 0.0F, bone.getRotZ()));
		if (bone.getRotY() != 0.0F)
			boneStack.mulPose(new Quaternionf().rotationXYZ(0.0F, bone.getRotY(), 0.0F));
		if (bone.getRotX() != 0.0F)
			boneStack.mulPose(new Quaternionf().rotationXYZ(bone.getRotX(), 0.0F, 0.0F));
		boneStack.scale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
		boneStack.translate(-bone.getPivotX() / 16.0F, -bone.getPivotY() / 16.0F, -bone.getPivotZ() / 16.0F);
		piece.frozen = new Matrix4f(boneStack.last().pose());

		List<Vector3f> points = new ArrayList<>();
		List<Vector3f> faceCenterList = new ArrayList<>();
		List<Vector3f> faceNormals = new ArrayList<>();
		List<Vector3f[]> faceCornerList = new ArrayList<>();
		List<GeoCube> cubes = bone.getCubes();
		boolean isHead = HEAD_BONE.equals(bone.getName());
		boolean[] skip = new boolean[cubes.size()];
		boolean anySkip = false;
		for (int ci = 0; ci < cubes.size(); ci++) {
			GeoCube cube = cubes.get(ci);
			// Kk: sarkan atki parcalari fizik govdesine girmesin (kafa dikdortgen gibi ayakta kalmasin)
			if (isHead && isHangingScarf(cube)) {
				if (HIDE_HANGING_SCARF) {
					skip[ci] = true;
					anySkip = true;
				}
				continue;
			}
			PoseStack cubeStack = new PoseStack();
			cubeStack.last().pose().set(piece.frozen);
			cubeStack.translate(cube.pivot().x / 16.0D, cube.pivot().y / 16.0D, cube.pivot().z / 16.0D);
			cubeStack.mulPose(new Quaternionf().rotationXYZ(0.0F, 0.0F, (float) cube.rotation().z));
			cubeStack.mulPose(new Quaternionf().rotationXYZ(0.0F, (float) cube.rotation().y, 0.0F));
			cubeStack.mulPose(new Quaternionf().rotationXYZ((float) cube.rotation().x, 0.0F, 0.0F));
			cubeStack.translate(-cube.pivot().x / 16.0D, -cube.pivot().y / 16.0D, -cube.pivot().z / 16.0D);
			Matrix4f cubeMatrix = cubeStack.last().pose();
			for (GeoQuad quad : cube.quads()) {
				GeoVertex[] vertices = quad.vertices();
				if (vertices.length < 3)
					continue;
				Vector3f[] quadPoints = new Vector3f[vertices.length];
				Vector3f quadCenter = new Vector3f();
				for (int i = 0; i < vertices.length; i++) {
					Vector3f position = vertices[i].position();
					Vector4f transformed = new Vector4f(position.x(), position.y(), position.z(), 1.0F);
					cubeMatrix.transform(transformed);
					Vector3f world = new Vector3f(transformed.x, transformed.y, transformed.z);
					this.linear.transformDirection(world);
					quadPoints[i] = world;
					points.add(world);
					quadCenter.add(world);
				}
				quadCenter.div(vertices.length);
				Vector3f normal = new Vector3f(quadPoints[1]).sub(quadPoints[0]).cross(new Vector3f(quadPoints[2]).sub(quadPoints[0]));
				if (normal.lengthSquared() < 1.0E-12F)
					continue;
				normal.normalize();
				faceCenterList.add(quadCenter);
				faceNormals.add(normal);
				faceCornerList.add(quadPoints);
			}
		}
		piece.skipCube = anySkip ? skip : null;
		if (points.isEmpty()) {
			piece.empty = true;
			return piece;
		}
		Vector3f center = new Vector3f();
		for (Vector3f p : points)
			center.add(p);
		center.div(points.size());
		piece.center = center;
		for (int i = 0; i < faceNormals.size(); i++) {
			Vector3f outward = new Vector3f(faceCenterList.get(i)).sub(center);
			if (faceNormals.get(i).dot(outward) < 0.0F)
				faceNormals.get(i).negate();
		}
		if (SWORD_BONE.equals(bone.getName())) {
			piece.sword = true;
			this.buildSword(level, piece, points);
			return piece;
		}
		piece.relative = new Vector3f[points.size()];
		for (int i = 0; i < piece.relative.length; i++) {
			piece.relative[i] = new Vector3f(points.get(i)).sub(center);
			piece.radius = Math.max(piece.radius, piece.relative[i].length());
		}
		int faces = faceNormals.size();
		piece.normals = faceNormals.toArray(new Vector3f[0]);
		piece.faceCenters = new Vector3f[faces];
		piece.faceCorners = new Vector3f[faces][];
		piece.faceAreas = new float[faces];
		for (int i = 0; i < faces; i++) {
			piece.faceCenters[i] = new Vector3f(faceCenterList.get(i)).sub(center);
			Vector3f[] src = faceCornerList.get(i);
			Vector3f[] dst = new Vector3f[src.length];
			Vector3f sum = new Vector3f();
			for (int k = 0; k < src.length; k++)
				dst[k] = new Vector3f(src[k]).sub(center);
			for (int k = 0; k < dst.length; k++)
				sum.add(new Vector3f(dst[k]).cross(dst[(k + 1) % dst.length]));
			piece.faceCorners[i] = dst;
			piece.faceAreas[i] = 0.5F * sum.length();
		}
		this.initMotion(piece, new Random(4242L + bone.getName().hashCode()));
		return piece;
	}

	private static boolean isHangingScarf(GeoCube cube) {
		float minY = Float.MAX_VALUE;
		for (GeoQuad quad : cube.quads())
			for (GeoVertex vertex : quad.vertices())
				minY = Math.min(minY, vertex.position().y());
		return minY * 16.0F < HEAD_SCARF_BELOW_Y;
	}

	private void buildSword(Level level, Piece piece, List<Vector3f> points) {
		int n = points.size();
		Vector3f cen = new Vector3f();
		for (Vector3f p : points)
			cen.add(p);
		cen.div(n);
		float[][] cov = new float[3][3];
		for (Vector3f p : points) {
			float[] d = {p.x - cen.x, p.y - cen.y, p.z - cen.z};
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++)
					cov[i][j] += d[i] * d[j];
		}
		Vector3f axis = new Vector3f(1.0F, 0.37F, 0.21F).normalize();
		for (int it = 0; it < 40; it++) {
			float bx = cov[0][0] * axis.x + cov[0][1] * axis.y + cov[0][2] * axis.z;
			float by = cov[1][0] * axis.x + cov[1][1] * axis.y + cov[1][2] * axis.z;
			float bz = cov[2][0] * axis.x + cov[2][1] * axis.y + cov[2][2] * axis.z;
			float len = (float) Math.sqrt(bx * bx + by * by + bz * bz);
			if (len < 1.0E-12F)
				break;
			axis.set(bx / len, by / len, bz / len);
		}
		float[] proj = new float[n];
		float[] perp = new float[n];
		for (int i = 0; i < n; i++) {
			Vector3f d = new Vector3f(points.get(i)).sub(cen);
			proj[i] = d.dot(axis);
			perp[i] = new Vector3f(d).sub(new Vector3f(axis).mul(proj[i])).length();
		}
		float[] sorted = proj.clone();
		Arrays.sort(sorted);
		float p25 = sorted[(int) ((n - 1) * 0.25F)];
		float p75 = sorted[(int) ((n - 1) * 0.75F)];
		float loSum = 0.0F;
		float hiSum = 0.0F;
		int loCount = 0;
		int hiCount = 0;
		for (int i = 0; i < n; i++) {
			if (proj[i] < p25) {
				loSum += perp[i];
				loCount++;
			}
			if (proj[i] > p75) {
				hiSum += perp[i];
				hiCount++;
			}
		}
		if (loCount > 0 && hiCount > 0 && hiSum / hiCount > loSum / loCount) {
			axis.negate();
			for (int i = 0; i < n; i++)
				proj[i] = -proj[i];
		}
		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;
		for (float v : proj) {
			min = Math.min(min, v);
			max = Math.max(max, v);
		}
		float length = Math.max(0.01F, max - min);
		piece.swordLength = length;
		piece.swordMid = new Vector3f(cen).add(new Vector3f(axis).mul((max + min) * 0.5F));
		Vector3f rotAxis = new Vector3f(axis).cross(0.0F, -1.0F, 0.0F);
		float angle = (float) Math.acos(Mth.clamp(-axis.y, -1.0F, 1.0F));
		if (rotAxis.lengthSquared() < 1.0E-12F) {
			rotAxis.set(1.0F, 0.0F, 0.0F);
			angle = axis.y > 0.0F ? (float) Math.PI : 0.0F;
		} else {
			rotAxis.normalize();
		}
		piece.swordAxis = rotAxis;
		piece.swordAngle = angle;
		float ground = (float) (groundAt(level, this.originX, this.originY, this.originZ) - this.originY);
		piece.swordTarget = new Vector3f(0.0F, ground + 0.2F * length, 0.0F);
	}

	private void initMotion(Piece piece, Random rng) {
		Vector3f c = piece.c.set(piece.center);
		piece.prevC.set(c);
		Vector3f dir = new Vector3f(c.x, 0.0F, c.z);
		float dn = dir.length();
		if (dn > 1.0E-3F) {
			dir.div(dn);
		} else {
			float a = rng.nextFloat() * 6.2831855F;
			dir.set((float) Math.cos(a), 0.0F, (float) Math.sin(a));
		}
		float speed = 2.0F + rng.nextFloat() * 3.5F;
		piece.v.set(dir.x * speed + (rng.nextFloat() * 2.0F - 1.0F), 3.5F + rng.nextFloat() * 4.5F, dir.z * speed + (rng.nextFloat() * 2.0F - 1.0F));
		Vector3f w = piece.w.set((float) rng.nextGaussian(), (float) rng.nextGaussian(), (float) rng.nextGaussian());
		if (w.lengthSquared() < 1.0E-6F)
			w.set(0.0F, 1.0F, 0.0F);
		w.normalize().mul(4.0F + rng.nextFloat() * 10.0F);
		piece.q.identity();
		piece.prevQ.identity();
		piece.rest = 0;
		piece.pushed = false;
		piece.simTime = 0.0F;
	}

	/** Tek bir fizik adimi (1/60 sn). Ilk dusus orijinal davranisla ayni; oyuncu itince yuvarlanma modu devreye girer. */
	private void step(Level level, Piece p) {
		Vector3f tmp = new Vector3f();
		if (p.rest == 0) {
			if (p.pushed)
				this.pushByPlayers(level, p, false);
			p.v.y -= GRAVITY * DT;
			float px = p.c.x;
			float pz = p.c.z;
			p.c.x += p.v.x * DT;
			p.c.y += p.v.y * DT;
			p.c.z += p.v.z * DT;
			float a = p.w.length() * DT;
			if (a > 1.0E-9F) {
				Vector3f wn = new Vector3f(p.w).normalize();
				p.q.premul(new Quaternionf().rotationAxis(a, wn.x, wn.y, wn.z));
				p.q.normalize();
			}
			if (solidAt(level, this.originX + p.c.x, this.originY + p.c.y, this.originZ + p.c.z)) {
				p.c.x = px;
				p.c.z = pz;
				p.v.x = -p.v.x * 0.4F;
				p.v.z = -p.v.z * 0.4F;
			}
			float floor = (float) (groundAt(level, this.originX + p.c.x, this.originY + p.c.y, this.originZ + p.c.z) - this.originY);
			float minY = minHeight(p.relative, p.q, tmp) + p.c.y;
			if (!p.pushed) {
				if (minY < floor) {
					p.c.y += floor - minY;
					p.v.y = p.v.y < -1.2F ? -p.v.y * 0.3F : 0.0F;
					p.v.x *= 0.6F;
					p.v.z *= 0.6F;
					p.w.mul(0.5F);
					if (Math.abs(p.v.y) < 1.0E-6F && Math.hypot(p.v.x, p.v.z) < 0.9D) {
						p.rest = 1;
						p.v.set(0.0F, 0.0F, 0.0F);
						p.w.set(0.0F, 0.0F, 0.0F);
					}
				}
			} else if (minY <= floor + 0.02F) {
				// Kk: yuvarlanma modu - surtunme yavaslatir, donus hizi ilerleme hizina baglanir (kaymadan yuvarlanma)
				if (minY < floor)
					p.c.y += floor - minY;
				boolean impact = p.v.y < -1.2F;
				p.v.y = impact ? -p.v.y * 0.3F : 0.0F;
				if (impact) {
					p.v.x *= 0.6F;
					p.v.z *= 0.6F;
				}
				float f = (float) Math.exp(-ROLL_FRICTION * DT);
				p.v.x *= f;
				p.v.z *= f;
				if (!impact) {
					if (Math.hypot(p.v.x, p.v.z) < ROLL_STOP_SPEED) {
						p.rest = 1;
						p.v.set(0.0F, 0.0F, 0.0F);
						p.w.set(0.0F, 0.0F, 0.0F);
					} else {
						float r = Math.max(0.2F, p.radius * 0.7F);
						p.w.set(p.v.z / r, 0.0F, -p.v.x / r);
					}
				}
			}
		}
		if (p.rest >= 1 && p.rest <= ALIGN_STEPS) {
			if (p.rest == 1)
				this.chooseAlign(p);
			if (p.hasAlign) {
				p.q.premul(new Quaternionf().rotationAxis(p.alignAngle / (float) ALIGN_STEPS, p.alignAxis.x, p.alignAxis.y, p.alignAxis.z));
				p.q.normalize();
			}
			float floor = (float) (groundAt(level, this.originX + p.c.x, this.originY + p.c.y, this.originZ + p.c.z) - this.originY);
			p.c.y += floor - (minHeight(p.relative, p.q, tmp) + p.c.y);
			p.rest++;
		} else if (p.rest >= REST_DONE) {
			if (this.pushByPlayers(level, p, true)) {
				p.rest = 0;
				p.pushed = true;
			}
		}
	}

	/** Yuruyen oyuncu parcaya carparsa parca yuruyus yonunde kayip yuvarlanir. true = parca uyandi/itildi. */
	private boolean pushByPlayers(Level level, Piece p, boolean wake) {
		double wx = this.originX + p.c.x;
		double wy = this.originY + p.c.y;
		double wz = this.originZ + p.c.z;
		float reach = Math.max(0.3F, p.radius * 0.6F);
		boolean hit = false;
		for (Player player : level.players()) {
			if (player.isSpectator())
				continue;
			double vx = (player.getX() - player.xo) * 20.0D;
			double vz = (player.getZ() - player.zo) * 20.0D;
			double speed = Math.sqrt(vx * vx + vz * vz);
			if (speed < PUSH_MIN_SPEED)
				continue;
			AABB box = player.getBoundingBox();
			double dx = wx - Mth.clamp(wx, box.minX, box.maxX);
			double dy = wy - Mth.clamp(wy, box.minY, box.maxY);
			double dz = wz - Mth.clamp(wz, box.minZ, box.maxZ);
			if (dx * dx + dy * dy + dz * dz > (double) (reach * reach))
				continue;
			float dirX = (float) (vx / speed);
			float dirZ = (float) (vz / speed);
			if ((wx - player.getX()) * dirX + (wz - player.getZ()) * dirZ < -0.2D)
				continue;
			float target = (float) Math.min(PUSH_MAX_SPEED, speed * 1.1D + 0.4D);
			float along = p.v.x * dirX + p.v.z * dirZ;
			if (along < target) {
				float add = (target - along) * (wake ? 1.0F : 0.5F);
				p.v.x += dirX * add;
				p.v.z += dirZ * add;
			}
			if (wake)
				p.v.y = Math.max(p.v.y, 1.5F);
			hit = true;
		}
		return hit;
	}

	/** Durma yuzunu secer: govdenin gercekten dayanabilecegi (en dis duzlem + agirlik merkezi icinde) en asagi bakan yuz. */
	private void chooseAlign(Piece p) {
		p.hasAlign = false;
		int faces = p.normals.length;
		if (faces == 0)
			return;
		int n = p.relative.length;
		Vector3f[] rp = new Vector3f[n];
		for (int j = 0; j < n; j++)
			rp[j] = p.q.transform(new Vector3f(p.relative[j]));
		Vector3f lowest = null;
		float lowestY = Float.MAX_VALUE;
		Vector3f best = null;
		float bestY = 0.0F;
		float bestArea = 0.0F;
		for (int i = 0; i < faces; i++) {
			Vector3f nw = p.q.transform(new Vector3f(p.normals[i]));
			if (nw.y < lowestY) {
				lowestY = nw.y;
				lowest = nw;
			}
			if (nw.y > -0.05F)
				continue;
			Vector3f fcw = p.q.transform(new Vector3f(p.faceCenters[i]));
			float planeD = fcw.dot(nw);
			boolean extremal = true;
			for (int j = 0; j < n; j++) {
				if (rp[j].dot(nw) - planeD > 0.01F) {
					extremal = false;
					break;
				}
			}
			if (!extremal || !comInsideFace(p, i, nw, planeD))
				continue;
			float area = p.faceAreas[i];
			if (best == null || nw.y < bestY - 0.05F || (Math.abs(nw.y - bestY) <= 0.05F && area > bestArea)) {
				best = nw;
				bestY = nw.y;
				bestArea = area;
			}
		}
		Vector3f pick = best != null ? best : lowest;
		if (pick == null)
			return;
		Vector3f ax = new Vector3f(pick).cross(0.0F, -1.0F, 0.0F);
		p.alignAngle = (float) Math.acos(Mth.clamp(-pick.y, -1.0F, 1.0F));
		if (ax.lengthSquared() > 1.0E-12F) {
			p.alignAxis.set(ax).normalize();
			p.hasAlign = true;
		}
	}

	/** Agirlik merkezinin (parca merkezi) yuz duzlemine izdusumu yuzun icinde mi? */
	private static boolean comInsideFace(Piece p, int face, Vector3f nw, float planeD) {
		Vector3f proj = new Vector3f(nw).mul(planeD);
		Vector3f[] src = p.faceCorners[face];
		int m = src.length;
		Vector3f[] cw = new Vector3f[m];
		for (int k = 0; k < m; k++)
			cw[k] = p.q.transform(new Vector3f(src[k]));
		boolean pos = false;
		boolean neg = false;
		for (int k = 0; k < m; k++) {
			Vector3f e = new Vector3f(cw[(k + 1) % m]).sub(cw[k]);
			Vector3f d = new Vector3f(proj).sub(cw[k]);
			float sgn = e.cross(d).dot(nw);
			if (sgn > 1.0E-5F)
				pos = true;
			else if (sgn < -1.0E-5F)
				neg = true;
		}
		return !(pos && neg);
	}

	private static float minHeight(Vector3f[] relative, Quaternionf q, Vector3f tmp) {
		float min = Float.MAX_VALUE;
		for (Vector3f p : relative) {
			tmp.set(p);
			q.transform(tmp);
			if (tmp.y < min)
				min = tmp.y;
		}
		return min;
	}

	private static boolean solidAt(Level level, double x, double y, double z) {
		BlockPos pos = BlockPos.containing(x, y, z);
		if (!level.hasChunkAt(pos))
			return false;
		BlockState state = level.getBlockState(pos);
		return !state.getCollisionShape(level, pos).isEmpty();
	}

	private static double groundAt(Level level, double x, double y, double z) {
		int bx = Mth.floor(x);
		int bz = Mth.floor(z);
		int top = Mth.floor(y) + 1;
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		for (int by = top; by > top - 40; by--) {
			pos.set(bx, by, bz);
			if (!level.hasChunkAt(pos))
				break;
			BlockState state = level.getBlockState(pos);
			VoxelShape shape = state.getCollisionShape(level, pos);
			if (!shape.isEmpty())
				return by + shape.max(Direction.Axis.Y);
		}
		return y - 40.0D;
	}
}
