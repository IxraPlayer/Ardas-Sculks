
package sculk.of.ixra.procedures;

import sculk.of.ixra.entity.SculkCreeperEntity;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;

public class SculkCreeperOnEntityTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;

		boolean playerNearby = world instanceof Level level && hasNearbySurvivalPlayer(level, x, y, z, 7);
		if (entity instanceof SculkCreeperEntity sculkCreeper)
			sculkCreeper.setAnimation(playerNearby ? "bom" : "empty");

		boolean isSwelling = (entity instanceof Creeper creeper ? creeper.getSwellDir() : 0) == 1;

		if (!(entity instanceof LivingEntity livingEntity) || livingEntity.level().isClientSide())
			return;

		if (isSwelling) {
			livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 80, 2));
			livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 2));
		} else {
			livingEntity.removeAllEffects();
		}
	}

	/**
	 * Only Survival-mode players count as "nearby" for the tremble/shake reaction.
	 * Creative and Spectator players (and any other non-Survival mode) are ignored,
	 * both in singleplayer and multiplayer.
	 */
	private static boolean hasNearbySurvivalPlayer(Level level, double x, double y, double z, double range) {
		double rangeSq = range * range;
		for (Player player : level.players()) {
			if (player.isSpectator())
				continue;
			if (player.distanceToSqr(x, y, z) > rangeSq)
				continue;
			if (player instanceof ServerPlayer serverPlayer) {
				// Server side (authoritative): check the real game mode of this player.
				if (serverPlayer.gameMode.getGameModeForPlayer() == GameType.SURVIVAL)
					return true;
			} else {
				// Client side fallback (e.g. local player prediction): use the
				// instabuild ability flag as a stand-in for "not creative".
				// The server's own check above is what ultimately decides the
				// synced result, so this only affects one frame of local feedback.
				if (!player.getAbilities().instabuild)
					return true;
			}
		}
		return false;
	}
}
