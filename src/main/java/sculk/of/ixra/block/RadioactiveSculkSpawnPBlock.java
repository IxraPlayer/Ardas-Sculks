
package sculk.of.ixra.block;

import sculk.of.ixra.procedures.RadioactiveSculkSpawnProductureProcedure;
import sculk.of.ixra.init.SculksOfArdaModBlocks;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class RadioactiveSculkSpawnPBlock extends FlowerBlock {
	public RadioactiveSculkSpawnPBlock() {
		super(MobEffects.MOVEMENT_SPEED, 100,
				BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).randomTicks().sound(SoundType.GRASS).strength(-1, 3600000).noCollission().offsetType(BlockBehaviour.OffsetType.XYZ).pushReaction(PushReaction.DESTROY));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 100;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 60;
	}

	@Override
	public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
		return groundState.is(SculksOfArdaModBlocks.RADIOACTIVE_SCULK_GRASS_BLOCK.get()) || groundState.is(SculksOfArdaModBlocks.RADIOACTIVE_SCULK.get());
	}

	@Override
	public boolean canSurvive(BlockState blockstate, LevelReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.below();
		BlockState groundState = worldIn.getBlockState(blockpos);
		return this.mayPlaceOn(groundState, worldIn, blockpos);
	}

	@Override
	public void randomTick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
		RadioactiveSculkSpawnProductureProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
	}
}
