package sculk.of.ixra.procedures;

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
		double repeat = 0;
		double radianArc = 0;
		double d = 0;
		double verticalOffset = 0;
		double sign = 0;
		double rollAngle = 0;
		double horizontalOffsetDir = 0;
		double i = 0;
		double dMajor = 0;
		double horizontalOffsetMag = 0;
		double circleDistanceConstant = 0;
		double radianSteps = 0;
		double arcStart = 0;
		double repeatfix = 0;
		if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
			entity.getPersistentData().putDouble("AI", (entity.getPersistentData().getDouble("AI") + 1));
		}
		if (entity.getPersistentData().getDouble("AI") == 60) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < 50) {
				if (entity instanceof RadioactiveWardenEntity) {
					((RadioactiveWardenEntity) entity).setAnimation("kalkan");
				}
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 6));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 2));
			}
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 60, 1));
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60, 1));
			entity.getPersistentData().putDouble("AI", (entity.getPersistentData().getDouble("AI") + 1));
		}
		if (entity.getPersistentData().getDouble("AI") == 180) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < 30) {
				if (entity instanceof RadioactiveWardenEntity) {
					((RadioactiveWardenEntity) entity).setAnimation("kalkan");
				}
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 6));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 2));
			}
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 1));
			entity.getPersistentData().putDouble("AI", (entity.getPersistentData().getDouble("AI") + 1));
		}
		if (entity.getPersistentData().getDouble("AI") == 230) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120, 1));
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 1));
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) > 100) {
				if (entity instanceof RadioactiveWardenEntity) {
					((RadioactiveWardenEntity) entity).setAnimation("yer");
				}
				SculksOfArdaMod.queueServerWork(20, () -> {
					{
						final Vec3 _center = new Vec3(x, y, z);
						List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(16 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
						for (Entity entityiterator : _entfound) {
							if (!(entityiterator == entity)) {
								entityiterator.push(0.5, 1, 0.5);
								entityiterator.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), 16);
								if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
									_entity.addEffect(new MobEffectInstance(MobEffects.POISON, 120, 1));
							}
						}
					}
				});
			}
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < 275) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 2));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 120, 2));
				entity.getPersistentData().putDouble("AI", (entity.getPersistentData().getDouble("AI") + 1));
			}
		}
		if (entity.getPersistentData().getDouble("AI") == 300) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < 50) {
				if (entity instanceof RadioactiveWardenEntity) {
					((RadioactiveWardenEntity) entity).setAnimation("kalkan");
				}
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 6));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 2));
			}
			entity.getPersistentData().putDouble("AI", 301);
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = SculksOfArdaModEntities.SCULK_CREAKING.get().spawn(_level, BlockPos.containing(x + 1, y, z), MobSpawnType.MOB_SUMMONED);
				if (entityToSpawn != null) {
				}
			}
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = SculksOfArdaModEntities.SCULK_CREAKING.get().spawn(_level, BlockPos.containing(x - 1, y, z), MobSpawnType.MOB_SUMMONED);
				if (entityToSpawn != null) {
				}
			}
			{
				final Vec3 _center = new Vec3(x, y, z);
				List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(16 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
				for (Entity entityiterator : _entfound) {
					if (entityiterator instanceof SculkCreakingEntity animatable)
						animatable.setTexture("sculkcrakingradioactive");
				}
			}
			entity.getPersistentData().putDouble("AI", (entity.getPersistentData().getDouble("AI") + 1));
		}
		if (entity.getPersistentData().getDouble("AI") == 360) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < 225) {
				if (entity instanceof RadioactiveWardenEntity) {
					((RadioactiveWardenEntity) entity).setAnimation("kalkan");
				}
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 6));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 6));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
			}
			entity.getPersistentData().putDouble("AI", (entity.getPersistentData().getDouble("AI") + 1));
		}
		if (entity.getPersistentData().getDouble("AI") == 420) {
			entity.getPersistentData().putDouble("AI", (entity.getPersistentData().getDouble("AI") + 1));
			SculksOfArdaMod.queueServerWork(20, () -> {
				if (entity instanceof RadioactiveWardenEntity) {
					((RadioactiveWardenEntity) entity).setAnimation("sonic");
				}
			});
			for (int index0 = 0; index0 < 12; index0++) {
				repeatfix = repeatfix + 1;
				world.addParticle(ParticleTypes.SONIC_BOOM,
						(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(repeatfix)), ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entity)).getBlockPos().getX()),
						(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(repeatfix)), ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entity)).getBlockPos().getY()),
						(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(repeatfix)), ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ()), 0,
						1.1, 0);
				{
					final Vec3 _center = new Vec3(
							(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(repeatfix)), ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entity)).getBlockPos().getX()),
							(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(repeatfix)), ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entity)).getBlockPos().getY()),
							(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(repeatfix)), ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ()));
					List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(12 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
					for (Entity entityiterator : _entfound) {
						SculksOfArdaMod.queueServerWork(35, () -> {
							if (!(entityiterator == entity)) {
								entityiterator.hurt(new DamageSource(world.holderOrThrow(DamageTypes.SONIC_BOOM)), 14);
								if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
									_entity.addEffect(new MobEffectInstance(MobEffects.POISON, 120, 1));
							}
						});
					}
				}
			}
		}
		if (entity.getPersistentData().getDouble("AI") == 500) {
			entity.getPersistentData().putDouble("AI", (entity.getPersistentData().getDouble("AI") + 1));
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < 60) {
				if (entity instanceof RadioactiveWardenEntity) {
					((RadioactiveWardenEntity) entity).setAnimation("kalkan");
				}
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 6));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
			}
		}
		if (entity.getPersistentData().getDouble("AI") == 580) {
			entity.getPersistentData().putDouble("AI", 0);
		}
	}
}
