
package sculk.of.ixra.block;

import sculk.of.ixra.procedures.RadioactiveSculkSensorOnTickUpdateProcedure;

import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import java.util.List;

public class RadioactiveSculkSensorBlock extends Block {
	public static final IntegerProperty BLOCKSTATE = IntegerProperty.create("blockstate", 0, 1);
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	private static final int ACTIVE_INTERVAL = 4;
	private static final int IDLE_INTERVAL = 20;
	private static final double IDLE_CHECK_RADIUS = 9;

	public RadioactiveSculkSensorBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.SCULK).strength(0.4f, 7f).lightLevel(s -> s.getValue(BLOCKSTATE) == 1 ? 12 : 0).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, context, list, flag);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (state.getValue(BLOCKSTATE) == 1) {
			return switch (state.getValue(FACING)) {
				default -> box(0, 0, 0, 16, 8, 16);
				case NORTH -> box(0, 0, 0, 16, 8, 16);
				case EAST -> box(0, 0, 0, 16, 8, 16);
				case WEST -> box(0, 0, 0, 16, 8, 16);
			};
		}
		return switch (state.getValue(FACING)) {
			default -> box(0, 0, 0, 16, 8, 16);
			case NORTH -> box(0, 0, 0, 16, 8, 16);
			case EAST -> box(0, 0, 0, 16, 8, 16);
			case WEST -> box(0, 0, 0, 16, 8, 16);
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, BLOCKSTATE);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(blockstate, world, pos, oldState, moving);
		world.scheduleTick(pos, this, ACTIVE_INTERVAL);
	}

	@Override
	public void tick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
		super.tick(blockstate, world, pos, random);

		boolean anyoneNear = world.hasNearbyAlivePlayer(pos.getX(), pos.getY(), pos.getZ(), IDLE_CHECK_RADIUS);
		if (!anyoneNear) {
			world.scheduleTick(pos, this, IDLE_INTERVAL);
			return;
		}

		boolean stillActive = RadioactiveSculkSensorOnTickUpdateProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
		world.scheduleTick(pos, this, stillActive ? ACTIVE_INTERVAL : IDLE_INTERVAL);
	}
}
