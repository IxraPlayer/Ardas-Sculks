
package sculk.of.ixra.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SlabBlock;

public class SculkCrystalTilesSlabBlock extends SlabBlock {
	public SculkCrystalTilesSlabBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).strength(1f, 10f).requiresCorrectToolForDrops().dynamicShape());
	}
}
