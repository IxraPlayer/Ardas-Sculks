
package sculk.of.ixra.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Blocks;

public class SacredObsidianStoneStairsBlock extends StairBlock {
	public SacredObsidianStoneStairsBlock() {
		super(Blocks.AIR.defaultBlockState(), BlockBehaviour.Properties.of().sound(SoundType.SCULK).strength(2.2f, 20f).dynamicShape());
	}

	@Override
	public float getExplosionResistance() {
		return 20f;
	}
}
