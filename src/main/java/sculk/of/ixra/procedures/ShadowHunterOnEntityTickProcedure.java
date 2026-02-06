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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerPlayer;

public class ShadowHunterOnEntityTickProcedure {
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
		double AI = 0;
		if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == null)) {
			entity.getPersistentData().putDouble("AI", (entity.getPersistentData().getDouble("AI") + 1));
		}
		if (entity.getPersistentData().getDouble("AI") == 60) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < 50) {
				if (entity instanceof ShadowHunterEntity) {
					((ShadowHunterEntity) entity).setAnimation("animation.ShadowHunter.shield");
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
				if (entity instanceof ShadowHunterEntity) {
					((ShadowHunterEntity) entity).setAnimation("animation.ShadowHunter.shield");
				}
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 6));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 2));
			}
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 1));
			entity.getPersistentData().putDouble("AI", (entity.getPersistentData().getDouble("AI") + 1));
		}
		if (entity.getPersistentData().getDouble("AI") == 230) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 1));
			entity.getPersistentData().putDouble("AI", (entity.getPersistentData().getDouble("AI") + 1));
		}
		if (entity.getPersistentData().getDouble("AI") == 300) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < 50) {
				if (entity instanceof ShadowHunterEntity) {
					((ShadowHunterEntity) entity).setAnimation("animation.ShadowHunter.shield");
				}
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 6));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 2));
			}
			entity.getPersistentData().putDouble("AI", 301);
			entity.getPersistentData().putDouble("AI", (entity.getPersistentData().getDouble("AI") + 1));
		}
		if (entity.getPersistentData().getDouble("AI") == 360) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < 225) {
				if (entity instanceof ShadowHunterEntity) {
					((ShadowHunterEntity) entity).setAnimation("animation.ShadowHunter.shield");
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
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 1));
			if ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) instanceof LivingEntity) {
				{
					Entity _ent = entity;
					_ent.teleportTo(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX() + 3), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
							((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ()));
					if (_ent instanceof ServerPlayer _serverPlayer)
						_serverPlayer.connection.teleport(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX() + 3), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
								((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ()), _ent.getYRot(), _ent.getXRot());
				}
				if (entity instanceof ShadowHunterEntity) {
					((ShadowHunterEntity) entity).setAnimation("animation.ShadowHunter.Tekme");
				}
				SculksOfArdaMod.queueServerWork(8, () -> {
					(entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), 4);
					(entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).push(1, 0.5, 1);
				});
			}
			SculksOfArdaMod.queueServerWork(25, () -> {
				if ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) instanceof LivingEntity) {
					{
						Entity _ent = entity;
						_ent.teleportTo(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX() - 3), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
								((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ()));
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX() - 3), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
									((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ()), _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof ShadowHunterEntity) {
						((ShadowHunterEntity) entity).setAnimation("animation.ShadowHunter.Tekme");
					}
					SculksOfArdaMod.queueServerWork(8, () -> {
						(entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), 4);
						(entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).push(1, 0.5, 1);
					});
				}
			});
			SculksOfArdaMod.queueServerWork(50, () -> {
				if ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) instanceof LivingEntity) {
					{
						Entity _ent = entity;
						_ent.teleportTo(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX()), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
								((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ() + 3));
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX()), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
									((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ() + 3), _ent.getYRot(), _ent.getXRot());
					}
					if (entity instanceof ShadowHunterEntity) {
						((ShadowHunterEntity) entity).setAnimation("animation.ShadowHunter.Tekme");
					}
					SculksOfArdaMod.queueServerWork(8, () -> {
						(entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), 4);
						(entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).push(1, 0.5, 1);
					});
				}
			});
			entity.getPersistentData().putDouble("AI", (entity.getPersistentData().getDouble("AI") + 1));
		}
		if (entity.getPersistentData().getDouble("AI") == 500) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 1));
			entity.getPersistentData().putDouble("AI", (entity.getPersistentData().getDouble("AI") + 1));
		}
		if (entity.getPersistentData().getDouble("AI") == 580) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < 60) {
				if (entity instanceof ShadowHunterEntity) {
					((ShadowHunterEntity) entity).setAnimation("animation.ShadowHunter.shield");
				}
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 6));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
			}
			entity.getPersistentData().putDouble("AI", 0);
		}
		if (!(entity instanceof LivingEntity _livEnt106 && _livEnt106.hasEffect(MobEffects.DARKNESS))) {
			if (entity instanceof Mob _mob ? _mob.isAggressive() : false) {
				if (entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D) {
					if (entity instanceof LivingEntity _livEnt109 && _livEnt109.hasEffect(MobEffects.MOVEMENT_SPEED)) {
						if (entity instanceof ShadowHunterEntity) {
							((ShadowHunterEntity) entity).setAnimation("animation.ShadowHunter.run");
						}
					}
					if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 1));
				}
			}
		}
		if (!world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 4.6, 4.6, 4.6), e -> true).isEmpty()) {
			if (!(entity instanceof LivingEntity _livEnt113 && _livEnt113.hasEffect(MobEffects.DARKNESS))) {
				if (entity instanceof Mob _mob ? _mob.isAggressive() : false) {
					if (entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D) {
						if (entity instanceof LivingEntity _livEnt116 && _livEnt116.hasEffect(MobEffects.MOVEMENT_SPEED)) {
							if (entity instanceof ShadowHunterEntity) {
								((ShadowHunterEntity) entity).setAnimation("animation.ShadowHunter.attack2");
							}
						}
						if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
							_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 1));
					}
				}
			}
		}
	}
}
