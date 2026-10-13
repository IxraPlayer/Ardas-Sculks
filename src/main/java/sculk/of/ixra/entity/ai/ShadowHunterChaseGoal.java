package sculk.of.ixra.entity.ai;

import sculk.of.ixra.entity.ShadowHunterEntity;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;

import java.util.EnumSet;

/**
 * Yercekimi yokmus gibi her yuzeyde (zemin, 4 duvar, tavan) kovalama.
 * Yuzey = boss'un "yukari" ekseni (ShadowHunterEntity.SURFACE). Zeminde vanilla yol bulma,
 * duvar/tavanda hedefe dogru yuzey duzleminde yurume + ic/dis kose gecisleri.
 */
public class ShadowHunterChaseGoal extends Goal {
	private static final int ATTACK_INTERVAL = 20;
	private static final int STUCK_LIMIT = 15;
	private static final int ATTACH_STUCK = 5;
	private static final int PATH_FALLBACK_TICKS = 40;
	private static final int MAX_SURFACE_TICKS = 1200;
	private static final double MIN_PROGRESS = 0.03D;
	private static final double HALF = 0.4D;
	private static final double ADHESION = 0.05D;
	private static final double SKIN = 0.002D;

	private record Attach(Direction n, Vec3 q) {
	}

	private final ShadowHunterEntity mob;
	private final double speed;
	private int attackCooldown;
	private int stuckTicks;
	private int pathTicks;
	private int surfaceTicks;
	private int blockedTicks;
	private int unsupportedTicks;
	private int reattachCooldown;
	private double lastX;
	private double lastZ;

	public ShadowHunterChaseGoal(ShadowHunterEntity mob, double speed) {
		this.mob = mob;
		this.speed = speed;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return isValidTarget(this.mob.getTarget());
	}

	@Override
	public boolean canContinueToUse() {
		return isValidTarget(this.mob.getTarget());
	}

	private boolean isValidTarget(LivingEntity target) {
		if (target == null || !target.isAlive())
			return false;
		if (target instanceof Player player)
			return !player.isSpectator() && !player.isCreative();
		return true;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void start() {
		this.mob.setAggressive(true);
		this.stuckTicks = 0;
		this.pathTicks = 0;
		this.lastX = this.mob.getX();
		this.lastZ = this.mob.getZ();
	}

	@Override
	public void stop() {
		this.mob.tryDetach();
		this.mob.setAggressive(false);
		this.mob.getNavigation().stop();
		this.mob.setZza(0.0F);
	}


	private static Vec3 vec(Direction d) {
		return new Vec3(d.getStepX(), d.getStepY(), d.getStepZ());
	}

	private static Vec3 withAxis(Vec3 v, Direction.Axis ax, double val) {
		return switch (ax) {
			case X -> new Vec3(val, v.y, v.z);
			case Y -> new Vec3(v.x, val, v.z);
			default -> new Vec3(v.x, v.y, val);
		};
	}

	private static Direction dir(Direction.Axis ax, double sign) {
		return Direction.fromAxisAndDirection(ax, sign >= 0.0D ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE);
	}

	private void attack(LivingEntity target) {
		if (this.attackCooldown <= 0) {
			this.attackCooldown = ATTACK_INTERVAL;
			if (this.isRunAnimationPlaying()) {
				this.mob.setAnimation("animation.ShadowHunter.attack2");
				this.mob.getPersistentData().putInt("RunSwing", 14);
			} else {
				this.mob.swing(InteractionHand.MAIN_HAND);
			}
			this.mob.doHurtTarget(target);
		}
	}

	private boolean isRunAnimationPlaying() {
		net.minecraft.nbt.CompoundTag data = this.mob.getPersistentData();
		return data.getInt("RunSwing") > 0 || this.mob.level().getGameTime() - data.getLong("RunAnimTime") <= 3L;
	}

	private boolean inReach(LivingEntity target) {
		AABB b = this.mob.getBoundingBox();
		AABB r = this.mob.isOnSurface() ? b.inflate(1.6D) : b.inflate(1.2D, 0.5D, 1.2D);
		return r.intersects(target.getBoundingBox());
	}

	/** pt noktasinin yuzey icine dogru 0.1 altindaki blok kati mi? */
	private boolean supported(Vec3 pt, Vec3 nv) {
		BlockPos pos = BlockPos.containing(pt.subtract(nv.scale(0.1D)));
		BlockState st = this.mob.level().getBlockState(pos);
		return !st.getCollisionShape(this.mob.level(), pos).isEmpty();
	}

	private void apply(Attach at) {
		this.mob.setSurface(at.n());
		this.mob.setPos(at.q().x, at.q().y, at.q().z);
		this.mob.setDeltaMovement(Vec3.ZERO);
		this.mob.setOnGround(at.n() == Direction.UP);
		this.mob.fallDistance = 0.0F;
		this.blockedTicks = 0;
		this.unsupportedTicks = 0;
		this.surfaceTicks = 0;
		if (at.n() == Direction.UP) {
			this.reattachCooldown = 20;
			this.stuckTicks = 0;
			this.pathTicks = PATH_FALLBACK_TICKS;
			this.lastX = this.mob.getX();
			this.lastZ = this.mob.getZ();
		}
	}

	private void detach() {
		if (this.mob.tryDetach())
			this.reattachCooldown = 40;
	}


	private BlockHitResult rayAt(double heightOffset, Direction d, double reach) {
		Vec3 from = new Vec3(this.mob.getX(), this.mob.getY() + heightOffset, this.mob.getZ());
		return this.mob.level().clip(new ClipContext(from, from.add(vec(d).scale(reach)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob));
	}

	/** Duvarin ayak seviyesinde (blok gercekten var) ayaklarin oturacagi noktayi verir; yoksa null. */
	private Attach wallAttachFrom(BlockHitResult hit) {
		if (hit.getType() == HitResult.Type.MISS || !hit.getDirection().getAxis().isHorizontal())
			return null;
		Direction n = hit.getDirection();
		Vec3 base = new Vec3(this.mob.getX(), this.mob.getY() + HALF + SKIN, this.mob.getZ());
		Vec3 q = withAxis(base, n.getAxis(), hit.getLocation().get(n.getAxis())).add(vec(n).scale(SKIN));
		if (!this.supported(q, vec(n)))
			return null;
		if (!this.mob.level().noCollision(this.mob, this.mob.boxFor(n, q)))
			return null;
		return new Attach(n, q);
	}

	/** Hedef yonunde basamakla cikilamayacak kadar yuksek, ayak seviyesinden basliyan duvar. */
	private Attach findGroundWall(double dtx, double dtz) {
		Direction d = Math.abs(dtx) > Math.abs(dtz) ? dir(Direction.Axis.X, dtx) : dir(Direction.Axis.Z, dtz);
		BlockHitResult low = this.rayAt(0.5D, d, 1.4D);
		BlockHitResult high = this.rayAt(2.5D, d, 1.4D);
		if (low.getType() == HitResult.Type.MISS || high.getType() == HitResult.Type.MISS)
			return null;
		if (low.getDirection() != high.getDirection())
			return null;
		return this.wallAttachFrom(low);
	}

	/** Alcak gecit: dik boss sigmiyor. Yan duvara yatip (0.8 blok yuksekliginde) sürünerek gecer. */
	private Attach findSqueeze(double dtx, double dtz) {
		double len = Math.sqrt(dtx * dtx + dtz * dtz);
		if (len < 1.0E-3D)
			return null;
		double fx = dtx / len;
		double fz = dtz / len;
		Attach best = null;
		double bestScore = 9.0D;
		for (Direction w : Direction.Plane.HORIZONTAL) {
			Attach at = this.wallAttachFrom(this.rayAt(0.5D, w, 1.2D));
			if (at == null)
				continue;
			Vec3 ahead = at.q().add(fx * 1.5D, 0.0D, fz * 1.5D);
			if (!this.mob.level().noCollision(this.mob, this.mob.boxFor(at.n(), ahead)))
				continue;
			double score = Math.abs(vec(w).x * fx + vec(w).z * fz);
			if (score < bestScore) {
				bestScore = score;
				best = at;
			}
		}
		return best;
	}

	/** Yuzeyde havada kalindiysa: dik dur, yoksa en yakin yuzeye yapis, o da olmazsa zemin moduna zorla. */
	private void recover(Vec3 p, Vec3 nv) {
		if (this.mob.tryDetach()) {
			this.reattachCooldown = 40;
			return;
		}
		Vec3 c = p.add(nv.scale(HALF));
		for (Direction d : Direction.values()) {
			BlockHitResult hit = this.mob.level().clip(new ClipContext(c, c.add(vec(d).scale(1.5D)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob));
			if (hit.getType() == HitResult.Type.MISS)
				continue;
			Direction n2 = hit.getDirection();
			Vec3 q = hit.getLocation().add(vec(n2).scale(SKIN));
			if (this.mob.level().noCollision(this.mob, this.mob.boxFor(n2, q))) {
				this.apply(new Attach(n2, q));
				return;
			}
		}
		this.mob.setSurface(Direction.UP);
		this.reattachCooldown = 40;
	}

	/** Yatik sürünürken dik durmaya yer varsa (ve onunde de yer varsa) zemine in. */
	private boolean settle(Vec3 p, Vec3 nv, Vec3 tn) {
		Vec3 q = new Vec3(p.x + nv.x * 0.45D, p.y - HALF, p.z + nv.z * 0.45D);
		BlockPos below = BlockPos.containing(q.x, q.y - 0.1D, q.z);
		if (this.mob.level().getBlockState(below).getCollisionShape(this.mob.level(), below).isEmpty())
			return false;
		double hl = Math.sqrt(tn.x * tn.x + tn.z * tn.z);
		Vec3 fwd = hl > 1.0E-3D ? new Vec3(tn.x / hl, 0.0D, tn.z / hl) : Vec3.ZERO;
		if (!this.mob.level().noCollision(this.mob, this.mob.boxFor(Direction.UP, q)))
			return false;
		if (!this.mob.level().noCollision(this.mob, this.mob.boxFor(Direction.UP, q.add(fwd.scale(1.5D)))))
			return false;
		this.apply(new Attach(Direction.UP, q));
		return true;
	}


	private double moveSpeed() {
		return Math.max(0.3D, Math.min(this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED) * 1.25D, this.mob.isHyperSonic() ? 2.2D : 0.9D));
	}

	private void faceHoriz(Vec3 d) {
		Vec3 h = new Vec3(d.x, 0.0D, d.z);
		if (h.lengthSqr() > 1.0E-4D)
			this.mob.faceAlong(h.normalize());
	}

	/** Boss'un bulundugu su sutununun yuzey yuksekligi; altinda/icinde su yoksa NaN. */
	private double waterSurface() {
		net.minecraft.world.level.Level lv = this.mob.level();
		BlockPos pos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.05D, this.mob.getZ());
		int down = 0;
		while (!lv.getFluidState(pos).is(FluidTags.WATER) && down < 3) {
			pos = pos.below();
			down++;
		}
		if (!lv.getFluidState(pos).is(FluidTags.WATER))
			return Double.NaN;
		int up = 0;
		while (lv.getFluidState(pos.above()).is(FluidTags.WATER) && up < 64) {
			pos = pos.above();
			up++;
		}
		return pos.getY() + lv.getFluidState(pos).getHeight(lv, pos);
	}

	/** Hedef suyun altinda: suda hic yavaslamadan dogrudan hedefe yuz (3 boyutlu). */
	private void swim(LivingEntity target) {
		if (this.mob.isShieldFrozen()) {
			this.faceHoriz(new Vec3(target.getX() - this.mob.getX(), 0.0D, target.getZ() - this.mob.getZ()));
			this.mob.setDeltaMovement(Vec3.ZERO);
			this.mob.fallDistance = 0.0F;
			return;
		}
		Vec3 aim = new Vec3(target.getX(), target.getY() + target.getBbHeight() * 0.5D, target.getZ());
		Vec3 from = this.mob.position().add(0.0D, this.mob.getBbHeight() * 0.5D, 0.0D);
		Vec3 d = aim.subtract(from);
		double len = d.length();
		this.faceHoriz(d);
		this.mob.setDeltaMovement(len > 0.3D ? d.scale(Math.min(this.moveSpeed(), len) / len) : Vec3.ZERO);
		this.mob.fallDistance = 0.0F;
	}

	/** Hedef karada: suyun ustunde (yuzeyin 0.2 ustunde) tam hizla yuru. hold = yerinde dur (saldirirken). */
	private void walkOnWater(LivingEntity target, double surf, boolean hold) {
		hold = hold || this.mob.isShieldFrozen();
		double hoverY = surf + 0.2D;
		double dx = target.getX() - this.mob.getX();
		double dz = target.getZ() - this.mob.getZ();
		double len = Math.sqrt(dx * dx + dz * dz);
		double sp = (hold || len < 0.05D) ? 0.0D : Math.min(this.moveSpeed(), len);
		double vx = len > 1.0E-4D ? dx / len * sp : 0.0D;
		double vz = len > 1.0E-4D ? dz / len * sp : 0.0D;
		double y = this.mob.getY();
		double vy = this.mob.getDeltaMovement().y;
		if (y <= hoverY + 0.02D) {
			vy = Mth.clamp(hoverY - y, -0.2D, 0.6D);
			if (this.mob.horizontalCollision && !hold)
				vy = Math.max(vy, 0.55D);
		}
		this.mob.setDeltaMovement(vx, vy, vz);
		this.mob.fallDistance = 0.0F;
		if (len > 0.05D)
			this.faceHoriz(new Vec3(dx, 0.0D, dz));
	}

	private void tickGround(LivingEntity target) {
		boolean subm = target.isUnderWater();
		boolean swimMode = subm && this.mob.isInWater();
		double surf = swimMode ? Double.NaN : this.waterSurface();
		boolean walkMode = !swimMode && !subm && !Double.isNaN(surf) && this.mob.getY() < surf + 1.0D;

		if (inReach(target)) {
			this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
			this.mob.getNavigation().stop();
			this.mob.setZza(0.0F);
			this.stuckTicks = 0;
			this.pathTicks = 0;
			if (swimMode)
				this.mob.setDeltaMovement(Vec3.ZERO);
			else if (walkMode)
				this.walkOnWater(target, surf, true);
			this.attack(target);
			return;
		}
		this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

		double mx = this.mob.getX() - this.lastX;
		double mz = this.mob.getZ() - this.lastZ;
		this.lastX = this.mob.getX();
		this.lastZ = this.mob.getZ();
		if (mx * mx + mz * mz < MIN_PROGRESS * MIN_PROGRESS)
			this.stuckTicks++;
		else
			this.stuckTicks = 0;

		double dtx = target.getX() - this.mob.getX();
		double dtz = target.getZ() - this.mob.getZ();
		boolean wantsUp = target.getY() - this.mob.getY() > 2.5D;
		if (!swimMode && this.reattachCooldown <= 0 && !this.mob.isReattachBlocked() && (wantsUp || this.stuckTicks >= ATTACH_STUCK)) {
			Attach at = this.findGroundWall(dtx, dtz);
			if (at == null && this.stuckTicks >= ATTACH_STUCK) {
				double hl = Math.sqrt(dtx * dtx + dtz * dtz);
				if (hl > 1.0E-3D && !this.mob.level().noCollision(this.mob, this.mob.getBoundingBox().move(dtx / hl * 0.6D, 0.0D, dtz / hl * 0.6D)))
					at = this.findSqueeze(dtx, dtz);
			}
			if (at != null) {
				this.mob.getNavigation().stop();
				this.apply(at);
				return;
			}
		}

		if (swimMode) {
			this.mob.getNavigation().stop();
			this.swim(target);
			return;
		}
		if (walkMode) {
			this.mob.getNavigation().stop();
			this.walkOnWater(target, surf, false);
			return;
		}
		if (this.pathTicks > 0) {
			this.pathTicks--;
			if (this.mob.getNavigation().isDone())
				this.mob.getNavigation().moveTo(target, this.speed);
			return;
		}
		if (this.stuckTicks >= STUCK_LIMIT) {
			this.stuckTicks = 0;
			this.pathTicks = PATH_FALLBACK_TICKS;
			this.mob.getNavigation().moveTo(target, this.speed);
			return;
		}
		this.mob.getNavigation().stop();
		this.mob.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), this.speed);
	}


	/** Ic kose: onumuz kapali, karsidaki yuzeye gec. */
	private Attach findInsideCorner(Vec3 p, Vec3 nv, Direction n, Direction a, double reach) {
		Vec3 av = vec(a);
		for (double h : new double[] { HALF, 1.2D, 2.2D }) {
			Vec3 from = p.add(nv.scale(h));
			BlockHitResult hit = this.mob.level().clip(new ClipContext(from, from.add(av.scale(reach)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob));
			if (hit.getType() == HitResult.Type.MISS)
				continue;
			Direction n2 = hit.getDirection();
			if (n2.getAxis() == n.getAxis())
				continue;
			Vec3 q = hit.getLocation().add(vec(n2).scale(SKIN));
			if (this.mob.level().noCollision(this.mob, this.mob.boxFor(n2, q)))
				return new Attach(n2, q);
		}
		return null;
	}

	/** Dis kose: destek bitti, bloklarin yan/ust yuzune sar (ornegin duvarin tepesine cikip zemine gec). */
	private Attach findWrap(Vec3 p, Vec3 nv, Direction a) {
		BlockPos cell = BlockPos.containing(p.subtract(nv.scale(0.1D)));
		Direction.Axis ax = a.getAxis();
		double e = a.getAxisDirection() == Direction.AxisDirection.POSITIVE ? cell.get(ax) + 1.0D : cell.get(ax);
		Vec3 q = withAxis(p, ax, e).subtract(nv.scale(HALF)).add(vec(a).scale(SKIN));
		if (this.mob.level().noCollision(this.mob, this.mob.boxFor(a, q)))
			return new Attach(a, q);
		return null;
	}

	private void tickSurface(LivingEntity target) {
		this.surfaceTicks++;
		Direction n = this.mob.getSurface();
		Vec3 nv = vec(n);
		Vec3 p = this.mob.position();
		this.mob.fallDistance = 0.0F;
		this.mob.setZza(0.0F);
		this.mob.getNavigation().stop();

		Vec3 aim = new Vec3(target.getX(), target.getY() + target.getBbHeight() * 0.5D, target.getZ());
		Vec3 toT = aim.subtract(p.add(nv.scale(HALF)));
		double out = toT.dot(nv);
		double horiz = Math.sqrt(toT.x * toT.x + toT.z * toT.z);

		if (this.surfaceTicks > 5 && toT.y < -3.5D && (out > 1.5D || horiz < 5.0D)) {
			this.detach();
			return;
		}
		if (this.surfaceTicks > MAX_SURFACE_TICKS) {
			this.detach();
			return;
		}

		if (n.getAxis().isHorizontal() && this.surfaceTicks > 10 && this.surfaceTicks % 5 == 0 && toT.y < 2.0D) {
			Vec3 tt = toT.subtract(nv.scale(out));
			if (tt.lengthSqr() > 1.0E-6D && this.settle(p, nv, tt.normalize()))
				return;
		}

		Vec3 t = toT.subtract(nv.scale(out));
		double tl = t.length();
		Vec3 tn = tl > 1.0E-3D ? t.scale(1.0D / tl) : Vec3.ZERO;
		if (tl > 1.0E-3D)
			this.mob.faceAlong(tn);

		if (inReach(target)) {
			this.mob.setDeltaMovement(Vec3.ZERO);
			this.attack(target);
			return;
		}
		if (this.mob.isShieldFrozen()) {
			this.mob.setDeltaMovement(Vec3.ZERO);
			return;
		}
		if (tl <= 1.0E-3D) {
			this.mob.setDeltaMovement(Vec3.ZERO);
			return;
		}

		double stepLen = Math.max(0.3D, Math.min(this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED) * 1.25D, this.mob.isHyperSonic() ? 2.2D : 0.9D));

		Direction a = null;
		double best = -1.0D;
		for (Direction.Axis ax : Direction.Axis.values()) {
			if (ax == n.getAxis())
				continue;
			double c = Math.abs(tn.get(ax));
			if (c > best) {
				best = c;
				a = dir(ax, tn.get(ax));
			}
		}
		Direction.Axis bAx = null;
		for (Direction.Axis ax : Direction.Axis.values())
			if (ax != n.getAxis() && ax != a.getAxis())
				bAx = ax;
		double cb = tn.get(bAx);
		Direction b = dir(bAx, cb);
		double ca = Math.abs(tn.get(a.getAxis()));
		Vec3 av = vec(a);

		if (!this.mob.level().noCollision(this.mob, this.mob.getBoundingBox().move(av.scale(stepLen + 0.05D)))) {
			Attach at = this.findInsideCorner(p, nv, n, a, stepLen + 0.6D);
			if (at != null) {
				this.apply(at);
				return;
			}
			if (++this.blockedTicks > 60) {
				this.recover(p, nv);
				return;
			}
		} else {
			this.blockedTicks = 0;
		}

		boolean here = this.supported(p, nv);
		if (here && !this.supported(p.add(av.scale(stepLen + 0.02D)), nv)) {
			Attach w = this.findWrap(p, nv, a);
			if (w != null) {
				this.apply(w);
				return;
			}
			ca = 0.0D;
		}
		if (!here) {
			if (++this.unsupportedTicks > 8) {
				this.recover(p, nv);
				return;
			}
		} else {
			this.unsupportedTicks = 0;
		}
		double cbAbs = Math.abs(cb);
		if (cbAbs > 0.05D && !this.supported(p.add(vec(b).scale(0.4D + stepLen * cbAbs)), nv))
			cbAbs = 0.0D;

		Vec3 dirV = av.scale(ca).add(vec(b).scale(cbAbs));
		Vec3 vel = dirV.lengthSqr() > 1.0E-6D ? dirV.normalize().scale(stepLen) : Vec3.ZERO;
		this.mob.setDeltaMovement(vel.subtract(nv.scale(ADHESION)));
	}

	@Override
	public void tick() {
		LivingEntity target = this.mob.getTarget();
		if (target == null)
			return;
		if (this.mob.isTransitioning()) {
			this.mob.setDeltaMovement(Vec3.ZERO);
			this.mob.getNavigation().stop();
			this.mob.setZza(0.0F);
			this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
			return;
		}
		if (this.attackCooldown > 0)
			this.attackCooldown--;
		if (this.reattachCooldown > 0)
			this.reattachCooldown--;
		if (this.mob.isOnSurface())
			this.tickSurface(target);
		else
			this.tickGround(target);
	}
}
