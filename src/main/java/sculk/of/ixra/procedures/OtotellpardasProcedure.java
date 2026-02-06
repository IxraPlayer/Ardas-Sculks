package sculk.of.ixra.procedures;

import sculk.of.ixra.SculksOfArdaMod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

public class OtotellpardasProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (world instanceof ServerLevel _currentLevel) {
			Entity _entity = entity;
			ServerLevel _nextLevel = _currentLevel.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("sculks_of_arda:sculk_world")));
			if (_nextLevel != null && !_nextLevel.dimension().equals(_currentLevel.dimension()) && _entity.canChangeDimensions(_currentLevel, _nextLevel)) {
				DimensionTransition.PostDimensionTransition _postDimensionTransition = new DimensionTransition.PostDimensionTransition() {
					@Override
					public void onTransition(Entity nextEntity) {
						if (nextEntity instanceof ServerPlayer serverPlayer) {
							serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0.0F));
							serverPlayer.teleportTo(_nextLevel, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), serverPlayer.getYRot(), serverPlayer.getXRot());
							this.execute(_nextLevel, serverPlayer);
							serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(serverPlayer.getAbilities()));
							for (MobEffectInstance mobEffectInstance : serverPlayer.getActiveEffects())
								serverPlayer.connection.send(new ClientboundUpdateMobEffectPacket(serverPlayer.getId(), mobEffectInstance, false));
							serverPlayer.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
						} else {
							this.execute(nextEntity.level(), nextEntity);
						}
					}

					public void execute(LevelAccessor world, Entity entityinstance) {
						SculksOfArdaMod.queueServerWork(200, () -> {
							if (world instanceof ServerLevel _level)
								_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
										"locate biome sculks_of_arda:black_birch_forest");
						});
					}
				};
				if (_entity instanceof ServerPlayer _serverPlayer) {
					_postDimensionTransition.onTransition(_serverPlayer);
				} else {
					_entity.changeDimension(new DimensionTransition(_nextLevel, _entity.position(), Vec3.ZERO, _entity.getYRot(), _entity.getXRot(), true, _postDimensionTransition));
				}
			}
		}
	}
}
