
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package sculk.of.ixra.init;

import sculk.of.ixra.world.inventory.SculkChestGuiMenu;
import sculk.of.ixra.world.inventory.ExperimentalScreenMenu;
import sculk.of.ixra.world.inventory.ExperimentalScreenFurnaceMenu;
import sculk.of.ixra.world.inventory.DeepslateDispenserGUIMenu;
import sculk.of.ixra.world.inventory.ArdasSculksItemsMenu5Menu;
import sculk.of.ixra.world.inventory.ArdasSculksItemsMenu4Menu;
import sculk.of.ixra.world.inventory.ArdasSculksItemsMenu3Menu;
import sculk.of.ixra.world.inventory.ArdasSculksItemsMenu1Menu;
import sculk.of.ixra.world.inventory.ArdasSculksBookMenuMenu;
import sculk.of.ixra.world.inventory.ArdasSculksBookMenu2Menu;
import sculk.of.ixra.world.inventory.ArdasSculkItemsMenu2Menu;
import sculk.of.ixra.world.inventory.ArdasSculkBookMobsMenu1Menu;
import sculk.of.ixra.world.inventory.AdvancedFurnaceGuiMenu;
import sculk.of.ixra.SculksOfArdaMod;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.registries.Registries;

public class SculksOfArdaModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, SculksOfArdaMod.MODID);
	public static final DeferredHolder<MenuType<?>, MenuType<AdvancedFurnaceGuiMenu>> ADVANCED_FURNACE_GUI = REGISTRY.register("advanced_furnace_gui", () -> IMenuTypeExtension.create(AdvancedFurnaceGuiMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<ArdasSculksBookMenuMenu>> ARDAS_SCULKS_BOOK_MENU = REGISTRY.register("ardas_sculks_book_menu", () -> IMenuTypeExtension.create(ArdasSculksBookMenuMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<ArdasSculkBookMobsMenu1Menu>> ARDAS_SCULK_BOOK_MOBS_MENU_1 = REGISTRY.register("ardas_sculk_book_mobs_menu_1", () -> IMenuTypeExtension.create(ArdasSculkBookMobsMenu1Menu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<ArdasSculksBookMenu2Menu>> ARDAS_SCULKS_BOOK_MENU_2 = REGISTRY.register("ardas_sculks_book_menu_2", () -> IMenuTypeExtension.create(ArdasSculksBookMenu2Menu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<ArdasSculksItemsMenu1Menu>> ARDAS_SCULKS_ITEMS_MENU_1 = REGISTRY.register("ardas_sculks_items_menu_1", () -> IMenuTypeExtension.create(ArdasSculksItemsMenu1Menu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<ArdasSculkItemsMenu2Menu>> ARDAS_SCULK_ITEMS_MENU_2 = REGISTRY.register("ardas_sculk_items_menu_2", () -> IMenuTypeExtension.create(ArdasSculkItemsMenu2Menu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<ArdasSculksItemsMenu3Menu>> ARDAS_SCULKS_ITEMS_MENU_3 = REGISTRY.register("ardas_sculks_items_menu_3", () -> IMenuTypeExtension.create(ArdasSculksItemsMenu3Menu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<ArdasSculksItemsMenu4Menu>> ARDAS_SCULKS_ITEMS_MENU_4 = REGISTRY.register("ardas_sculks_items_menu_4", () -> IMenuTypeExtension.create(ArdasSculksItemsMenu4Menu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<ArdasSculksItemsMenu5Menu>> ARDAS_SCULKS_ITEMS_MENU_5 = REGISTRY.register("ardas_sculks_items_menu_5", () -> IMenuTypeExtension.create(ArdasSculksItemsMenu5Menu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<ExperimentalScreenMenu>> EXPERIMENTAL_SCREEN = REGISTRY.register("experimental_screen", () -> IMenuTypeExtension.create(ExperimentalScreenMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<ExperimentalScreenFurnaceMenu>> EXPERIMENTAL_SCREEN_FURNACE = REGISTRY.register("experimental_screen_furnace", () -> IMenuTypeExtension.create(ExperimentalScreenFurnaceMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<SculkChestGuiMenu>> SCULK_CHEST_GUI = REGISTRY.register("sculk_chest_gui", () -> IMenuTypeExtension.create(SculkChestGuiMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<DeepslateDispenserGUIMenu>> DEEPSLATE_DISPENSER_GUI = REGISTRY.register("deepslate_dispenser_gui", () -> IMenuTypeExtension.create(DeepslateDispenserGUIMenu::new));
}
