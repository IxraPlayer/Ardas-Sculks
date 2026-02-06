
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package sculk.of.ixra.init;

import sculk.of.ixra.block.entity.RSculkChestTileEntity;
import sculk.of.ixra.block.entity.DeepslateDispenserBlockEntity;
import sculk.of.ixra.block.entity.AdvancedFurnaceOnBlockEntity;
import sculk.of.ixra.block.entity.AdvancedFurnaceBlockEntity;
import sculk.of.ixra.SculksOfArdaMod;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class SculksOfArdaModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, SculksOfArdaMod.MODID);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> ADVANCED_FURNACE = register("advanced_furnace", SculksOfArdaModBlocks.ADVANCED_FURNACE, AdvancedFurnaceBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> ADVANCED_FURNACE_ON = register("advanced_furnace_on", SculksOfArdaModBlocks.ADVANCED_FURNACE_ON, AdvancedFurnaceOnBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> SCULK_CHEST = register("sculk_chest", SculksOfArdaModBlocks.SCULK_CHEST, RSculkChestTileEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> DEEPSLATE_DISPENSER = register("deepslate_dispenser", SculksOfArdaModBlocks.DEEPSLATE_DISPENSER, DeepslateDispenserBlockEntity::new);

	// Start of user code block custom block entities
	// End of user code block custom block entities
	private static DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> register(String registryname, DeferredHolder<Block, Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ADVANCED_FURNACE.get(), (blockEntity, side) -> ((AdvancedFurnaceBlockEntity) blockEntity).getItemHandler());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ADVANCED_FURNACE_ON.get(), (blockEntity, side) -> ((AdvancedFurnaceOnBlockEntity) blockEntity).getItemHandler());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, DEEPSLATE_DISPENSER.get(), (blockEntity, side) -> ((DeepslateDispenserBlockEntity) blockEntity).getItemHandler());
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SCULK_CHEST.get(), (blockEntity, side) -> ((RSculkChestTileEntity) blockEntity).getItemHandler());
	}
}
