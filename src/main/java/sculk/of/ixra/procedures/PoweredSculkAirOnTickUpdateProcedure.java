package sculk.of.ixra.procedures;

import sculk.of.ixra.init.SculksOfArdaModBlocks;
import sculk.of.ixra.SculksOfArdaMod;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class PoweredSculkAirOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
		SculksOfArdaMod.queueServerWork(20, () -> {
			world.setBlock(BlockPos.containing(x, y, z), SculksOfArdaModBlocks.POWERED_SCULK_AIR_NOT_TICK_LIMIT.get().defaultBlockState(), 3);
		});
	}
}
