package sculk.of.ixra.procedures;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class SculkeriteHammerToolInHandTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double i = 0;
		double j = 0;
		String tag = "";
		tag = "mineable/pickaxe";
		i = -1;
		for (int index0 = 0; index0 < 3; index0++) {
			j = -1;
			for (int index1 = 0; index1 < 3; index1++) {
				if (i != 0 || j != 0) {
					if (entity.getXRot() > 40 || entity.getXRot() < -40) {
						if ((world.getBlockState(BlockPos.containing(x + i, y, z + j))).is(BlockTags.create(ResourceLocation.parse((tag).toLowerCase(java.util.Locale.ENGLISH))))) {
							{
								BlockPos _pos = BlockPos.containing(x + i, y, z + j);
								Block.dropResources(world.getBlockState(_pos), world, BlockPos.containing(x, y, z), null);
								world.destroyBlock(_pos, false);
							}
						}
					} else if ((entity.getDirection()).getAxis() == Direction.Axis.Z) {
						if ((world.getBlockState(BlockPos.containing(x + i, y + j, z))).is(BlockTags.create(ResourceLocation.parse((tag).toLowerCase(java.util.Locale.ENGLISH))))) {
							{
								BlockPos _pos = BlockPos.containing(x + i, y + j, z);
								Block.dropResources(world.getBlockState(_pos), world, BlockPos.containing(x, y, z), null);
								world.destroyBlock(_pos, false);
							}
						}
					} else if ((entity.getDirection()).getAxis() == Direction.Axis.X) {
						if ((world.getBlockState(BlockPos.containing(x, y + j, z + i))).is(BlockTags.create(ResourceLocation.parse((tag).toLowerCase(java.util.Locale.ENGLISH))))) {
							{
								BlockPos _pos = BlockPos.containing(x, y + j, z + i);
								Block.dropResources(world.getBlockState(_pos), world, BlockPos.containing(x, y, z), null);
								world.destroyBlock(_pos, false);
							}
						}
					}
				}
				j = j + 1;
			}
			i = i + 1;
		}
	}
}
