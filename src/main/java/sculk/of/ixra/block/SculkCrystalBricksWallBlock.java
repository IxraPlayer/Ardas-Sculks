
package sculk.of.ixra.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.SoundType;

public class SculkCrystalBricksWallBlock extends WallBlock {
	public SculkCrystalBricksWallBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).strength(1f, 10f).requiresCorrectToolForDrops().dynamicShape().forceSolidOn());
	}
}
