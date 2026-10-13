
package sculk.of.ixra.item;

import sculk.of.ixra.init.SculksOfArdaModItems;

import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Holder;
import net.minecraft.Util;

import java.util.List;
import java.util.EnumMap;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public abstract class SculkArmorItem extends ArmorItem {
	public static Holder<ArmorMaterial> ARMOR_MATERIAL = null;

	@SubscribeEvent
	public static void registerArmorMaterial(RegisterEvent event) {
		event.register(Registries.ARMOR_MATERIAL, registerHelper -> {
			ArmorMaterial armorMaterial = new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
				map.put(ArmorItem.Type.BOOTS, 4);
				map.put(ArmorItem.Type.LEGGINGS, 8);
				map.put(ArmorItem.Type.CHESTPLATE, 10);
				map.put(ArmorItem.Type.HELMET, 5);
				map.put(ArmorItem.Type.BODY, 10);
			}), 30, DeferredHolder.create(Registries.SOUND_EVENT, ResourceLocation.parse("block.sculk_shrieker.shriek")), () -> Ingredient.of(new ItemStack(SculksOfArdaModItems.SCULK_INGOT.get())),
					List.of(new ArmorMaterial.Layer(ResourceLocation.parse("sculks_of_arda:sculkerite"))), 4f, 0.2f);
			registerHelper.register(ResourceLocation.parse("sculks_of_arda:sculk_armor"), armorMaterial);
			ARMOR_MATERIAL = BuiltInRegistries.ARMOR_MATERIAL.wrapAsHolder(armorMaterial);
		});
	}

	public SculkArmorItem(ArmorItem.Type type, Item.Properties properties) {
		super(ARMOR_MATERIAL, type, properties);
	}

	public static class Helmet extends SculkArmorItem {
		public Helmet() {
			super(ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(56)).fireResistant());
		}
	}

	public static class Chestplate extends SculkArmorItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(56)).fireResistant());
		}
	}

	public static class Leggings extends SculkArmorItem {
		public Leggings() {
			super(ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(56)).fireResistant());
		}
	}

	public static class Boots extends SculkArmorItem {
		public Boots() {
			super(ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(56)).fireResistant());
		}
	}
}
