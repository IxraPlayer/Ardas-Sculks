
package sculk.of.ixra.block;

import org.checkerframework.checker.units.qual.s;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

public class DeepslateLightBlock extends Block {
	public DeepslateLightBlock() {
		super(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.SCULK).strength(1.2f, 4.5f).lightLevel(s -> 10).requiresCorrectToolForDrops().hasPostProcess((bs, br, bp) -> true)
				.emissiveRendering((bs, br, bp) -> true));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}
}
