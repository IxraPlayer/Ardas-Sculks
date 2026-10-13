package sculk.of.ixra.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;

public class SculkSpawnPOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		world.setBlock(BlockPos.containing(x, y, z), Blocks.SCULK_SENSOR.defaultBlockState(), 3);
	}
}
