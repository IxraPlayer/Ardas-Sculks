
package sculk.of.ixra.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.SoundType;

public class SculkeriteDeepslateBricksWallBlock extends WallBlock {
	public SculkeriteDeepslateBricksWallBlock() {
		super(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.SCULK).strength(1.2f, 4.5f).requiresCorrectToolForDrops().dynamicShape().forceSolidOn());
	}
}
