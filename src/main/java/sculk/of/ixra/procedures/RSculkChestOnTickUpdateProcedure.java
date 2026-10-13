package sculk.of.ixra.procedures;

import sculk.of.ixra.world.inventory.SculkChestGuiMenu;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import java.util.Comparator;

public class RSculkChestOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (nearestPlayerOpenedThisChest(world, x, y, z, 7)) {
			setAnimationState(world, x, y, z, 1);
		} else if (!nearestPlayerOpenedThisChest(world, x, y, z, 3)) {
			setAnimationState(world, x, y, z, 2);
		}
	}

	private static boolean nearestPlayerOpenedThisChest(LevelAccessor world, double x, double y, double z, double radius) {
		Entity nearest = world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), radius, radius, radius), e -> true)
				.stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(x, y, z))).findFirst().orElse(null);
		return nearest instanceof Player player && player.containerMenu instanceof SculkChestGuiMenu;
	}

	private static void setAnimationState(LevelAccessor world, double x, double y, double z, int value) {
		BlockPos pos = BlockPos.containing(x, y, z);
		BlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock().getStateDefinition().getProperty("animation") instanceof IntegerProperty integerProp
				&& integerProp.getPossibleValues().contains(value))
			world.setBlock(pos, blockState.setValue(integerProp, value), 3);
	}
}
