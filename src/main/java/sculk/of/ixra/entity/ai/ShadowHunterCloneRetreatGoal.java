package sculk.of.ixra.entity.ai;

import sculk.of.ixra.entity.ShadowHunterEntity;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;

import java.util.EnumSet;

/**
 * Klon modundaki "vur-kac" kismi: 2-4 sn boyunca oyuncunun TERSINE gider ama yuzu oyuncuya donuk kalir.
 * ShadowHunterChaseGoal'e dokunmaz; sadece kacis aktifken (yuksek oncelikle) hareketi ustlenir.
 * Kacis bitince bu goal biter ve ChaseGoal normal sekilde geri devralir.
 */
public class ShadowHunterCloneRetreatGoal extends Goal {
	private final ShadowHunterEntity mob;

	public ShadowHunterCloneRetreatGoal(ShadowHunterEntity mob) {
		this.mob = mob;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return this.mob.isCloneRetreating() && this.mob.getTarget() != null;
	}

	@Override
	public boolean canContinueToUse() {
		return this.canUse();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void start() {
		this.mob.tryDetach();
		this.mob.getNavigation().stop();
	}

	@Override
	public void stop() {
		this.mob.getNavigation().stop();
		this.mob.setZza(0.0F);
	}

	@Override
	public void tick() {
		LivingEntity target = this.mob.getTarget();
		if (target == null)
			return;
		double dx = this.mob.getX() - target.getX();
		double dz = this.mob.getZ() - target.getZ();
		double len = Math.sqrt(dx * dx + dz * dz);
		if (len < 1.0E-4D) {
			dx = 1.0D;
			dz = 0.0D;
			len = 1.0D;
		}
		double speed = Math.max(0.3D, Math.min(this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED) * 1.25D, 0.9D));
		Vec3 cur = this.mob.getDeltaMovement();
		this.mob.setDeltaMovement(dx / len * speed, cur.y, dz / len * speed);
		if (this.mob.horizontalCollision && this.mob.onGround())
			this.mob.getJumpControl().jump();

		// Yuz oyuncuya donuk kalir (geri geri kacis).
		float yaw = (float) (Mth.atan2(-dz, -dx) * (180.0D / Math.PI)) - 90.0F;
		this.mob.setYRot(yaw);
		this.mob.setYBodyRot(yaw);
		this.mob.setYHeadRot(yaw);
	}
}
