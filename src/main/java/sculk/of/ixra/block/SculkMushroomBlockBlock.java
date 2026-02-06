
package sculk.of.ixra.block;

import org.checkerframework.checker.units.qual.s;

import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;

public class SculkMushroomBlockBlock extends Block {
	public SculkMushroomBlockBlock() {
		super(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.ICE).sound(SoundType.SCULK).strength(0.35f, 3.5f).lightLevel(s -> 1));
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 15;
	}
}
