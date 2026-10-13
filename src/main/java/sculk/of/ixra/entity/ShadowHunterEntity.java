package sculk.of.ixra.entity;

import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.GeoEntity;

import sculk.of.ixra.procedures.ShadowHunterOnEntityTickProcedure;
import sculk.of.ixra.entity.ai.ShadowHunterChaseGoal;

import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.common.NeoForgeMod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ClipContext;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class ShadowHunterEntity extends Zombie implements GeoEntity {
	public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(ShadowHunterEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Direction> SURFACE = SynchedEntityData.defineId(ShadowHunterEntity.class, EntityDataSerializers.DIRECTION);
	public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(ShadowHunterEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Integer> PHASE_AGE = SynchedEntityData.defineId(ShadowHunterEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(ShadowHunterEntity.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(ShadowHunterEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Boolean> CLONE_MODE = SynchedEntityData.defineId(ShadowHunterEntity.class, EntityDataSerializers.BOOLEAN);
	/** Klon halkasinin cizilecegi hedefin entity id'si (-1 = yok). Istemci klon gorselini bu entity'nin etrafina cizer. */
	public static final EntityDataAccessor<Integer> CLONE_TARGET = SynchedEntityData.defineId(ShadowHunterEntity.class, EntityDataSerializers.INT);
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private boolean swinging;
	private boolean lastloop;
	private long lastSwing;
	public String animationprocedure = "empty";
	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.BLUE, ServerBossEvent.BossBarOverlay.NOTCHED_20);

	public ShadowHunterEntity(EntityType<ShadowHunterEntity> type, Level world) {
		super(type, world);
		xpReward = 150;
		setNoAi(false);
		setPersistenceRequired();
		this.moveControl = new SmoothChaseMoveControl(this);
	}

	@Override
	public void setBaby(boolean baby) {
		super.setBaby(false);
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public void push(Entity entity) {
	}

	private static class SmoothChaseMoveControl extends MoveControl {
		private static final float MAX_TURN_PER_TICK = 60.0F;

		SmoothChaseMoveControl(Mob mob) {
			super(mob);
		}

		@Override
		public void tick() {
			if (this.operation == Operation.MOVE_TO) {
				this.operation = Operation.WAIT;
				double dx = this.wantedX - this.mob.getX();
				double dy = this.wantedY - this.mob.getY();
				double dz = this.wantedZ - this.mob.getZ();
				double distSqr = dx * dx + dy * dy + dz * dz;
				if (distSqr < 2.500000277905201E-7D) {
					this.mob.setZza(0.0F);
					return;
				}
				float wantedYaw = (float) (Mth.atan2(dz, dx) * (180.0F / (float) Math.PI)) - 90.0F;
				this.mob.setYRot(this.rotlerp(this.mob.getYRot(), wantedYaw, MAX_TURN_PER_TICK));
				this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
				BlockPos blockpos = this.mob.blockPosition();
				BlockState blockstate = this.mob.level().getBlockState(blockpos);
				VoxelShape voxelshape = blockstate.getCollisionShape(this.mob.level(), blockpos);
				if ((dy > (double) this.mob.maxUpStep() && dx * dx + dz * dz < (double) Math.max(1.0F, this.mob.getBbWidth()))
						|| (!voxelshape.isEmpty() && this.mob.getY() < voxelshape.max(Direction.Axis.Y) + (double) blockpos.getY()
								&& !blockstate.is(BlockTags.DOORS) && !blockstate.is(BlockTags.FENCES))) {
					this.mob.getJumpControl().jump();
					this.operation = Operation.JUMPING;
				}
			} else if (this.operation == Operation.JUMPING) {
				this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
				if (this.mob.onGround()) {
					this.operation = Operation.WAIT;
				}
			} else {
				this.mob.setZza(0.0F);
			}
		}
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(SHOOT, false);
		builder.define(SURFACE, Direction.UP);
		builder.define(ANIMATION, "undefined");
		builder.define(TEXTURE, "shadowhuntertexture");
		builder.define(PHASE, 1);
		builder.define(PHASE_AGE, 0);
		builder.define(CLONE_MODE, false);
		builder.define(CLONE_TARGET, -1);
	}


	/** Faz 2 icin can orani esigi: bu oranin ALTINA dusunce faz 2 baslar. */
	public static final float PHASE2_HEALTH_RATIO = 0.40F;

	/** Sunucu tarafi: super guc (kombo, klon modu, hiper sonik, gecis) suruyorsa kalan tick. Kaydedilmez. */
	private int powerTicks;

	public int getPhase() {
		return this.entityData.get(PHASE);
	}

	public static final float PHASE2_SCALE = 1.25F;

	public float getPhase2Progress() {
		if (!this.isPhase2())
			return 0.0F;
		return Mth.clamp(this.entityData.get(PHASE_AGE) / (float) PHASE2_TRANSITION_TICKS, 0.0F, 1.0F);
	}

	public float getPhaseScale() {
		float p = this.getPhase2Progress();
		float eased = p * p * (3.0F - 2.0F * p);
		return 1.0F + (PHASE2_SCALE - 1.0F) * eased;
	}

	public boolean isPhase2() {
		return this.getPhase() >= 2;
	}

	/** Bir super guc basladiginda cagrilir; sure dolana kadar kalkan calismaz. Daha uzun sure oncekini uzatir. */
	public void beginPower(int ticks) {
		this.powerTicks = Math.max(this.powerTicks, ticks);
	}

	/** Super guc bitince erken kapatmak icin. */
	public void endPower() {
		this.powerTicks = 0;
	}

	public boolean isPowerActive() {
		return this.powerTicks > 0;
	}

	/** Sunucu tarafi: tekme kombosu suruyorsa kalan tick. Bu sure boyunca yuzeye tekrar tutunma kapali. Kaydedilmez. */
	private int kickTicks;

	public void beginKick(int ticks) {
		this.kickTicks = Math.max(this.kickTicks, ticks);
	}

	public boolean isKickActive() {
		return this.kickTicks > 0;
	}

	/** ChaseGoal: klon modu veya tekme kombosu surerken duvar/tavana yeniden tutunma yapilmaz. */
	public boolean isReattachBlocked() {
		return this.isCloneMode() || this.kickTicks > 0;
	}

	/** Shield'in Slowness'i (amplifier 6) aktifse boss hareket etmemeli; yuzey/su kodu da buna uymali. */
	public static final int SHIELD_SLOWNESS_AMP = 6;

	public boolean isShieldFrozen() {
		net.minecraft.world.effect.MobEffectInstance slow = this.getEffect(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN);
		return slow != null && slow.getAmplifier() >= SHIELD_SLOWNESS_AMP;
	}

	/** Bir shield tetiginin tum parametreleri; guc yuzunden atlanirsa bekleme slotunda tutulur. */
	public record ShieldSpec(double threshold, int slowdownDuration, int slowdownAmp, int resistanceDuration, int resistanceAmp, int regenDuration, int regenAmp) {
	}

	/** Atlanan shield en fazla bu kadar tick bekler; sonra iptal olur (cok gec tetiklenmesin). */
	public static final int PENDING_SHIELD_TICKS = 60;

	private ShieldSpec pendingShield;
	private int pendingShieldTicks;

	public void queueShield(ShieldSpec spec) {
		this.pendingShield = spec;
		this.pendingShieldTicks = PENDING_SHIELD_TICKS;
	}

	/** Her tick (sunucu): guc bittiyse ve can hala esigin altindaysa bekleyen shield'i uygular, sure dolarsa atar. */
	private void tickPendingShield() {
		if (this.pendingShield == null)
			return;
		if (--this.pendingShieldTicks <= 0 || !this.isAlive()) {
			this.pendingShield = null;
			return;
		}
		if (this.isPowerActive() || this.isTransitioning())
			return;
		ShieldSpec spec = this.pendingShield;
		this.pendingShield = null;
		if (this.getHealth() < spec.threshold())
			ShadowHunterOnEntityTickProcedure.applyShield(this, spec);
	}

	/** Faz 2'ye gecisi tek seferlik baslatir. Gecis sekansi (donma, gorunum, buff) sonraki adimlarda buraya baglanir. */
	private void enterPhase2() {
		if (this.isPhase2())
			return;
		this.entityData.set(PHASE, 2);
		this.transitionTicks = PHASE2_TRANSITION_TICKS;
		this.setAnimation("animation.ShadowHunter.shield");
		this.beginPower(PHASE2_TRANSITION_TICKS + 20);
		this.applyPhase2Buffs();
		this.startTransitionEffects();
	}


	/** Faz 2 hiz carpani: kosma hizi 1 kademe (+%15). */
	public static final double PHASE2_SPEED_BONUS = 0.15D;
	/** Faz 2 hasar artisi (+%20). */
	public static final double PHASE2_DAMAGE_BONUS = 0.20D;
	public static final double PHASE2_KICK_ANIM_SPEED = 2.5D;
	/** Faz 2 dayaniklilik: gelen hasar bu carpanla azalir (-%20). */
	public static final float PHASE2_DAMAGE_TAKEN_MULT = 0.80F;
	/** Oklardan gelen hasar bu carpanla azalir (%70 guclu azalma). */
	public static final float ARROW_DAMAGE_MULT = 0.30F;

	private static final ResourceLocation PHASE2_SPEED_ID = ResourceLocation.parse("sculks_of_arda:shadow_hunter_phase2_speed");
	private static final ResourceLocation PHASE2_DAMAGE_ID = ResourceLocation.parse("sculks_of_arda:shadow_hunter_phase2_damage");

	/** Hiz ve hasar bonusunu ekler. Kaydedilmeyen (transient) modifier; dunya yuklenince faz 2 ise tekrar eklenir. */
	private void applyPhase2Buffs() {
		var speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (speed != null && !speed.hasModifier(PHASE2_SPEED_ID))
			speed.addTransientModifier(new net.minecraft.world.entity.ai.attributes.AttributeModifier(PHASE2_SPEED_ID, PHASE2_SPEED_BONUS, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		var dmg = this.getAttribute(Attributes.ATTACK_DAMAGE);
		if (dmg != null && !dmg.hasModifier(PHASE2_DAMAGE_ID))
			dmg.addTransientModifier(new net.minecraft.world.entity.ai.attributes.AttributeModifier(PHASE2_DAMAGE_ID, PHASE2_DAMAGE_BONUS, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
	}


	/** Hedef bu mesafeden (blok) uzaktaysa hiper sonik kosu acilir; bu mesafenin icine girince kapanir. */
	public static final double HYPERSONIC_RANGE = 40.0D;
	/** Hiper sonik ek hiz artisi (normal faz 2 hizinin ustune, +%100). */
	public static final double HYPERSONIC_SPEED_BONUS = 1.0D;
	private static final ResourceLocation HYPERSONIC_ID = ResourceLocation.parse("sculks_of_arda:shadow_hunter_hypersonic");

	/** Sunucu tarafi: hiper sonik kosu suruyor mu. Kaydedilmez. */
	private boolean hyperSonic;

	public boolean isHyperSonic() {
		return this.hyperSonic;
	}

	private void setHyperSonic(boolean on) {
		if (this.hyperSonic == on)
			return;
		this.hyperSonic = on;
		var speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (speed != null) {
			if (on) {
				if (!speed.hasModifier(HYPERSONIC_ID))
					speed.addTransientModifier(new net.minecraft.world.entity.ai.attributes.AttributeModifier(HYPERSONIC_ID, HYPERSONIC_SPEED_BONUS, net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
			} else {
				speed.removeModifier(HYPERSONIC_ID);
			}
		}
		if (this.level() instanceof net.minecraft.server.level.ServerLevel sl) {
			Vec3 c = this.bodyCenter();
			sl.playSound(null, this.blockPosition(), on ? net.minecraft.sounds.SoundEvents.WARDEN_SONIC_CHARGE : net.minecraft.sounds.SoundEvents.WARDEN_SONIC_BOOM, net.minecraft.sounds.SoundSource.HOSTILE, on ? 3.0F : 2.0F, on ? 0.8F : 1.2F);
			sl.sendParticles(net.minecraft.core.particles.ParticleTypes.SONIC_BOOM, c.x, c.y, c.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
		}
	}

	/** Her tick (sunucu, faz 2): hedef 40 bloktan uzaktaysa hiper sonik ac, icine girince kapat. */
	private void tickHyperSonic() {
		LivingEntity target = this.getTarget();
		boolean want = this.isPhase2() && this.isAlive() && !this.isTransitioning() && target != null && target.isAlive()
				&& !(target instanceof Player p && (p.isSpectator() || p.isCreative()))
				&& this.distanceTo(target) > HYPERSONIC_RANGE;
		this.setHyperSonic(want);
		if (!want)
			return;
		this.beginPower(3);
		this.removeEffect(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN);
		if (this.level() instanceof net.minecraft.server.level.ServerLevel sl) {
			Vec3 c = this.bodyCenter();
			sl.sendParticles(net.minecraft.core.particles.ParticleTypes.REVERSE_PORTAL, c.x, c.y, c.z, 10, 0.5D, 0.8D, 0.5D, 0.15D);
			sl.sendParticles(net.minecraft.core.particles.ParticleTypes.DRAGON_BREATH, c.x, c.y, c.z, 4, 0.4D, 0.6D, 0.4D, 0.02D);
			if (this.tickCount % 3 == 0)
				net.neoforged.neoforge.network.PacketDistributor.sendToPlayersTrackingEntity(this,
						new sculk.of.ixra.network.ShadowHunterAfterimageMessage(this.xo, this.yo, this.zo, this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot(), this.getRenderTexture(), 3, this.getSurface().get3DDataValue()));
		}
	}


	private static final int CLONE_HUNT_MAX_TICKS = 140;
	private static final int CLONE_TELEPORT_GAP = 6;
	private static final int CLONE_CYCLES = 2;
	private static final int CLONE_RETREAT_MIN = 40;
	private static final int CLONE_RETREAT_RANGE = 41;
	/** 0 = kapali, 1 = av (klonlar arasi isinlanarak yaklasir), 2 = kacis (vur-kac, oyuncunun tersine). Kaydedilmez. */
	private int cloneState;
	private int cloneTimer;
	private int cloneCycles;
	private int cloneHuntTicks;
	private int cloneHuntStartTime;

	public boolean isCloneMode() {
		return this.entityData.get(CLONE_MODE);
	}

	public boolean isCloneRetreating() {
		return this.cloneState == 2;
	}

	/** AI sayacindaki klon asamasi cagirir. Sadece faz 2, gecis/baska guc yokken baslar. */
	public void startCloneMode() {
		LivingEntity target = this.getTarget();
		if (this.level().isClientSide() || this.cloneState != 0 || !this.isPhase2() || !this.isAlive() || this.isTransitioning() || target == null)
			return;
		this.tryDetach();
		this.cloneCycles = CLONE_CYCLES;
		this.beginCloneHunt();
		this.entityData.set(CLONE_TARGET, target.getId());
		this.entityData.set(CLONE_MODE, true);
	}

	private void beginCloneHunt() {
		this.cloneState = 1;
		this.cloneTimer = 4;
		this.cloneHuntTicks = 0;
		// getLastHurtMobTimestamp() entity'nin tickCount'u ile yazilir; ayni saatle karsilastirmak icin tickCount saklanir.
		this.cloneHuntStartTime = this.tickCount;
	}

	private void endCloneMode() {
		this.cloneState = 0;
		this.entityData.set(CLONE_MODE, false);
		this.entityData.set(CLONE_TARGET, -1);
	}

	private void tickCloneMode() {
		if (this.cloneState == 0)
			return;
		LivingEntity target = this.getTarget();
		if (!this.isAlive() || this.isTransitioning() || target == null || !target.isAlive() || (target instanceof Player p && (p.isSpectator() || p.isCreative()))) {
			this.endCloneMode();
			return;
		}
		this.beginPower(3);
		this.removeEffect(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN);
		if (this.entityData.get(CLONE_TARGET) != target.getId())
			this.entityData.set(CLONE_TARGET, target.getId());

		if (this.cloneState == 1) {
			this.cloneHuntTicks++;
			boolean landedHit = this.getLastHurtMobTimestamp() >= this.cloneHuntStartTime && this.getLastHurtMob() == target && this.cloneHuntTicks > 5;
			if (landedHit || this.cloneHuntTicks >= CLONE_HUNT_MAX_TICKS) {
				this.cloneState = 2;
				this.cloneTimer = CLONE_RETREAT_MIN + this.random.nextInt(CLONE_RETREAT_RANGE);
				return;
			}
			if (--this.cloneTimer <= 0) {
				this.cloneTimer = CLONE_TELEPORT_GAP;
				this.cloneSwapTeleport(target);
			}
		} else if (--this.cloneTimer <= 0) {
			if (--this.cloneCycles > 0)
				this.beginCloneHunt();
			else
				this.endCloneMode();
		}
	}

	/** Halkadaki 7 klon noktasindan rastgele birine isinlanir (yakindaysa isinlanmaz). */
	private void cloneSwapTeleport(LivingEntity target) {
		double radius = ShadowHunterCloneRing.radiusFor(this.getX(), this.getZ(), target.getX(), target.getZ());
		if (this.distanceTo(target) < ShadowHunterCloneRing.MIN_RADIUS + 0.5D && this.random.nextInt(2) == 0)
			return;
		double base = ShadowHunterCloneRing.angleOf(this.getX(), this.getZ(), target.getX(), target.getZ());
		int start = 1 + this.random.nextInt(ShadowHunterCloneRing.POINTS - 1);
		for (int i = 0; i < ShadowHunterCloneRing.POINTS - 1; i++) {
			int k = 1 + (start - 1 + i) % (ShadowHunterCloneRing.POINTS - 1);
			Vec3 p = ShadowHunterCloneRing.point(this.level(), target.getX(), target.getY(), target.getZ(), radius, base + k * ShadowHunterCloneRing.STEP);
			if (p == null)
				continue;
			// Boss'un gercek hitbox'i (faz 2 olcegi dahil) bu noktada bos degilse sonraki noktayi dene.
			if (!this.level().noCollision(this, this.boxFor(Direction.UP, p)))
				continue;
			if (this.level() instanceof net.minecraft.server.level.ServerLevel sl) {
				net.neoforged.neoforge.network.PacketDistributor.sendToPlayersTrackingEntity(this,
						new sculk.of.ixra.network.ShadowHunterAfterimageMessage(this.getX(), this.getY(), this.getZ(), this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot(), this.getRenderTexture(), 3, this.getSurface().get3DDataValue()));
				sl.playSound(null, this.blockPosition(), net.minecraft.sounds.SoundEvents.ENDERMAN_TELEPORT, net.minecraft.sounds.SoundSource.HOSTILE, 1.0F, 1.2F);
			}
			if (this.isOnSurface())
				this.setSurface(Direction.UP);
			this.teleportTo(p.x, p.y, p.z);
			this.setDeltaMovement(Vec3.ZERO);
			this.fallDistance = 0.0F;
			float yaw = (float) (Mth.atan2(target.getZ() - p.z, target.getX() - p.x) * (180.0D / Math.PI)) - 90.0F;
			this.setYRot(yaw);
			this.setYHeadRot(yaw);
			this.setYBodyRot(yaw);
			return;
		}
	}


	/** Gecis suresi: 2 saniye donma + dokunulmazlik. */
	public static final int PHASE2_TRANSITION_TICKS = 40;
	private static final double SHOCKWAVE_RADIUS = 10.0D;
	private static final double TRANSITION_RING_RADIUS = 8.0D;

	/** Kalan gecis tick'i (sunucu). Kaydedilmez; gecis sirasinda dunya kapanirsa gecis atlanir, faz 2 kalir. */
	private int transitionTicks;

	/** Gecis sirasinda boss donmus ve dokunulmaz. ChaseGoal da bunu kontrol eder. */
	public boolean isTransitioning() {
		return this.transitionTicks > 0;
	}

	public Vec3 bodyCenter() {
		Direction n = this.getSurface();
		return this.position().add(n.getStepX() * 1.2D, n.getStepY() * 1.2D, n.getStepZ() * 1.2D).add(0.0D, this.isOnSurface() ? 0.0D : 1.2D, 0.0D);
	}

	/** Gecisin ilk ani: kukreme, itme dalgasi, ekran sarsintisi. */
	private void startTransitionEffects() {
		if (!(this.level() instanceof net.minecraft.server.level.ServerLevel sl))
			return;
		sl.playSound(null, this.blockPosition(), net.minecraft.sounds.SoundEvents.WARDEN_ROAR, net.minecraft.sounds.SoundSource.HOSTILE, 4.0F, 0.7F);
		sl.playSound(null, this.blockPosition(), net.minecraft.sounds.SoundEvents.WARDEN_SONIC_BOOM, net.minecraft.sounds.SoundSource.HOSTILE, 2.0F, 0.6F);
		Vec3 c = this.bodyCenter();
		for (ServerPlayer sp : sl.players()) {
			double d = sp.distanceTo(this);
			if (d <= 48.0D)
				net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(sp, new sculk.of.ixra.network.ShadowHunterKickShakeMessage(30));
			if (d <= SHOCKWAVE_RADIUS && !sp.isSpectator()) {
				Vec3 away = new Vec3(sp.getX() - c.x, 0.0D, sp.getZ() - c.z);
				away = away.lengthSqr() < 1.0E-4D ? new Vec3(1.0D, 0.0D, 0.0D) : away.normalize();
				sp.push(away.x * 1.6D, 0.55D, away.z * 1.6D);
				sp.hurtMarked = true;
			}
		}
	}

	/** Her tick: donma. Mor lazer halka client tarafinda cizilir (ShadowHunterTransitionRingClient). */
	private void tickTransition() {
		this.getNavigation().stop();
		Vec3 dm = this.getDeltaMovement();
		this.setDeltaMovement(0.0D, this.isOnSurface() ? 0.0D : Math.min(dm.y, 0.0D), 0.0D);
		if (this.transitionTicks == 1) {
			if (this.level() instanceof net.minecraft.server.level.ServerLevel sl)
				sl.playSound(null, this.blockPosition(), net.minecraft.sounds.SoundEvents.WARDEN_ATTACK_IMPACT, net.minecraft.sounds.SoundSource.HOSTILE, 3.0F, 0.6F);
			this.setAnimation("empty");
		}
		this.transitionTicks--;
	}

	/** Client: gecis halkasinin yaricapi / merkezi / yuksekligi (ShadowHunterTransitionRingClient kullanir). */
	public static final double TRANSITION_RING_RADIUS_PUBLIC = TRANSITION_RING_RADIUS;

	public double getTransitionRingY() {
		return this.isOnSurface() ? this.bodyCenter().y : this.getY() + 0.15D;
	}

	/** Gecis suruyor mu? (PHASE_AGE senkron oldugu icin client'ta da calisir.) */
	public boolean isPhase2TransitionVisual() {
		return this.isPhase2() && this.entityData.get(PHASE_AGE) < PHASE2_TRANSITION_TICKS;
	}

	public float getPhase2AgeTicks() {
		return this.entityData.get(PHASE_AGE);
	}

	/** Duvar/tavan modunda hitbox'in yuzeyden disari dogru uzunlugu (zeminde normal boy: 3.5). */
	public static final float SURFACE_DEPTH = 2.0F;

	/** Sunucu tarafi: son tick'te yuzey uzerinde alinan mesafenin karesi (animasyon/iz icin). */
	public double surfaceSpeedSqr;

	private final Quaternionf surfaceQuat = new Quaternionf();
	private final Quaternionf surfaceQuatO = new Quaternionf();

	public Direction getSurface() {
		return this.entityData.get(SURFACE);
	}

	public boolean isOnSurface() {
		return this.getSurface() != Direction.UP;
	}

	/** Yuzeyi degistirir ve hitbox'i hemen yeniden kurar. */
	public void setSurface(Direction surface) {
		if (this.getSurface() == surface)
			return;
		this.entityData.set(SURFACE, surface);
		this.setPos(this.getX(), this.getY(), this.getZ());
	}

	/** Yuzey degistiginde (istemcide de) hitbox yeniden kurulur. */
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);
		if (SURFACE.equals(key))
			this.setPos(this.getX(), this.getY(), this.getZ());
	}

	/** Verilen yuzey yonu icin, boss'un "yukari"sini o yone ceviren donusum. */
	public static Quaternionf surfaceRotation(Direction surface) {
		Quaternionf q = new Quaternionf();
		switch (surface) {
			case DOWN -> q.rotationX((float) Math.PI);
			case EAST -> q.rotationZ(-(float) Math.PI / 2.0F);
			case WEST -> q.rotationZ((float) Math.PI / 2.0F);
			case SOUTH -> q.rotationX((float) Math.PI / 2.0F);
			case NORTH -> q.rotationX(-(float) Math.PI / 2.0F);
			default -> {
			}
		}
		return q;
	}

	/** Ayaklar (pos) yuzeyde, govde yuzeyden disari dogru uzanan eksen hizali kutu. */
	public static AABB surfaceBox(Vec3 p, Direction n, double width, double depth) {
		double h = width * 0.5D;
		return switch (n) {
			case UP -> new AABB(p.x - h, p.y, p.z - h, p.x + h, p.y + depth, p.z + h);
			case DOWN -> new AABB(p.x - h, p.y - depth, p.z - h, p.x + h, p.y, p.z + h);
			case EAST -> new AABB(p.x, p.y - h, p.z - h, p.x + depth, p.y + h, p.z + h);
			case WEST -> new AABB(p.x - depth, p.y - h, p.z - h, p.x, p.y + h, p.z + h);
			case SOUTH -> new AABB(p.x - h, p.y - h, p.z, p.x + h, p.y + h, p.z + depth);
			default -> new AABB(p.x - h, p.y - h, p.z - depth, p.x + h, p.y + h, p.z);
		};
	}

	@Override
	protected AABB makeBoundingBox() {
		if (this.entityData == null)
			return super.makeBoundingBox();
		Direction surface = this.entityData.get(SURFACE);
		if (surface == Direction.UP)
			return super.makeBoundingBox();
		return surfaceBox(this.position(), surface, this.getBbWidth(), SURFACE_DEPTH);
	}

	/** Verilen yuzey icin, ayaklar 'feet' noktasindayken kutu. */
	public AABB boxFor(Direction surface, Vec3 feet) {
		if (surface == Direction.UP)
			return this.getDimensions(this.getPose()).makeBoundingBox(feet);
		return surfaceBox(feet, surface, this.getBbWidth(), SURFACE_DEPTH);
	}

	/** Yuzeyin icindeki 'f' yonune bakacak sekilde yaw ayarlar (model yuzeyle birlikte dondugu icin yaw yerel eksende hesaplanir). */
	public void faceAlong(Vec3 f) {
		if (f.lengthSqr() < 1.0E-6D)
			return;
		Quaternionf inv = new Quaternionf(surfaceRotation(this.getSurface())).invert();
		Vector3f l = inv.transform(new Vector3f((float) f.x, (float) f.y, (float) f.z));
		if (l.x * l.x + l.z * l.z < 1.0E-6F)
			return;
		float yaw = (float) (Mth.atan2(-l.x, l.z) * (180.0D / Math.PI));
		this.setYRot(yaw);
		this.setYBodyRot(yaw);
		this.setYHeadRot(yaw);
	}

	/** Yuzeyden ayrilip dik konuma (zemin moduna) doner; yer bulunamazsa false. Sonrasinda yercekimi devreye girer. */
	public boolean tryDetach() {
		Direction n = this.getSurface();
		if (n == Direction.UP)
			return true;
		Vec3 p = this.position();
		for (int i = 0; i < 4; i++) {
			Vec3 q;
			if (n == Direction.DOWN) {
				q = new Vec3(p.x, p.y - 3.52D - i * 0.0D, p.z);
				if (i > 0)
					break;
			} else {
				q = new Vec3(p.x + n.getStepX() * 0.45D, p.y - 0.4D - i, p.z + n.getStepZ() * 0.45D);
			}
			if (this.level().noCollision(this, this.boxFor(Direction.UP, q))) {
				this.setSurface(Direction.UP);
				this.setPos(q.x, q.y, q.z);
				this.setDeltaMovement(Vec3.ZERO);
				this.fallDistance = 0.0F;
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean convertsInWater() {
		return false;
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	public float maxUpStep() {
		return this.isOnSurface() ? 0.0F : super.maxUpStep();
	}

	@Override
	protected BodyRotationControl createBodyControl() {
		return new BodyRotationControl(this) {
			@Override
			public void clientTick() {
				if (ShadowHunterEntity.this.isOnSurface()) {
					ShadowHunterEntity.this.yBodyRot = ShadowHunterEntity.this.getYRot();
					ShadowHunterEntity.this.yHeadRot = ShadowHunterEntity.this.getYRot();
					return;
				}
				super.clientTick();
			}
		};
	}

	public Quaternionf getSurfaceQuat(float partialTick) {
		return new Quaternionf(this.surfaceQuatO).slerp(this.surfaceQuat, partialTick);
	}

	/** Puppet/afterimage icin: donusu yumusatmadan hedefe oturtur. */
	public void snapSurfaceQuat() {
		Quaternionf target = surfaceRotation(this.getSurface());
		this.surfaceQuat.set(target);
		this.surfaceQuatO.set(target);
	}

	@Override
	public boolean isNoGravity() {
		return super.isNoGravity() || this.isOnSurface();
	}

	@Override
	public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
		return false;
	}

	@Override
	public boolean isInWall() {
		return this.isOnSurface() ? false : super.isInWall();
	}

	@Override
	public boolean hasLineOfSight(Entity entity) {
		if (!this.isOnSurface())
			return super.hasLineOfSight(entity);
		if (entity.level() != this.level())
			return false;
		Direction n = this.getSurface();
		Vec3 from = this.position().add(n.getStepX() * 1.3D, n.getStepY() * 1.3D, n.getStepZ() * 1.3D);
		Vec3 to = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
		if (to.distanceTo(from) > 128.0D)
			return false;
		return this.level().clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
	}

	public void setTexture(String texture) {
		this.entityData.set(TEXTURE, texture);
	}

	public String getTexture() {
		return this.entityData.get(TEXTURE);
	}

	public static final String PHASE2_TEXTURE_SUFFIX = "_phase2";

	public String getRenderTexture() {
		String base = this.getTexture();
		if (this.getPhase2Progress() >= 1.0F && !base.endsWith(PHASE2_TEXTURE_SUFFIX))
			return base + PHASE2_TEXTURE_SUFFIX;
		return base;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new sculk.of.ixra.entity.ai.ShadowHunterCloneRetreatGoal(this));
		this.goalSelector.addGoal(1, new ShadowHunterChaseGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 16.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float f) {
		return super.getPassengerAttachmentPoint(entity, dimensions, f).add(0, 0.1f, 0);
	}

	@Override
	public SoundEvent getAmbientSound() {
		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.warden.ambient"));
	}

	@Override
	public void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.warden.step")), 0.15f, 1);
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.warden.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.warden.death"));
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isTransitioning() && !source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY))
			return false;
		if (source.is(DamageTypes.IN_FIRE))
			return false;
		if (source.getDirectEntity() instanceof net.minecraft.world.entity.projectile.ThrownTrident)
			return false;
		if (source.getDirectEntity() instanceof AbstractArrow)
			amount *= ARROW_DAMAGE_MULT;
		if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud || source.typeHolder().is(NeoForgeMod.POISON_DAMAGE))
			return false;
		if (source.is(DamageTypes.FALL) || source.is(DamageTypes.IN_WALL))
			return false;
		if (source.is(DamageTypes.DROWN))
			return false;
		if (source.is(DamageTypes.LIGHTNING_BOLT))
			return false;
		if (source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION))
			return false;
		if (source.is(DamageTypes.TRIDENT))
			return false;
		if (source.is(DamageTypes.FALLING_ANVIL))
			return false;
		if (source.is(DamageTypes.WITHER) || source.is(DamageTypes.WITHER_SKULL))
			return false;
		if (this.isPhase2() && !source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY))
			amount *= PHASE2_DAMAGE_TAKEN_MULT;
		return super.hurt(source, amount);
	}

	@Override
	public boolean ignoreExplosion(Explosion explosion) {
		return true;
	}

	@Override
	public boolean fireImmune() {
		return true;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("Texture", this.getTexture());
		compound.putInt("Phase", this.getPhase());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("Texture"))
			this.setTexture(compound.getString("Texture"));
		if (compound.contains("Phase"))
			this.entityData.set(PHASE, Math.max(1, compound.getInt("Phase")));
		if (this.isPhase2()) {
			this.entityData.set(PHASE_AGE, PHASE2_TRANSITION_TICKS);
			this.applyPhase2Buffs();
		}
	}

	private int surfaceIdleTicks;

	@Override
	public void baseTick() {
		super.baseTick();
		if (!this.level().isClientSide()) {
			this.setAirSupply(this.getMaxAirSupply());
			if (this.powerTicks > 0)
				this.powerTicks--;
			if (this.kickTicks > 0)
				this.kickTicks--;
			if (!this.isPhase2() && this.isAlive() && this.getHealth() < this.getMaxHealth() * PHASE2_HEALTH_RATIO)
				this.enterPhase2();
			if (this.transitionTicks > 0)
				this.tickTransition();
			this.tickHyperSonic();
			this.tickCloneMode();
			this.tickPendingShield();
		}
		this.surfaceSpeedSqr = this.position().distanceToSqr(this.xo, this.yo, this.zo);
		this.surfaceQuatO.set(this.surfaceQuat);
		this.surfaceQuat.slerp(surfaceRotation(this.getSurface()), 0.3F);
		if (!this.level().isClientSide() && this.isOnSurface()) {
			LivingEntity t = this.getTarget();
			if (t == null || !t.isAlive())
				this.surfaceIdleTicks++;
			else
				this.surfaceIdleTicks = 0;
			if (this.isDeadOrDying() || this.surfaceIdleTicks > 10) {
				if (!this.tryDetach() && (this.isDeadOrDying() || this.surfaceIdleTicks > 100))
					this.setSurface(Direction.UP);
			}
		}
		ShadowHunterOnEntityTickProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ(), this);
		if (!this.level().isClientSide() && this.isPhase2() && this.entityData.get(PHASE_AGE) < PHASE2_TRANSITION_TICKS)
			this.entityData.set(PHASE_AGE, this.entityData.get(PHASE_AGE) + 1);
		this.refreshDimensions();
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose pose) {
		return super.getDefaultDimensions(pose).scale(this.getPhaseScale());
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Override
	public void customServerAiStep() {
		super.customServerAiStep();
		this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
		this.bossInfo.setColor(this.isPhase2() ? ServerBossEvent.BossBarColor.PURPLE : ServerBossEvent.BossBarColor.BLUE);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		this.updateSwingTime();
		if (this.horizontalCollision && this.onGround() && !this.jumping && !this.isOnSurface() && this.getTarget() != null) {
			this.getJumpControl().jump();
		}
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.4);
		builder = builder.add(Attributes.MAX_HEALTH, 1440);
		builder = builder.add(Attributes.ARMOR, 8);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 18);
		builder = builder.add(Attributes.FOLLOW_RANGE, 1000);
		builder = builder.add(Attributes.STEP_HEIGHT, 2.0625);
		builder = builder.add(Attributes.JUMP_STRENGTH, 0.6);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 101.5);
		builder = builder.add(Attributes.ATTACK_KNOCKBACK, 2.2);
		builder = builder.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
		return builder;
	}

	private PlayState movementPredicate(AnimationState event) {
		if (this.isDeadOrDying())
			return PlayState.CONTINUE;
		if (this.animationprocedure.equals("empty")) {
			if ((event.isMoving() || (this.isOnSurface() && this.surfaceSpeedSqr > 4.0E-4D) || !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) && !this.isSprinting()) {
				return event.setAndContinue(RawAnimation.begin().thenLoop("animation.ShadowHunter.walk"));
			}
			if (this.isSprinting()) {
				return event.setAndContinue(RawAnimation.begin().thenLoop("animation.ShadowHunter.run"));
			}
			return event.setAndContinue(RawAnimation.begin().thenLoop("animation.ShadowHunter.ambient"));
		}
		return PlayState.STOP;
	}

	private PlayState attackingPredicate(AnimationState event) {
		if (this.isDeadOrDying())
			return PlayState.CONTINUE;
		if (getAttackAnim(event.getPartialTick()) > 0f && !this.swinging) {
			this.swinging = true;
			this.lastSwing = level().getGameTime();
		}
		if (this.swinging && this.lastSwing + 2L <= level().getGameTime()) {
			this.swinging = false;
		}
		if (this.swinging && !this.animationprocedure.equals("empty"))
			return PlayState.STOP;
		if (this.swinging && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
			event.getController().forceAnimationReset();
			return event.setAndContinue(RawAnimation.begin().thenPlay("animation.ShadowHunter.attack"));
		}
		return PlayState.CONTINUE;
	}

	String prevAnim = "empty";

	private PlayState procedurePredicate(AnimationState event) {
		if (this.isDeadOrDying())
			return PlayState.CONTINUE;
		if (!animationprocedure.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED || (!this.animationprocedure.equals(prevAnim) && !this.animationprocedure.equals("empty"))) {
			if (!this.animationprocedure.equals(prevAnim))
				event.getController().forceAnimationReset();
			event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
			if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
				this.animationprocedure = "empty";
				event.getController().forceAnimationReset();
			}
		} else if (animationprocedure.equals("empty")) {
			prevAnim = "empty";
			return PlayState.STOP;
		}
		prevAnim = this.animationprocedure;
		return PlayState.CONTINUE;
	}

	public static final int CORPSE_TICKS = 150;

	@Override
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime == 1)
			this.bossInfo.removeAllPlayers();
		if (this.deathTime == 60)
			this.dropExperience(this);
		if (this.deathTime >= CORPSE_TICKS)
			this.remove(ShadowHunterEntity.RemovalReason.KILLED);
	}

	@Override
	public AABB getBoundingBoxForCulling() {
		AABB box = super.getBoundingBoxForCulling();
		return this.isDeadOrDying() ? box.inflate(10.0D) : box;
	}

	public String getSyncedAnimation() {
		return this.entityData.get(ANIMATION);
	}

	public void setAnimation(String animation) {
		this.entityData.set(ANIMATION, animation);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
		data.add(new AnimationController<>(this, "movement", 1, this::movementPredicate));
		data.add(new AnimationController<>(this, "attacking", 1, this::attackingPredicate));
		data.add(new AnimationController<ShadowHunterEntity>(this, "procedure", 1, this::procedurePredicate)
				.setAnimationSpeedHandler(e -> e.isPhase2() && "animation.ShadowHunter.Tekme".equals(e.animationprocedure) ? PHASE2_KICK_ANIM_SPEED : 1.0D));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
