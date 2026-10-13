
package sculk.of.ixra.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

public class SacredObsidianStoneBlock extends Block {
	public SacredObsidianStoneBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.SCULK).strength(2.2f, 20f));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}
}
