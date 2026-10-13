package sculk.of.ixra.procedures;

import sculk.of.ixra.SculksOfArdaMod;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.BlockPos;

public class SoulSculkSensorOnTickUpdateProcedure {

	public static boolean execute(LevelAccessor world, double x, double y, double z) {
		if (!(world instanceof Level level))
			return false;

		Player nearbyPlayer = level.getNearestPlayer(x, y, z, 8, false);
		if (nearbyPlayer != null) {
			setBlockState(world, x, y, z, 1);
			if (!level.isClientSide())
				nearbyPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 70, 1, false, false));
		}

		boolean stillSlowed = nearbyPlayer != null && nearbyPlayer.hasEffect(MobEffects.MOVEMENT_SLOWDOWN);
		if (!stillSlowed) {
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
