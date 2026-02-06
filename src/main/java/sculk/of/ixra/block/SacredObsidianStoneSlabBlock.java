
package sculk.of.ixra.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SlabBlock;

public class SacredObsidianStoneSlabBlock extends SlabBlock {
	public SacredObsidianStoneSlabBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.SCULK).strength(2.2f, 20f).dynamicShape());
	}
}
