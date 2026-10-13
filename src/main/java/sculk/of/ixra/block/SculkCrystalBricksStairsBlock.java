
package sculk.of.ixra.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Blocks;

public class SculkCrystalBricksStairsBlock extends StairBlock {
	public SculkCrystalBricksStairsBlock() {
		super(Blocks.AIR.defaultBlockState(), BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).strength(1f, 10f).requiresCorrectToolForDrops().dynamicShape());
	}

	@Override
	public float getExplosionResistance() {
		return 10f;
	}
}
