
package sculk.of.ixra.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.SoundType;

public class SculkCrystalTilesWallBlock extends WallBlock {
	public SculkCrystalTilesWallBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).strength(1f, 10f).requiresCorrectToolForDrops().dynamicShape().forceSolidOn());
	}
}
