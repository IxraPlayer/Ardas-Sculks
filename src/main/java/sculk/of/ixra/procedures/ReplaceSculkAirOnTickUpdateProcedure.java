package sculk.of.ixra.procedures;

import sculk.of.ixra.init.SculksOfArdaModBlocks;
import sculk.of.ixra.SculksOfArdaMod;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class ReplaceSculkAirOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		world.setBlock(BlockPos.containing(x, y + 1, z), SculksOfArdaModBlocks.REPLACE_SCULK_AIR.get().defaultBlockState(), 3);
		world.setBlock(BlockPos.containing(x, y + 2, z), SculksOfArdaModBlocks.REPLACE_SCULK_AIR.get().defaultBlockState(), 3);
		SculksOfArdaMod.queueServerWork(200, () -> {
			world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
		});
	}
}
