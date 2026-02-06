
package sculk.of.ixra.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.SoundType;

public class SacredObsidianStoneWallBlock extends WallBlock {
	public SacredObsidianStoneWallBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.SCULK).strength(2.2f, 20f).dynamicShape().forceSolidOn());
	}
}
