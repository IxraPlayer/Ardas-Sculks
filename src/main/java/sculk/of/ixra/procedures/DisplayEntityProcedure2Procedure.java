package sculk.of.ixra.procedures;

import sculk.of.ixra.init.SculksOfArdaModEntities;
import sculk.of.ixra.entity.SculkSkeletonEntity;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;

public class DisplayEntityProcedure2Procedure {
	public static Entity execute(LevelAccessor world) {
		return world instanceof Level _level ? new SculkSkeletonEntity(SculksOfArdaModEntities.SCULK_SKELETON.get(), _level) : null;
	}
}
