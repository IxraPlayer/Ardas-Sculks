package sculk.of.ixra.procedures;

import net.minecraft.world.entity.Entity;

public class WardenSwordLivingEntityIsHitWithToolProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.push(0.3, 0.5, 0.3);
	}
}
