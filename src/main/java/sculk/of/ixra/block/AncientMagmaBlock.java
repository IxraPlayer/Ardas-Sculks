
package sculk.of.ixra.block;

import sculk.of.ixra.procedures.SculkCactusEntityCollidesInTheBlockProcedure;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

public class AncientMagmaBlock extends Block {
	public AncientMagmaBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.SHROOMLIGHT).strength(3.8f, 20f).requiresCorrectToolForDrops().hasPostProcess((bs, br, bp) -> true).emissiveRendering((bs, br, bp) -> true));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 11;
	}

	@Override
	public void stepOn(Level world, BlockPos pos, BlockState blockstate, Entity entity) {
		super.stepOn(world, pos, blockstate, entity);
		SculkCactusEntityCollidesInTheBlockProcedure.execute(world, entity);
	}
}
