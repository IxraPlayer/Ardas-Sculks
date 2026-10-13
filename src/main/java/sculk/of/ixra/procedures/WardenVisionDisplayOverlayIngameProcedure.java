package sculk.of.ixra.procedures;

import sculk.of.ixra.init.SculksOfArdaModMobEffects;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

public class WardenVisionDisplayOverlayIngameProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		return entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(SculksOfArdaModMobEffects.WARDEN_POWER);
	}
}
