package sculk.of.ixra.procedures;

import sculk.of.ixra.entity.ShadowHunterEntity;
import sculk.of.ixra.SculksOfArdaMod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class ShadowHunterOnEntityTickProcedure {

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;

		Entity target = (entity instanceof Mob mob) ? mob.getTarget() : null;
		boolean cloneActive = entity instanceof ShadowHunterEntity cloneOwner && cloneOwner.isCloneMode();
		if (target != null && !cloneActive) {
			entity.getPersistentData().putDouble("AI", entity.getPersistentData().getDouble("AI") + 1);
		}

		double ai = entity.getPersistentData().getDouble("AI");
		double health = (entity instanceof LivingEntity livEnt) ? livEnt.getHealth() : -1;
		boolean isServer = entity instanceof LivingEntity livEnt && !livEnt.level().isClientSide();

		if (ai == 60) {
			shieldIfLowHealth(entity, health, 98, 219, 6, 200, 2, 0, 0);
			addEffect(entity, isServer, MobEffects.DIG_SPEED, 60, 1);
			addEffect(entity, isServer, MobEffects.DAMAGE_BOOST, 60, 1);
			advanceAI(entity);
		} else if (ai == 180) {
			shieldIfLowHealth(entity, health, 59, 219, 6, 200, 2, 0, 0);
			addEffect(entity, isServer, MobEffects.DIG_SPEED, 200, 1);
			advanceAI(entity);
		} else if (ai == 230) {
			addEffect(entity, isServer, MobEffects.DIG_SPEED, 200, 1);
			advanceAI(entity);
		} else if (ai == 300) {
			shieldIfLowHealth(entity, health, 98, 219, 6, 200, 2, 0, 0);
			entity.getPersistentData().putDouble("AI", 301);
			advanceAI(entity);
		} else if (ai == 360) {
			shieldIfLowHealth(entity, health, 438, 219, 6, 200, 6, 200, 1);
			advanceAI(entity);
		} else if (ai == 420) {
			runTeleportDashSequence(world, entity, isServer);
			advanceAI(entity);
		} else if (ai == 500) {
			addEffect(entity, isServer, MobEffects.DIG_SPEED, 200, 1);
			advanceAI(entity);
		} else if (ai == 540) {
			if (entity instanceof ShadowHunterEntity cloneBoss && cloneBoss.isPhase2())
				cloneBoss.startCloneMode();
			advanceAI(entity);
		} else if (ai == 580) {
			shieldIfLowHealth(entity, health, 116, 219, 6, 200, 1, 200, 1);
			entity.getPersistentData().putDouble("AI", 0);
		}

		updateMovementAnimation(world, x, y, z, entity);
	}

	private static void advanceAI(Entity entity) {
		entity.getPersistentData().putDouble("AI", entity.getPersistentData().getDouble("AI") + 1);
	}

	private static void addEffect(Entity entity, boolean isServer, Holder<MobEffect> effect, int duration, int amplifier) {
		if (isServer)
			((LivingEntity) entity).addEffect(new MobEffectInstance(effect, duration, amplifier, false, false));
	}

	/**
	 * Can esik degerinin altindaysa shield uygular. Bir super guc (kombo, klon, hiper sonik) suruyorsa shield
	 * dusmez: parametreler bekleme slotuna yazilir, guc bitince can hala esigin altindaysa uygulanir
	 * (ShadowHunterEntity.PENDING_SHIELD_TICKS sonra iptal). regenDuration 0 ise regen atlanir.
	 */
	private static void shieldIfLowHealth(Entity entity, double health, double threshold,
			int slowdownDuration, int slowdownAmp, int resistanceDuration, int resistanceAmp,
			int regenDuration, int regenAmp) {
		if (health >= threshold)
			return;
		ShadowHunterEntity.ShieldSpec spec = new ShadowHunterEntity.ShieldSpec(threshold, slowdownDuration, slowdownAmp, resistanceDuration, resistanceAmp, regenDuration, regenAmp);
		if (entity instanceof ShadowHunterEntity power && power.isPowerActive()) {
			power.queueShield(spec);
			return;
		}
		applyShield(entity, spec);
	}

	/** Shield animasyonu + slowdown/resistance (+ istege bagli regen) efektleri. */
	public static void applyShield(Entity entity, ShadowHunterEntity.ShieldSpec spec) {
		if (entity instanceof ShadowHunterEntity shadowHunter) {
			shadowHunter.setAnimation("animation.ShadowHunter.shield");
		}
		boolean isServer = entity instanceof LivingEntity livEnt && !livEnt.level().isClientSide();
		addEffect(entity, isServer, MobEffects.MOVEMENT_SLOWDOWN, spec.slowdownDuration(), spec.slowdownAmp());
		addEffect(entity, isServer, MobEffects.DAMAGE_RESISTANCE, spec.resistanceDuration(), spec.resistanceAmp());
		if (spec.regenDuration() > 0) {
			addEffect(entity, isServer, MobEffects.REGENERATION, spec.regenDuration(), spec.regenAmp());
		}
	}

	private static final int KICKS_PHASE1 = 3;
	private static final int KICK_GAP_PHASE1 = 25;
	private static final int KICK_HIT_DELAY_PHASE1 = 8;
	private static final float KICK_DAMAGE_PHASE1 = 4.0F;
	private static final int KICKS_PHASE2 = 8;
	private static final int KICK_GAP_PHASE2 = 7;
	private static final int KICK_HIT_DELAY_PHASE2 = 4;
	private static final float KICK_DAMAGE_PHASE2 = 6.0F;
	private static final double KICK_LAUNCH_Y_PHASE1 = 0.6D;
	private static final double KICK_LAUNCH_Y_PHASE2 = 0.6D;
	/** Tekme itmesinin yatay siddeti (eski push(1, y, 1) ~ 1.41). Yon kombo basina rastgele secilir. */
	private static final double KICK_LAUNCH_HORIZONTAL = 1.4D;
	/** Kick teleport noktasinin altinda en fazla bu kadar blok icinde zemin aranir (bosluga isinlanmayi onler). */
	private static final int KICK_GROUND_SCAN = 12;

	/**
	 * AI==420 fazindaki isinlan-tekmele dizisi. Faz 1'de 3, faz 2'de 8 tekme atilir.
	 * Her adimda hedefin O ANKI bakis yonune gore ONUNE isinlaniyor (faz 2'de aci rastgele).
	 * Kombo basina TEK rastgele itme yonu secilir; kombonun tum tekmeleri hedefi o yone ucurur.
	 */
	private static void runTeleportDashSequence(LevelAccessor world, Entity entity, boolean isServer) {
		if (isServer) {
			((LivingEntity) entity).removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
			((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 1, false, false));
		}

		boolean phase2 = entity instanceof ShadowHunterEntity sh && sh.isPhase2();
		int kicks = phase2 ? KICKS_PHASE2 : KICKS_PHASE1;
		int gap = phase2 ? KICK_GAP_PHASE2 : KICK_GAP_PHASE1;
		int hitDelay = phase2 ? KICK_HIT_DELAY_PHASE2 : KICK_HIT_DELAY_PHASE1;
		float damage = phase2 ? KICK_DAMAGE_PHASE2 : KICK_DAMAGE_PHASE1;

		if (entity instanceof ShadowHunterEntity power) {
			int total = (kicks - 1) * gap + hitDelay + 10;
			power.beginPower(total);
			power.beginKick(total);
		}

		final double launchAngle = ((LivingEntity) entity).getRandom().nextDouble() * Math.PI * 2.0D;

		dashAttackIfTargetAlive(world, entity, 2.5, 0.5, hitDelay, damage, launchAngle);
		for (int i = 1; i < kicks; i++)
			SculksOfArdaMod.queueServerWork(i * gap, () -> dashAttackIfTargetAlive(world, entity, 2.5, 0.5, hitDelay, damage, launchAngle));
	}

	/** Boss'un gercek hitbox'i (faz 2 olcegi dahil) v noktasinda blok icine girmiyor ve altinda zemin var mi? */
	private static boolean kickSpotOk(ShadowHunterEntity boss, Vec3 v) {
		if (!boss.level().noCollision(boss, boss.boxFor(Direction.UP, v)))
			return false;
		for (int i = 0; i < KICK_GROUND_SCAN; i++) {
			BlockPos pos = BlockPos.containing(v.x, v.y - 0.05D - i, v.z);
			if (!boss.level().getBlockState(pos).getCollisionShape(boss.level(), pos).isEmpty())
				return true;
		}
		return false;
	}

	/**
	 * Istenen noktada yer yoksa yakin yedekleri dener: bir blok yukari, sonra hedefe dogru kisaltilmis
	 * noktalar (yukari kaymali dahil). Hicbiri uygun degilse null: o tekme atlanir, kombo devam eder.
	 */
	private static Vec3 findKickSpot(ShadowHunterEntity boss, Entity target, double tx, double ty, double tz) {
		double[] shrink = { 1.0D, 0.6D, 0.3D, 0.0D };
		double[] lift = { 0.0D, 1.0D };
		for (double f : shrink) {
			for (double up : lift) {
				Vec3 v = new Vec3(target.getX() + (tx - target.getX()) * f, ty + up, target.getZ() + (tz - target.getZ()) * f);
				if (kickSpotOk(boss, v))
					return v;
			}
		}
		return null;
	}

	/**
	 * Hedefin O ANKI bakis yonune (yRot) gore ONUNE isinlanir (faz 2'de rastgele aci).
	 * kickHeight her zaman hedefin YERE GORE Y'sine eklenir (entity'nin kendi
	 * onceki Y'sine degil), bu yuzden ust uste isinlandikca birikip yukselmez -
	 * her tekme her zaman ayni sabit 0.5 blok yukseklikte gerceklesir.
	 * Isinlanma noktasi once hitbox + zemin icin dogrulanir, yuzeydeysen (duvar/tavan) once zemin moduna donulur.
	 * hitDelay tick sonra hedefe hasar/itki uygulanir; itme yonu launchAngle ile sabittir.
	 */
	private static void dashAttackIfTargetAlive(LevelAccessor world, Entity entity, double distanceBehind, double kickHeight, int hitDelay, float damage, double launchAngle) {
		if (entity instanceof ShadowHunterEntity self && (!self.isAlive() || self.isTransitioning()))
			return;
		Entity target = (entity instanceof Mob mob) ? mob.getTarget() : null;
		if (!(target instanceof LivingEntity))
			return;

		boolean phase2Kick = entity instanceof ShadowHunterEntity shPhase && shPhase.isPhase2();
		float targetYawRad = (float) Math.toRadians(target.getYRot());
		if (phase2Kick && entity instanceof LivingEntity randomSource)
			targetYawRad = randomSource.getRandom().nextFloat() * (float) (Math.PI * 2.0D);
		double frontDirX = -Math.sin(targetYawRad);
		double frontDirZ = Math.cos(targetYawRad);

		double tx = target.getX() + frontDirX * distanceBehind;
		double ty = target.getY() + kickHeight;
		double tz = target.getZ() + frontDirZ * distanceBehind;

		if (entity instanceof ShadowHunterEntity boss) {
			Vec3 spot = findKickSpot(boss, target, tx, ty, tz);
			if (spot == null)
				return;
			tx = spot.x;
			ty = spot.y;
			tz = spot.z;
		}

		if (entity instanceof ShadowHunterEntity shadowHunterForGhost && world instanceof net.minecraft.server.level.ServerLevel serverLevelForGhost) {
			net.neoforged.neoforge.network.PacketDistributor.sendToPlayersTrackingEntity(shadowHunterForGhost,
					new sculk.of.ixra.network.ShadowHunterAfterimageMessage(entity.getX(), entity.getY(), entity.getZ(), tx, ty, tz, entity.getYRot(), entity.getXRot(), shadowHunterForGhost.getRenderTexture(), 7, shadowHunterForGhost.getSurface().get3DDataValue()));
		}

		if (world instanceof net.minecraft.world.level.Level levelForSound) {
			levelForSound.playSound(null, net.minecraft.core.BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()),
					net.minecraft.sounds.SoundEvents.ENDERMAN_TELEPORT, net.minecraft.sounds.SoundSource.HOSTILE, 1.0F, 1.0F);
		}

		// Duvar/tavandaysa once zemin moduna don; yoksa yan yatmis hitbox'la havada kalir.
		if (entity instanceof ShadowHunterEntity surfaceBoss && surfaceBoss.isOnSurface())
			surfaceBoss.setSurface(Direction.UP);
		entity.teleportTo(tx, ty, tz);
		entity.setDeltaMovement(Vec3.ZERO);
		entity.fallDistance = 0.0F;
		if (entity instanceof ServerPlayer serverPlayer)
			serverPlayer.connection.teleport(tx, ty, tz, entity.getYRot(), entity.getXRot());

		double lookDx = target.getX() - tx;
		double lookDz = target.getZ() - tz;
		float facingYaw = (float) (net.minecraft.util.Mth.atan2(lookDz, lookDx) * (180D / Math.PI)) - 90F;
		entity.setYRot(facingYaw);
		entity.setYHeadRot(facingYaw);
		if (entity instanceof Mob mob)
			mob.setYBodyRot(facingYaw);

		if (entity instanceof ShadowHunterEntity shadowHunter)
			shadowHunter.setAnimation("animation.ShadowHunter.Tekme");

		final double launchX = Math.cos(launchAngle) * KICK_LAUNCH_HORIZONTAL;
		final double launchZ = Math.sin(launchAngle) * KICK_LAUNCH_HORIZONTAL;
		SculksOfArdaMod.queueServerWork(hitDelay, () -> {
			if (entity instanceof ShadowHunterEntity self && (!self.isAlive() || self.isTransitioning()))
				return;
			Entity currentTarget = (entity instanceof Mob mob) ? mob.getTarget() : null;
			if (currentTarget == null)
				return;
			currentTarget.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), damage);
			currentTarget.push(launchX, phase2Kick ? KICK_LAUNCH_Y_PHASE2 : KICK_LAUNCH_Y_PHASE1, launchZ);
			currentTarget.hurtMarked = true;
			if (currentTarget instanceof ServerPlayer hitPlayer) {
				net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(hitPlayer, new sculk.of.ixra.network.ShadowHunterKickShakeMessage(12));
			}
		});
	}

	private static void updateMovementAnimation(LevelAccessor world, double x, double y, double z, Entity entity) {
		boolean hasDarkness = entity instanceof LivingEntity livEnt && livEnt.hasEffect(MobEffects.DARKNESS);
		if (hasDarkness)
			return;

		boolean isHunting = entity instanceof Mob mob && mob.getTarget() != null;
		if (!isHunting)
			return;

		boolean onSurface = entity instanceof ShadowHunterEntity sh && sh.isOnSurface();
		if ((onSurface ? entity.getDeltaMovement().lengthSqr() : entity.getDeltaMovement().horizontalDistanceSqr()) <= 1.0E-6D)
			return;

		boolean hasSpeedEffect = entity instanceof LivingEntity livEnt && livEnt.hasEffect(MobEffects.MOVEMENT_SPEED);
		if (hasSpeedEffect && entity instanceof ShadowHunterEntity shadowHunter) {
			net.minecraft.nbt.CompoundTag data = entity.getPersistentData();
			int swingLeft = data.getInt("RunSwing");
			if (swingLeft > 0)
				data.putInt("RunSwing", swingLeft - 1);
			else {
				shadowHunter.setAnimation("animation.ShadowHunter.run");
				data.putLong("RunAnimTime", entity.level().getGameTime());
			}
		}

		boolean isServer = entity instanceof LivingEntity livEnt && !livEnt.level().isClientSide();
		int amplifier = chaseSpeedAmplifier(entity);
		addEffect(entity, isServer, MobEffects.MOVEMENT_SPEED, 10, amplifier);
		maybeSpawnRunningTrail(entity, isServer, amplifier);
	}

	private static void maybeSpawnRunningTrail(Entity entity, boolean isServer, int amplifier) {
		if (!isServer || amplifier < 6 || !(entity instanceof ShadowHunterEntity shadowHunter))
			return;
		if (!(entity.level() instanceof net.minecraft.server.level.ServerLevel))
			return;

		net.minecraft.nbt.CompoundTag data = entity.getPersistentData();
		int tickCounter = data.getInt("TrailTick") + 1;
		if (tickCounter < 4) {
			data.putInt("TrailTick", tickCounter);
			return;
		}
		data.putInt("TrailTick", 0);

		double prevX = data.contains("TrailX") ? data.getDouble("TrailX") : entity.getX();
		double prevY = data.contains("TrailY") ? data.getDouble("TrailY") : entity.getY();
		double prevZ = data.contains("TrailZ") ? data.getDouble("TrailZ") : entity.getZ();
		data.putDouble("TrailX", entity.getX());
		data.putDouble("TrailY", entity.getY());
		data.putDouble("TrailZ", entity.getZ());

		net.neoforged.neoforge.network.PacketDistributor.sendToPlayersTrackingEntity(shadowHunter,
				new sculk.of.ixra.network.ShadowHunterAfterimageMessage(prevX, prevY, prevZ, entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot(), shadowHunter.getRenderTexture(), 2, shadowHunter.getSurface().get3DDataValue()));
	}

	private static int chaseSpeedAmplifier(Entity entity) {
		Entity target = (entity instanceof Mob mob) ? mob.getTarget() : null;
		if (target == null)
			return 1;

		double distance = entity.distanceTo(target);
		if (distance > 40)
			return 9;
		if (distance > 20)
			return 3;
		return 1;
	}
}
