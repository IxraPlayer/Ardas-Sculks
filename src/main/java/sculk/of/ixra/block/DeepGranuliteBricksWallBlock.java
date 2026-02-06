
package sculk.of.ixra.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.SoundType;

public class DeepGranuliteBricksWallBlock extends WallBlock {
	public DeepGranuliteBricksWallBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.BASALT).strength(6.75f, 20f).requiresCorrectToolForDrops().dynamicShape().forceSolidOn());
	}
}
