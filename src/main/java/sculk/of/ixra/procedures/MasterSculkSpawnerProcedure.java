package sculk.of.ixra.procedures;

import sculk.of.ixra.init.SculksOfArdaModEntities;
import sculk.of.ixra.init.SculksOfArdaModBlocks;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.ChatFormatting;

import java.util.Timer;
import java.util.TimerTask;

public class MasterSculkSpawnerProcedure {

    public static void execute(Level world, double x, double y, double z, Entity entity) {
        onRightClick(world, x, y, z, entity);
    }

    public static void onRightClick(Level world, double x, double y, double z, Entity entity) {
        if (world.isClientSide() || !(entity instanceof Player player)) return;

        BlockPos pos = BlockPos.containing(x, y, z);
        var blockState = world.getBlockState(pos);
        EntityType<?> typeToSpawn = null;
        boolean isRandom = false;

        if (blockState.getBlock() == SculksOfArdaModBlocks.SCULK_GOLEM_SPAWNER.get()) {
            typeToSpawn = SculksOfArdaModEntities.SCULK_BOSS_1_T.get();
        } else if (blockState.getBlock() == SculksOfArdaModBlocks.RADIOACTIVE_WARDEN_SPAWNER.get()) {
            typeToSpawn = SculksOfArdaModEntities.RADIOACTIVE_WARDEN.get();
        } else if (blockState.getBlock() == SculksOfArdaModBlocks.SHADOW_HUNTER_SPAWNER.get()) {
            typeToSpawn = SculksOfArdaModEntities.SHADOW_HUNTER.get();
        } else if (blockState.getBlock() == SculksOfArdaModBlocks.SCULK_MOB_SPAWNER.get()) {
            isRandom = true;
        }

        if (typeToSpawn == null && !isRandom) return;

        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        world.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 3);
        world.setBlock(pos.below(), Blocks.AIR.defaultBlockState(), 3);

        startCountdown((ServerLevel) world, pos, player, typeToSpawn, isRandom);
    }

    private static void startCountdown(ServerLevel world, BlockPos pos, Player player, EntityType<?> type, boolean isRandom) {
        final EntityType<?> finalType = type;
        final boolean finalIsRandom = isRandom;
        
        for (int i = 0; i <= 15; i++) {
            final int timeLeft = 15 - i;
            
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    world.getServer().execute(() -> {
                        if (timeLeft > 0) {
                            
                            player.displayClientMessage(Component.literal("Critical Awakening: " + timeLeft + "s")
                                    .withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.BOLD), true);
                            
                            
                        } else {
                            
                            player.displayClientMessage(Component.literal("BOSS AWAKENED!")
                                    .withStyle(ChatFormatting.DARK_AQUA).withStyle(ChatFormatting.BOLD), true);

                            if (finalIsRandom) {
                                spawnRandomMobs(world, pos);
                            } else {
                                spawnMob(world, finalType, pos);
                            }
                            
                            world.playSound(null, pos, SoundEvents.WITHER_SPAWN, SoundSource.BLOCKS, 1.0f, 1.0f);
                        }
                    });
                }
            }, i * 1000);
        }
    }

    private static void spawnMob(ServerLevel level, EntityType<?> type, BlockPos pos) {
        Entity entity = type.spawn(level, pos, MobSpawnType.MOB_SUMMONED);
        if (entity != null) {
            entity.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        }
    }

    private static void spawnRandomMobs(ServerLevel level, BlockPos pos) {
        int chance = (int) (Math.random() * 3) + 1;
        if (chance == 1) {
            spawnMob(level, SculksOfArdaModEntities.SCULK_SKELETON.get(), pos);
            spawnMob(level, SculksOfArdaModEntities.SCULK_FOX.get(), pos);
        } else if (chance == 2) {
            spawnMob(level, SculksOfArdaModEntities.SCULK_CREAKING.get(), pos);
        } else {
            spawnMob(level, SculksOfArdaModEntities.AGILE_SLIME.get(), pos);
        }
    }
}