package sculk.of.ixra.bossheads;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

/** Boss trophy head hung on a wall. FACING is the direction the face looks at (the wall is behind it). */
public class BossWallHeadBlock extends Block implements EntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	private final BossHeadType type;

	public BossWallHeadBlock(BossHeadType type) {
		super(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(1.0f, 6.0f).noOcclusion().pushReaction(PushReaction.DESTROY));
		this.type = type;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	public BossHeadType getType() {
		return type;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BossHeadBlockEntity(pos, state);
	}

	/** the model is drawn by BossHeadBlockEntityRenderer (it animates), not by the chunk */
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return type.wallShape(state.getValue(FACING));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = this.defaultBlockState();
		BlockGetter level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		for (Direction direction : context.getNearestLookingDirections()) {
			if (direction.getAxis().isHorizontal()) {
				state = state.setValue(FACING, direction.getOpposite());
				if (!level.getBlockState(pos.relative(direction)).canBeReplaced(context))
					return state;
			}
		}
		return null;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}
}
