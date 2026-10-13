package sculk.of.ixra.bossheads;

import sculk.of.ixra.SculksOfArdaMod;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.IEventBus;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.EnumMap;

/**
 * Registers the three boss trophy heads (floor block + wall block + item each) and
 * puts them at the very end of the "Arda's Sculks - Blocks" creative tab.
 *
 * Everything lives in its own DeferredRegisters so the MCreator generated init classes are untouched;
 * the only hook is BossHeadRegistry.register(modEventBus) in the mod constructor.
 */
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class BossHeadRegistry {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SculksOfArdaMod.MODID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SculksOfArdaMod.MODID);

	private static final Map<BossHeadType, DeferredBlock<BossHeadBlock>> FLOOR = new EnumMap<>(BossHeadType.class);
	private static final Map<BossHeadType, DeferredBlock<BossWallHeadBlock>> WALL = new EnumMap<>(BossHeadType.class);
	private static final Map<BossHeadType, DeferredItem<BossHeadItem>> ITEM = new EnumMap<>(BossHeadType.class);

	static {
		for (BossHeadType type : BossHeadType.values()) {
			DeferredBlock<BossHeadBlock> floor = BLOCKS.register(type.floorId, () -> new BossHeadBlock(type));
			DeferredBlock<BossWallHeadBlock> wall = BLOCKS.register(type.wallId, () -> new BossWallHeadBlock(type));
			DeferredItem<BossHeadItem> item = ITEMS.register(type.floorId, () -> new BossHeadItem(type, floor.get(), wall.get()));
			FLOOR.put(type, floor);
			WALL.put(type, wall);
			ITEM.put(type, item);
		}
	}

	/** one block entity type for all six head blocks; it only exists to draw the animated model */
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SculksOfArdaMod.MODID);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BossHeadBlockEntity>> BLOCK_ENTITY = BLOCK_ENTITIES.register("boss_head", () -> {
		Block[] all = new Block[BossHeadType.values().length * 2];
		int i = 0;
		for (BossHeadType type : BossHeadType.values()) {
			all[i++] = FLOOR.get(type).get();
			all[i++] = WALL.get(type).get();
		}
		return BlockEntityType.Builder.of(BossHeadBlockEntity::new, all).build(null);
	});

	public static void register(IEventBus modEventBus) {
		BLOCKS.register(modEventBus);
		ITEMS.register(modEventBus);
		BLOCK_ENTITIES.register(modEventBus);
	}

	public static BlockEntityType<BossHeadBlockEntity> blockEntityType() {
		return BLOCK_ENTITY.get();
	}

	public static Block floorBlock(BossHeadType type) {
		return FLOOR.get(type).get();
	}

	public static Item item(BossHeadType type) {
		return ITEM.get(type).get();
	}

	@SubscribeEvent
	public static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey().equals(sculk.of.ixra.init.SculksOfArdaModTabs.ARDAS_SCULKS_BLOCKS.getKey())) {
			for (BossHeadType type : BossHeadType.values())
				event.accept(item(type));
		}
	}
}
