
package sculk.of.ixra.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.SoundType;

public class CryingSacredObsidianStoneWallBlock extends WallBlock {
	public CryingSacredObsidianStoneWallBlock() {
		super(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.SCULK).strength(3f, 20f).dynamicShape().forceSolidOn());
	}
}
