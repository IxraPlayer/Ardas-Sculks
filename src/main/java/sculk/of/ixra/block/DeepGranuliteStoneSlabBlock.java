
package sculk.of.ixra.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SlabBlock;

public class DeepGranuliteStoneSlabBlock extends SlabBlock {
	public DeepGranuliteStoneSlabBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.BASALT).strength(6.65f, 20f).requiresCorrectToolForDrops().dynamicShape());
	}
}
