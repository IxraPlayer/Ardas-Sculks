
package sculk.of.ixra.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Blocks;

public class WisteryaRockStairsBlock extends StairBlock {
	public WisteryaRockStairsBlock() {
		super(Blocks.AIR.defaultBlockState(), BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(1.1f, 11f).requiresCorrectToolForDrops().dynamicShape());
	}

	@Override
	public float getExplosionResistance() {
		return 11f;
	}
}
