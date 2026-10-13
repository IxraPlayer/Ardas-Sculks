package sculk.of.ixra.procedures;

import sculk.of.ixra.init.SculksOfArdaModEntities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.Difficulty;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SculkMobSpawnerProductureProcedure {

	public static boolean execute(LevelAccessor world, double x, double y, double z, RandomSource random) {
		if (world.getDifficulty() == Difficulty.PEACEFUL)
			return false;

		BlockPos pos = BlockPos.containing(x, y, z);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null && blockEntity.getPersistentData().getBoolean("boss"))
			return false;

		if (!(world instanceof Level level))
			return false;

		Player nearestPlayer = level.getNearestPlayer(x, y, z, 10, false);
		if (nearestPlayer == null)
			return false;

		if (Mth.nextInt(random, 1, 5) == 2) {
			fireSpawner(world, pos, SculksOfArdaModEntities.SCULK_SKELETON, SculksOfArdaModEntities.SCULK_SKELETON,
					SculksOfArdaModEntities.SCULK_FOX);
			return true;
		}
		if (Mth.nextInt(random, 1, 5) == 2) {
			fireSpawner(world, pos, SculksOfArdaModEntities.SCULK_CREAKING, SculksOfArdaModEntities.SCULK_CREAKING);
			return true;
		}
		if (Mth.nextInt(random, 1, 5) == 2) {
			fireSpawner(world, pos, SculksOfArdaModEntities.AGILE_SLIME, SculksOfArdaModEntities.AGILE_SLIME,
					SculksOfArdaModEntities.SCULK_FOX, SculksOfArdaModEntities.SCULK_SKELETON);
			return true;
		}

		return true;
	}

	@SafeVarargs
	private static void fireSpawner(LevelAccessor world, BlockPos pos,
			DeferredHolder<EntityType<?>, ? extends EntityType<?>>... entityTypes) {
		if (!world.isClientSide()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			BlockState blockState = world.getBlockState(pos);
			if (blockEntity != null)
				blockEntity.getPersistentData().putBoolean("boss", true);
			if (world instanceof Level level)
				level.sendBlockUpdated(pos, blockState, blockState, 3);
		}

		if (world instanceof ServerLevel serverLevel) {
			for (DeferredHolder<EntityType<?>, ? extends EntityType<?>> entityType : entityTypes) {
				Entity entityToSpawn = entityType.get().spawn(serverLevel, pos, MobSpawnType.MOB_SUMMONED);
				if (entityToSpawn != null)
					entityToSpawn.setDeltaMovement(0, 0, 0);
			}
		}

		world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
		world.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 3);
		world.setBlock(pos.below(), Blocks.AIR.defaultBlockState(), 3);
	}
}
