package sculk.of.ixra.bossheads;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

/** Holds nothing; it only exists so the placed head can be drawn (and animated) by BossHeadBlockEntityRenderer. */
public class BossHeadBlockEntity extends BlockEntity {
	public BossHeadBlockEntity(BlockPos pos, BlockState state) {
		super(BossHeadRegistry.blockEntityType(), pos, state);
	}
}
