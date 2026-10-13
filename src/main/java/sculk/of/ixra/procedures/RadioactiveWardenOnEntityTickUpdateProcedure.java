package sculk.of.ixra.procedures;

import sculk.of.ixra.init.SculksOfArdaModParticleTypes;
import sculk.of.ixra.init.SculksOfArdaModEntities;
import sculk.of.ixra.entity.SculkCreakingEntity;
import sculk.of.ixra.entity.RadioactiveWardenEntity;
import sculk.of.ixra.SculksOfArdaMod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Comparator;

public class RadioactiveWardenOnEntityTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;

		if ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) != null) {
			entity.getPersistentData().putDouble("AI", (entity.getPersistentData().getDouble("AI") + 1));
		}

		double ai = entity.getPersistentData().getDouble("AI");

		if (ai == 60) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < 98) {
				if (entity instanceof RadioactiveWardenEntity)
					((RadioactiveWardenEntity) entity).setAnimation("kalkan");
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 219, 6));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 2));
			}
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 60, 1));
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60, 1));
			entity.getPersistentData().putDouble("AI", ai + 1);
		}

		if (ai == 180) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < 59) {
				if (entity instanceof RadioactiveWardenEntity)
					((RadioactiveWardenEntity) entity).setAnimation("kalkan");
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 219, 6));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 2));
			}
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 1));
			entity.getPersistentData().putDouble("AI", ai + 1);
		}

		if (ai == 230) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120, 1));
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 1));

			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) > 196) {
				if (entity instanceof RadioactiveWardenEntity)
					((RadioactiveWardenEntity) entity).setAnimation("yer");
				SculksOfArdaMod.queueServerWork(20, () -> {
					Vec3 center = new Vec3(x, y, z);
					List<Entity> found = world.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(16 / 2d), e -> true)
							.stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList();
					for (Entity found_entity : found) {
						if (found_entity == entity)
							continue;
						found_entity.push(0.5, 1, 0.5);
						found_entity.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), 16);
						if (found_entity instanceof LivingEntity livingEntity && !livingEntity.level().isClientSide())
							livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 120, 1));
					}
				});
			}

			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < 539) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 2));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 120, 2));
				entity.getPersistentData().putDouble("AI", ai + 1);
			}
		}

		if (ai == 300) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < 98) {
				if (entity instanceof RadioactiveWardenEntity)
					((RadioactiveWardenEntity) entity).setAnimation("kalkan");
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 219, 6));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 2));
			}

			entity.getPersistentData().putDouble("AI", 301);

			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = SculksOfArdaModEntities.SCULK_CREAKING.get().spawn(_level, BlockPos.containing(x + 1, y, z), MobSpawnType.MOB_SUMMONED);
			}
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = SculksOfArdaModEntities.SCULK_CREAKING.get().spawn(_level, BlockPos.containing(x - 1, y, z), MobSpawnType.MOB_SUMMONED);
			}

			Vec3 center = new Vec3(x, y, z);
			List<Entity> found = world.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(16 / 2d), e -> true)
					.stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList();
			for (Entity found_entity : found) {
				if (found_entity instanceof SculkCreakingEntity animatable)
					animatable.setTexture("sculkcrakingradioactive");
			}

			entity.getPersistentData().putDouble("AI", entity.getPersistentData().getDouble("AI") + 1);
		}

		if (ai == 360) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < 440) {
				if (entity instanceof RadioactiveWardenEntity)
					((RadioactiveWardenEntity) entity).setAnimation("kalkan");
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 219, 6));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 6));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
			}
			entity.getPersistentData().putDouble("AI", ai + 1);
		}

		if (ai == 420) {
			entity.getPersistentData().putDouble("AI", ai + 1);
			// Everything below used to fire on three separate timers (particles instantly, the
			// "sonic" animation 20 ticks later, damage 35 ticks after that) which is why the
			// boom, its visual, and the hit felt completely disconnected from each other.
			// Now the particles and the attack animation start together, and the hit lands a
			// short, fixed moment later so it reads as "the wave travels out and then hits".
			SculksOfArdaMod.queueServerWork(20, () -> {
				if (entity instanceof RadioactiveWardenEntity)
					((RadioactiveWardenEntity) entity).setAnimation("sonic");

				Vec3 eyePos = entity.getEyePosition(1f);
				Vec3 viewDir = entity.getViewVector(1f);

				for (int index0 = 0; index0 < 12; index0++) {
					double distance = index0 + 1;
					Vec3 targetPos = eyePos.add(viewDir.scale(distance));
					BlockPos clipPos = entity.level()
							.clip(new ClipContext(eyePos, targetPos, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entity))
							.getBlockPos();

					if (world instanceof ServerLevel _level)
						// count = 0 tells the game to use xOffset/yOffset/zOffset as the single
						// particle's actual velocity (scaled by speed) instead of random jitter,
						// which is what lets the particle travel forward and grow client-side.
						_level.sendParticles(SculksOfArdaModParticleTypes.RADIOACTIVE_SONIC_BOOM.get(), clipPos.getX() + 0.5, clipPos.getY() + 0.5, clipPos.getZ() + 0.5, 0, viewDir.x * 0.6, viewDir.y * 0.6, viewDir.z * 0.6, 1.0);

					Vec3 center = new Vec3(clipPos.getX(), clipPos.getY(), clipPos.getZ());
					List<Entity> found = world.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(12 / 2d), e -> true)
							.stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList();
					for (Entity found_entity : found) {
						if (found_entity == entity)
							continue;
						SculksOfArdaMod.queueServerWork(6, () -> {
							found_entity.hurt(new DamageSource(world.holderOrThrow(DamageTypes.SONIC_BOOM)), 14);
							if (found_entity instanceof LivingEntity livingEntity && !livingEntity.level().isClientSide())
								livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 120, 1));
						});
					}
				}
			});
		}

		if (ai == 500) {
			entity.getPersistentData().putDouble("AI", ai + 1);
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < 118) {
				if (entity instanceof RadioactiveWardenEntity)
					((RadioactiveWardenEntity) entity).setAnimation("kalkan");
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 219, 6));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
			}
		}

		if (ai == 580) {
			entity.getPersistentData().putDouble("AI", 0);
		}
	}
}
