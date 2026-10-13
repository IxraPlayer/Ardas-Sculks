
/*
*	MCreator note: This file will be REGENERATED on each build.
*/
package sculk.of.ixra.init;

import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;

@EventBusSubscriber
public class SculksOfArdaModTrades {
	@SubscribeEvent
	public static void registerTrades(VillagerTradesEvent event) {
		if (event.getType() == SculksOfArdaModVillagerProfessions.DARK_VILLAGER.get()) {
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get()), new ItemStack(Blocks.SCULK, 5), 5, 14, 0.05f));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 5), new ItemStack(SculksOfArdaModItems.SCULK_PIECE.get(), 2), 4, 9, 0.05f));
			event.getTrades().get(3).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 20), new ItemStack(SculksOfArdaModItems.SCULK_SENSOR_PART.get(), 3), 4, 11, 0.05f));
			event.getTrades().get(3).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 30), new ItemStack(SculksOfArdaModBlocks.SCULK_BRICK.get(), 15), 10, 5, 0.05f));
			event.getTrades().get(4).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 10), new ItemStack(Items.ECHO_SHARD, 6), 6, 5, 0.05f));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 2), new ItemStack(Blocks.DEEPSLATE_BRICKS, 5), 15, 5, 0.05f));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 4), new ItemStack(Blocks.CHISELED_DEEPSLATE, 8), 15, 5, 0.05f));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 2), new ItemStack(Blocks.POLISHED_DEEPSLATE, 15), 15, 5, 0.05f));
			event.getTrades().get(4).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 25), new ItemStack(SculksOfArdaModBlocks.SCULK_BRICK_CARVED.get(), 10), 8, 5, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 35), new ItemStack(SculksOfArdaModBlocks.DIAMOND_SCULK_ORE.get()), 5, 5, 0.05f));
			event.getTrades().get(5)
					.add(new BasicItemListing(new ItemStack(SculksOfArdaModBlocks.SCULKED_EMERALD_BLOCK.get(), 32), new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 64), new ItemStack(SculksOfArdaModItems.SCULK_INGOT.get()), 1, 5, 0.05f));
			event.getTrades().get(5).add(
					new BasicItemListing(new ItemStack(SculksOfArdaModBlocks.SCULKED_EMERALD_BLOCK.get(), 6), new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 28), new ItemStack(SculksOfArdaModItems.GOLDEN_SCULK_PIECE.get()), 1, 5, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(SculksOfArdaModBlocks.SCULKED_EMERALD_BLOCK.get(), 8), new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 48),
					new ItemStack(SculksOfArdaModItems.GOLDEN_SCULK_SENSOR_PART.get()), 1, 5, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(SculksOfArdaModBlocks.SCULKED_EMERALD_BLOCK.get(), 10), new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 64),
					new ItemStack(SculksOfArdaModItems.GOLDEN_WARDEN_CLAWS.get()), 1, 5, 0.05f));
			event.getTrades().get(5)
					.add(new BasicItemListing(new ItemStack(SculksOfArdaModBlocks.SCULKED_EMERALD_BLOCK.get(), 32), new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 64), new ItemStack(Blocks.WITHER_SKELETON_SKULL), 1, 5, 0.05f));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get()), new ItemStack(SculksOfArdaModBlocks.SCULK_FLOWER.get(), 5), 8, 5, 0.05f));
			event.getTrades().get(3).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 6), new ItemStack(SculksOfArdaModBlocks.SCULK_FUNGUS.get(), 2), 6, 5, 0.05f));
			event.getTrades().get(3).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 6), new ItemStack(SculksOfArdaModBlocks.QUARTZ_SCULK_ORE.get(), 4), 8, 5, 0.05f));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 8), new ItemStack(Blocks.SCULK_SENSOR, 12), 6, 5, 0.05f));
			event.getTrades().get(4).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 32), new ItemStack(Blocks.CALIBRATED_SCULK_SENSOR, 6), 5, 5, 0.05f));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 9), new ItemStack(Blocks.SCULK_CATALYST, 10), 6, 5, 0.05f));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 4), new ItemStack(Blocks.SCULK_VEIN, 8), 6, 5, 0.05f));
			event.getTrades().get(5).add(
					new BasicItemListing(new ItemStack(SculksOfArdaModBlocks.SCULKED_EMERALD_BLOCK.get(), 42), new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 12), new ItemStack(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE), 1, 5, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(SculksOfArdaModBlocks.SCULKED_EMERALD_BLOCK.get(), 4), new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 6), new ItemStack(Blocks.SCULK_SHRIEKER, 2), 2, 5, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(SculksOfArdaModBlocks.SCULKED_EMERALD_BLOCK.get(), 22), new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 32), new ItemStack(Items.NETHERITE_SCRAP, 2), 1, 5, 0.05f));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULK_PIECE.get(), 36), new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 12), 10, 5, 0.05f));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.SCULK_SENSOR_PART.get(), 24), new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 10), 10, 5, 0.05f));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.WARDEN_CLAWS.get(), 18), new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 8), 10, 5, 0.05f));
			event.getTrades().get(3).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.GOLDEN_SCULK_PIECE.get(), 16), new ItemStack(SculksOfArdaModBlocks.SCULKED_EMERALD_BLOCK.get(), 4), 10, 5, 0.05f));
			event.getTrades().get(3).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.GOLDEN_SCULK_SENSOR_PART.get(), 12), new ItemStack(SculksOfArdaModBlocks.SCULKED_EMERALD_BLOCK.get(), 3), 10, 5, 0.05f));
			event.getTrades().get(3).add(new BasicItemListing(new ItemStack(SculksOfArdaModItems.GOLDEN_WARDEN_CLAWS.get(), 6), new ItemStack(SculksOfArdaModBlocks.SCULKED_EMERALD_BLOCK.get(), 2), 10, 5, 0.05f));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(SculksOfArdaModBlocks.SCULK_FLOWER.get(), 52), new ItemStack(SculksOfArdaModBlocks.SCULKED_EMERALD_BLOCK.get()), 10, 5, 0.05f));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(SculksOfArdaModBlocks.SCULK_FUNGUS.get(), 30), new ItemStack(SculksOfArdaModBlocks.SCULKED_EMERALD_BLOCK.get()), 10, 5, 0.05f));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Blocks.SCULK_SHRIEKER, 12), new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 12), 10, 5, 0.05f));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(Blocks.CALIBRATED_SCULK_SENSOR, 16), new ItemStack(SculksOfArdaModBlocks.SCULKED_EMERALD_BLOCK.get(), 2), 10, 5, 0.05f));
			event.getTrades().get(4).add(
					new BasicItemListing(new ItemStack(SculksOfArdaModBlocks.SCULK_FLOWER.get(), 38), new ItemStack(SculksOfArdaModBlocks.SCULK_FUNGUS.get(), 16), new ItemStack(SculksOfArdaModBlocks.SCULKED_EMERALD_BLOCK.get(), 3), 10, 5, 0.05f));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Blocks.SCULK, 64), new ItemStack(Blocks.SCULK_CATALYST, 36), new ItemStack(SculksOfArdaModBlocks.SCULKED_EMERALD_BLOCK.get(), 4), 10, 5, 0.05f));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Blocks.COBBLED_DEEPSLATE, 6), new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 2), new ItemStack(Blocks.DEEPSLATE_BRICKS, 18), 10, 5, 0.05f));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(Blocks.DEEPSLATE, 8), new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get(), 2), new ItemStack(Blocks.CHISELED_DEEPSLATE, 36), 10, 5, 0.05f));
		}
	}
}
