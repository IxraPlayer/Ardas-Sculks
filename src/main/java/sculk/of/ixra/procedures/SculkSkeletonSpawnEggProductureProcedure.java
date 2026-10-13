package sculk.of.ixra.procedures;

import sculk.of.ixra.init.SculksOfArdaModItems;
import sculk.of.ixra.init.SculksOfArdaModEntities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.client.Minecraft;

public class SculkSkeletonSpawnEggProductureProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (isSurvivalPlayer(entity)) {
			if (entity instanceof Player _player) {
				ItemStack _stktoremove = new ItemStack(SculksOfArdaModItems.SCULK_SKELETON_SPAWN_EGG.get());
				_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
			}
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = SculksOfArdaModEntities.SCULK_SKELETON.get().spawn(_level, BlockPos.containing(x, y + 1, z), MobSpawnType.MOB_SUMMONED);
			}
		} else {
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = SculksOfArdaModEntities.SCULK_SKELETON.get().spawn(_level, BlockPos.containing(x, y + 1, z), MobSpawnType.MOB_SUMMONED);
			}
		}
	}
	private static boolean isSurvivalPlayer(Entity entity) {
		if (entity instanceof ServerPlayer serverPlayer) {
			return serverPlayer.gameMode.getGameModeForPlayer() == GameType.SURVIVAL;
		} else if (entity.level().isClientSide() && entity instanceof Player player) {
			return Minecraft.getInstance().getConnection().getPlayerInfo(player.getGameProfile().getId()) != null
			&& Minecraft.getInstance().getConnection().getPlayerInfo(player.getGameProfile().getId()).getGameMode() == GameType.SURVIVAL;
		}
		return false;
	}
}
