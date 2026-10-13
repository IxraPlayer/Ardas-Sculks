
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package sculk.of.ixra.init;

import sculk.of.ixra.entity.TinyAgileSlimeEntity;
import sculk.of.ixra.entity.ShadowHunterEntity;
import sculk.of.ixra.entity.SculkSkeletonEntity;
import sculk.of.ixra.entity.SculkGhostEntity;
import sculk.of.ixra.entity.SculkFoxEntity;
import sculk.of.ixra.entity.SculkFishEntity;
import sculk.of.ixra.entity.SculkEndermanEntity;
import sculk.of.ixra.entity.SculkCreeperEntity;
import sculk.of.ixra.entity.SculkCreakingEntity;
import sculk.of.ixra.entity.SculkBoss1TEntity;
import sculk.of.ixra.entity.SculkArrowEntity;
import sculk.of.ixra.entity.RadioactiveWardenEntity;
import sculk.of.ixra.entity.AgileSlimeEntity;
import sculk.of.ixra.SculksOfArdaMod;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.registries.Registries;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class SculksOfArdaModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, SculksOfArdaMod.MODID);
	public static final DeferredHolder<EntityType<?>, EntityType<SculkFoxEntity>> SCULK_FOX = register("sculk_fox",
			EntityType.Builder.<SculkFoxEntity>of(SculkFoxEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.sized(0.5f, 0.6f));
	public static final DeferredHolder<EntityType<?>, EntityType<SculkBoss1TEntity>> SCULK_BOSS_1_T = register("sculk_boss_1_t",
			EntityType.Builder.<SculkBoss1TEntity>of(SculkBoss1TEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<SculkArrowEntity>> SCULK_ARROW = register("sculk_arrow",
			EntityType.Builder.<SculkArrowEntity>of(SculkArrowEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<SculkCreeperEntity>> SCULK_CREEPER = register("sculk_creeper",
			EntityType.Builder.<SculkCreeperEntity>of(SculkCreeperEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(0.6f, 1.7f));
	public static final DeferredHolder<EntityType<?>, EntityType<SculkSkeletonEntity>> SCULK_SKELETON = register("sculk_skeleton",
			EntityType.Builder.<SculkSkeletonEntity>of(SculkSkeletonEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<SculkCreakingEntity>> SCULK_CREAKING = register("sculk_creaking",
			EntityType.Builder.<SculkCreakingEntity>of(SculkCreakingEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(0.6f, 2.4f));
	public static final DeferredHolder<EntityType<?>, EntityType<SculkFishEntity>> SCULK_FISH = register("sculk_fish",
			EntityType.Builder.<SculkFishEntity>of(SculkFishEntity::new, MobCategory.WATER_CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.sized(0.7f, 0.4f));
	public static final DeferredHolder<EntityType<?>, EntityType<SculkGhostEntity>> SCULK_GHOST = register("sculk_ghost",
			EntityType.Builder.<SculkGhostEntity>of(SculkGhostEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<AgileSlimeEntity>> AGILE_SLIME = register("agile_slime",
			EntityType.Builder.<AgileSlimeEntity>of(AgileSlimeEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.sized(1f, 1f));
	public static final DeferredHolder<EntityType<?>, EntityType<RadioactiveWardenEntity>> RADIOACTIVE_WARDEN = register("radioactive_warden",
			EntityType.Builder.<RadioactiveWardenEntity>of(RadioactiveWardenEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(150).setUpdateInterval(3).fireImmune().sized(1.1f, 3.4f));
	public static final DeferredHolder<EntityType<?>, EntityType<TinyAgileSlimeEntity>> TINY_AGILE_SLIME = register("tiny_agile_slime",
			EntityType.Builder.<TinyAgileSlimeEntity>of(TinyAgileSlimeEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)

					.sized(1f, 1f));
	public static final DeferredHolder<EntityType<?>, EntityType<ShadowHunterEntity>> SHADOW_HUNTER = register("shadow_hunter",
			EntityType.Builder.<ShadowHunterEntity>of(ShadowHunterEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(1000).setUpdateInterval(3).fireImmune().sized(0.8f, 3.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<SculkEndermanEntity>> SCULK_ENDERMAN = register("sculk_enderman",
			EntityType.Builder.<SculkEndermanEntity>of(SculkEndermanEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(0.6f, 3f));

	// Start of user code block custom entities
	// End of user code block custom entities
	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(RegisterSpawnPlacementsEvent event) {
		SculkFoxEntity.init(event);
		SculkBoss1TEntity.init(event);
		SculkCreeperEntity.init(event);
		SculkSkeletonEntity.init(event);
		SculkCreakingEntity.init(event);
		SculkFishEntity.init(event);
		SculkGhostEntity.init(event);
		AgileSlimeEntity.init(event);
		RadioactiveWardenEntity.init(event);
		TinyAgileSlimeEntity.init(event);
		ShadowHunterEntity.init(event);
		SculkEndermanEntity.init(event);
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(SCULK_FOX.get(), SculkFoxEntity.createAttributes().build());
		event.put(SCULK_BOSS_1_T.get(), SculkBoss1TEntity.createAttributes().build());
		event.put(SCULK_CREEPER.get(), SculkCreeperEntity.createAttributes().build());
		event.put(SCULK_SKELETON.get(), SculkSkeletonEntity.createAttributes().build());
		event.put(SCULK_CREAKING.get(), SculkCreakingEntity.createAttributes().build());
		event.put(SCULK_FISH.get(), SculkFishEntity.createAttributes().build());
		event.put(SCULK_GHOST.get(), SculkGhostEntity.createAttributes().build());
		event.put(AGILE_SLIME.get(), AgileSlimeEntity.createAttributes().build());
		event.put(RADIOACTIVE_WARDEN.get(), RadioactiveWardenEntity.createAttributes().build());
		event.put(TINY_AGILE_SLIME.get(), TinyAgileSlimeEntity.createAttributes().build());
		event.put(SHADOW_HUNTER.get(), ShadowHunterEntity.createAttributes().build());
		event.put(SCULK_ENDERMAN.get(), SculkEndermanEntity.createAttributes().build());
	}
}
