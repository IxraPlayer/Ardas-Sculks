package sculk.of.ixra.procedures;

import sculk.of.ixra.init.SculksOfArdaModBlocks;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class SoulSculkWSpawnOntickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		world.setBlock(BlockPos.containing(x, y, z), SculksOfArdaModBlocks.SOUL_SCULK_SENSOR.get().defaultBlockState(), 3);
	}
}
