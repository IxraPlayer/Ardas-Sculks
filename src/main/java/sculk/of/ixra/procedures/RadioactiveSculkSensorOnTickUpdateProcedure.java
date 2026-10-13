package sculk.of.ixra.procedures;

import sculk.of.ixra.SculksOfArdaMod;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.BlockPos;

public class RadioactiveSculkSensorOnTickUpdateProcedure {

	public static boolean execute(LevelAccessor world, double x, double y, double z) {
		if (!(world instanceof Level level))
			return false;

		Player nearbyPlayer = level.getNearestPlayer(x, y, z, 8, false);

		boolean healthExactly8 = nearbyPlayer != null && nearbyPlayer.getHealth() == 8;
		boolean hasPoison = nearbyPlayer != null && nearbyPlayer.hasEffect(MobEffects.POISON);
		if (healthExactly8 || hasPoison) {
			return nearbyPlayer != null;
		}

		if (nearbyPlayer != null) {
			setBlockState(world, x, y, z, 1);
			if (!level.isClientSide())
				nearbyPlayer.addEffect(new MobEffectInstance(MobEffects.POISON, 70, 1, false, false));
		}

		boolean stillPoisoned = nearbyPlayer != null && nearbyPlayer.hasEffect(MobEffects.POISON);
		if (!stillPoisoned) {
			SculksOfArdaMod.queueServerWork(1, () -> setBlockState(world, x, y, z, 0));
		}

		return nearbyPlayer != null;
	}

	private static void setBlockState(LevelAccessor world, double x, double y, double z, int value) {
		BlockPos pos = BlockPos.containing(x, y, z);
		BlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty integerProp
				&& integerProp.getPossibleValues().contains(value))
			world.setBlock(pos, blockState.setValue(integerProp, value), 3);
	}
}
