package sculk.of.ixra.procedures;

import sculk.of.ixra.init.SculksOfArdaModEntities;
import sculk.of.ixra.entity.SculkCreeperEntity;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;

public class DisplayEntityProcedureProcedure {
	public static Entity execute(LevelAccessor world) {
		return world instanceof Level _level ? new SculkCreeperEntity(SculksOfArdaModEntities.SCULK_CREEPER.get(), _level) : null;
	}
}
