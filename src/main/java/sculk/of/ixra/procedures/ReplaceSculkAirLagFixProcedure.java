package sculk.of.ixra.procedures;

import sculk.of.ixra.init.SculksOfArdaModBlocks;
import sculk.of.ixra.SculksOfArdaMod;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class ReplaceSculkAirLagFixProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == Blocks.AIR) {
			world.setBlock(BlockPos.containing(x, y + 1, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 2, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 3, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 4, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 5, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 6, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 7, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 8, z), SculksOfArdaModBlocks.REPLACE_SCULK_AIR_NO_TICK_LIMIT.get().defaultBlockState(), 3);
			SculksOfArdaMod.queueServerWork(20, () -> {
				world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			});
		}
		if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == SculksOfArdaModBlocks.SACRED_OBSIDIAN_STONE.get()) {
			world.setBlock(BlockPos.containing(x, y + 1, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 2, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 3, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 4, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 5, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 6, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 7, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 8, z), SculksOfArdaModBlocks.REPLACE_SCULK_AIR_NO_TICK_LIMIT.get().defaultBlockState(), 3);
			SculksOfArdaMod.queueServerWork(20, () -> {
				world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			});
		}
		if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == Blocks.DEEPSLATE_TILES) {
			world.setBlock(BlockPos.containing(x, y + 1, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 2, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 3, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 4, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 5, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 6, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 7, z), Blocks.AIR.defaultBlockState(), 3);
			world.setBlock(BlockPos.containing(x, y + 8, z), SculksOfArdaModBlocks.REPLACE_SCULK_AIR_NO_TICK_LIMIT.get().defaultBlockState(), 3);
			SculksOfArdaMod.queueServerWork(20, () -> {
				world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
			});
		}
	}
}
