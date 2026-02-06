package sculk.of.ixra.procedures;

import sculk.of.ixra.SculksOfArdaMod;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;

public class SculkFishOnEntityTickUpdateProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (!entity.isInWater()) {
			if (entity.getPersistentData().getBoolean("pause") == true) {
				SculksOfArdaMod.queueServerWork(20, () -> {
					entity.getPersistentData().putBoolean("pause", false);
				});
			} else if (entity.getPersistentData().getBoolean("pause") == false) {
				SculksOfArdaMod.queueServerWork(20, () -> {
					entity.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), 1);
					entity.getPersistentData().putBoolean("pause", true);
				});
			}
		}
	}
}
