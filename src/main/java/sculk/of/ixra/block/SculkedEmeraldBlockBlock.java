
package sculk.of.ixra.block;

import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.util.FastColor;
import net.minecraft.core.BlockPos;

public class SculkedEmeraldBlockBlock extends Block {
	public SculkedEmeraldBlockBlock() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.LAPIS).sound(SoundType.NETHERITE_BLOCK).strength(2.3f, 19f).requiresCorrectToolForDrops());
	}

	@Override
	public Integer getBeaconColorMultiplier(BlockState state, LevelReader world, BlockPos pos, BlockPos beaconPos) {
		return FastColor.ARGB32.opaque(-16764058);
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}
}
