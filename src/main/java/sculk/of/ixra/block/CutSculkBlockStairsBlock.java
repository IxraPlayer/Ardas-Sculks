
package sculk.of.ixra.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Blocks;

public class CutSculkBlockStairsBlock extends StairBlock {
	public CutSculkBlockStairsBlock() {
		super(Blocks.AIR.defaultBlockState(), BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.SCULK).strength(0.45f, 4.5f).dynamicShape());
	}

	@Override
	public float getExplosionResistance() {
		return 4.5f;
	}
}
