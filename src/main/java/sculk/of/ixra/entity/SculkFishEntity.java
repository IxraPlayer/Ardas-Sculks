
package sculk.of.ixra.entity;

import sculk.of.ixra.procedures.SculkFishOnEntityTickUpdateProcedure;
import sculk.of.ixra.init.SculksOfArdaModItems;
import sculk.of.ixra.init.SculksOfArdaModEntities;

import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.common.NeoForgeMod;

import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;

public class SculkFishEntity extends PathfinderMob {
	public SculkFishEntity(EntityType<SculkFishEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(false);
		this.setPathfindingMalus(PathType.WATER, 0);
		this.moveControl = new MoveControl(this) {
			@Override
			public void tick() {
				if (SculkFishEntity.this.isInWater())
					SculkFishEntity.this.setDeltaMovement(SculkFishEntity.this.getDeltaMovement().add(0, 0.005, 0));
				if (this.operation == MoveControl.Operation.MOVE_TO && !SculkFishEntity.this.getNavigation().isDone()) {
					double dx = this.wantedX - SculkFishEntity.this.getX();
					double dy = this.wantedY - SculkFishEntity.this.getY();
					double dz = this.wantedZ - SculkFishEntity.this.getZ();
					float f = (float) (Mth.atan2(dz, dx) * (double) (180 / Math.PI)) - 90;
					float f1 = (float) (this.speedModifier * SculkFishEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
					SculkFishEntity.this.setYRot(this.rotlerp(SculkFishEntity.this.getYRot(), f, 10));
					SculkFishEntity.this.yBodyRot = SculkFishEntity.this.getYRot();
					SculkFishEntity.this.yHeadRot = SculkFishEntity.this.getYRot();
					if (SculkFishEntity.this.isInWater()) {
						SculkFishEntity.this.setSpeed((float) SculkFishEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
						float f2 = -(float) (Mth.atan2(dy, (float) Math.sqrt(dx * dx + dz * dz)) * (180 / Math.PI));
						f2 = Mth.clamp(Mth.wrapDegrees(f2), -85, 85);
						SculkFishEntity.this.setXRot(this.rotlerp(SculkFishEntity.this.getXRot(), f2, 5));
						float f3 = Mth.cos(SculkFishEntity.this.getXRot() * (float) (Math.PI / 180.0));
						SculkFishEntity.this.setZza(f3 * f1);
						SculkFishEntity.this.setYya((float) (f1 * dy));
					} else {
						SculkFishEntity.this.setSpeed(f1 * 0.05F);
					}
				} else {
					SculkFishEntity.this.setSpeed(0);
					SculkFishEntity.this.setYya(0);
					SculkFishEntity.this.setZza(0);
				}
			}
		};
	}

	@Override
	protected PathNavigation createNavigation(Level world) {
		return new WaterBoundPathNavigation(this, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new RandomSwimmingGoal(this, 5.5, 40));
		this.goalSelector.addGoal(2, new PanicGoal(this, 6.5));
		this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8));
	}

	protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource source, boolean recentlyHitIn) {
		super.dropCustomDeathLoot(serverLevel, source, recentlyHitIn);
		this.spawnAtLocation(new ItemStack(SculksOfArdaModItems.ANCIENTNAUTILUSSHELL.get()));
	}

	@Override
	public void baseTick() {
		super.baseTick();
		SculkFishOnEntityTickUpdateProcedure.execute(this.level(), this);
	}

	@Override
	public boolean checkSpawnObstruction(LevelReader world) {
		return world.isUnobstructed(this);
	}

	@Override
	public boolean canDrownInFluidType(FluidType type) {
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		Level world = this.level();
		Entity entity = this;
		return false;
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
		event.register(SculksOfArdaModEntities.SCULK_FISH.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> (world.getBlockState(pos).is(Blocks.WATER) && world.getBlockState(pos.above()).is(Blocks.WATER)), RegisterSpawnPlacementsEvent.Operation.REPLACE);
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 10);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
		builder = builder.add(NeoForgeMod.SWIM_SPEED, 0.3);
		return builder;
	}
}
