package sculk.of.ixra.bossheads;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
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
import net.minecraft.core.BlockPos;

/** Boss trophy head standing on the floor. 16 rotation steps, like a vanilla skull. */
public class BossHeadBlock extends Block implements EntityBlock {
	public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
	private final BossHeadType type;

	public BossHeadBlock(BossHeadType type) {
		super(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(1.0f, 6.0f).noOcclusion().pushReaction(PushReaction.DESTROY));
		this.type = type;
		this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, 0));
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
		return type.floorShape();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(ROTATION);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(ROTATION, RotationSegment.convertToSegment(context.getRotation()));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(ROTATION, rot.rotate(state.getValue(ROTATION), 16));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(ROTATION, mirror.mirror(state.getValue(ROTATION), 16));
	}
}
