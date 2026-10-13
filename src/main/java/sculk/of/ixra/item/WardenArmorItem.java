
package sculk.of.ixra.item;

import sculk.of.ixra.procedures.WardenFeelingsOnKeyPressedProcedure;
import sculk.of.ixra.init.SculksOfArdaModItems;
import sculk.of.ixra.client.model.Modelwardenarmorfix;

import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Holder;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.Minecraft;
import net.minecraft.Util;

import java.util.Map;
import java.util.List;
import java.util.EnumMap;
import java.util.Collections;

import com.google.common.collect.Iterables;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public abstract class WardenArmorItem extends ArmorItem {
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
					List.of(new ArmorMaterial.Layer(ResourceLocation.parse("sculks_of_arda:warden_armor_fix"))), 4f, 0.2f);
			registerHelper.register(ResourceLocation.parse("sculks_of_arda:warden_armor"), armorMaterial);
			ARMOR_MATERIAL = BuiltInRegistries.ARMOR_MATERIAL.wrapAsHolder(armorMaterial);
		});
	}

	@SubscribeEvent
	public static void registerItemExtensions(RegisterClientExtensionsEvent event) {
		event.registerItem(new IClientItemExtensions() {
			@Override
			public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
				HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
						Map.of("head", new Modelwardenarmorfix(Minecraft.getInstance().getEntityModels().bakeLayer(Modelwardenarmorfix.LAYER_LOCATION)).Head, "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "body",
								new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
								"right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
				armorModel.crouching = living.isShiftKeyDown();
				armorModel.riding = defaultModel.riding;
				armorModel.young = living.isBaby();
				return armorModel;
			}
		}, SculksOfArdaModItems.WARDEN_ARMOR_HELMET.get());
		event.registerItem(new IClientItemExtensions() {
			@Override
			@OnlyIn(Dist.CLIENT)
			public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
				HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(), Map.of("body", new Modelwardenarmorfix(Minecraft.getInstance().getEntityModels().bakeLayer(Modelwardenarmorfix.LAYER_LOCATION)).Body, "left_arm",
						new Modelwardenarmorfix(Minecraft.getInstance().getEntityModels().bakeLayer(Modelwardenarmorfix.LAYER_LOCATION)).LeftArm, "right_arm",
						new Modelwardenarmorfix(Minecraft.getInstance().getEntityModels().bakeLayer(Modelwardenarmorfix.LAYER_LOCATION)).RightArm, "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "hat",
						new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
				armorModel.crouching = living.isShiftKeyDown();
				armorModel.riding = defaultModel.riding;
				armorModel.young = living.isBaby();
				return armorModel;
			}
		}, SculksOfArdaModItems.WARDEN_ARMOR_CHESTPLATE.get());
		event.registerItem(new IClientItemExtensions() {
			@Override
			@OnlyIn(Dist.CLIENT)
			public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
				HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
						Map.of("left_leg", new Modelwardenarmorfix(Minecraft.getInstance().getEntityModels().bakeLayer(Modelwardenarmorfix.LAYER_LOCATION)).LeftLeg, "right_leg",
								new Modelwardenarmorfix(Minecraft.getInstance().getEntityModels().bakeLayer(Modelwardenarmorfix.LAYER_LOCATION)).RightLeg, "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "hat",
								new ModelPart(Collections.emptyList(), Collections.emptyMap()), "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
								"left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
				armorModel.crouching = living.isShiftKeyDown();
				armorModel.riding = defaultModel.riding;
				armorModel.young = living.isBaby();
				return armorModel;
			}
		}, SculksOfArdaModItems.WARDEN_ARMOR_LEGGINGS.get());
		event.registerItem(new IClientItemExtensions() {
			@Override
			@OnlyIn(Dist.CLIENT)
			public HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
				HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
						Map.of("left_leg", new Modelwardenarmorfix(Minecraft.getInstance().getEntityModels().bakeLayer(Modelwardenarmorfix.LAYER_LOCATION)).LeftLeg, "right_leg",
								new Modelwardenarmorfix(Minecraft.getInstance().getEntityModels().bakeLayer(Modelwardenarmorfix.LAYER_LOCATION)).RightLeg, "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "hat",
								new ModelPart(Collections.emptyList(), Collections.emptyMap()), "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()), "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
								"left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
				armorModel.crouching = living.isShiftKeyDown();
				armorModel.riding = defaultModel.riding;
				armorModel.young = living.isBaby();
				return armorModel;
			}
		}, SculksOfArdaModItems.WARDEN_ARMOR_BOOTS.get());
	}

	public WardenArmorItem(ArmorItem.Type type, Item.Properties properties) {
		super(ARMOR_MATERIAL, type, properties);
	}

	public static class Helmet extends WardenArmorItem {
		public Helmet() {
			super(ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(85)).fireResistant());
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
			super.appendHoverText(itemstack, context, list, flag);
			list.add(Component.translatable("item.sculks_of_arda.warden_armor_helmet.description_0"));
			list.add(Component.translatable("item.sculks_of_arda.warden_armor_helmet.description_1"));
		}
	}

	public static class Chestplate extends WardenArmorItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(85)).fireResistant());
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
			super.appendHoverText(itemstack, context, list, flag);
			list.add(Component.translatable("item.sculks_of_arda.warden_armor_chestplate.description_0"));
			list.add(Component.translatable("item.sculks_of_arda.warden_armor_chestplate.description_1"));
		}

		@Override
		public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
			super.inventoryTick(itemstack, world, entity, slot, selected);
			if (entity instanceof Player player && Iterables.contains(player.getArmorSlots(), itemstack)) {
				WardenFeelingsOnKeyPressedProcedure.execute(entity);
			}
		}
	}

	public static class Leggings extends WardenArmorItem {
		public Leggings() {
			super(ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(85)).fireResistant());
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
			super.appendHoverText(itemstack, context, list, flag);
			list.add(Component.translatable("item.sculks_of_arda.warden_armor_leggings.description_0"));
			list.add(Component.translatable("item.sculks_of_arda.warden_armor_leggings.description_1"));
		}
	}

	public static class Boots extends WardenArmorItem {
		public Boots() {
			super(ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(85)).fireResistant());
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
			super.appendHoverText(itemstack, context, list, flag);
			list.add(Component.translatable("item.sculks_of_arda.warden_armor_boots.description_0"));
			list.add(Component.translatable("item.sculks_of_arda.warden_armor_boots.description_1"));
		}
	}
}
