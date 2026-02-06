
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package sculk.of.ixra.init;

import sculk.of.ixra.client.gui.SculkChestGuiScreen;
import sculk.of.ixra.client.gui.ExperimentalScreenScreen;
import sculk.of.ixra.client.gui.ExperimentalScreenFurnaceScreen;
import sculk.of.ixra.client.gui.DeepslateDispenserGUIScreen;
import sculk.of.ixra.client.gui.ArdasSculksItemsMenu5Screen;
import sculk.of.ixra.client.gui.ArdasSculksItemsMenu4Screen;
import sculk.of.ixra.client.gui.ArdasSculksItemsMenu3Screen;
import sculk.of.ixra.client.gui.ArdasSculksItemsMenu1Screen;
import sculk.of.ixra.client.gui.ArdasSculksBookMenuScreen;
import sculk.of.ixra.client.gui.ArdasSculksBookMenu2Screen;
import sculk.of.ixra.client.gui.ArdasSculkItemsMenu2Screen;
import sculk.of.ixra.client.gui.ArdasSculkBookMobsMenu1Screen;
import sculk.of.ixra.client.gui.AdvancedFurnaceGuiScreen;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SculksOfArdaModScreens {
	@SubscribeEvent
	public static void clientLoad(RegisterMenuScreensEvent event) {
		event.register(SculksOfArdaModMenus.ADVANCED_FURNACE_GUI.get(), AdvancedFurnaceGuiScreen::new);
		event.register(SculksOfArdaModMenus.ARDAS_SCULKS_BOOK_MENU.get(), ArdasSculksBookMenuScreen::new);
		event.register(SculksOfArdaModMenus.ARDAS_SCULK_BOOK_MOBS_MENU_1.get(), ArdasSculkBookMobsMenu1Screen::new);
		event.register(SculksOfArdaModMenus.ARDAS_SCULKS_BOOK_MENU_2.get(), ArdasSculksBookMenu2Screen::new);
		event.register(SculksOfArdaModMenus.ARDAS_SCULKS_ITEMS_MENU_1.get(), ArdasSculksItemsMenu1Screen::new);
		event.register(SculksOfArdaModMenus.ARDAS_SCULK_ITEMS_MENU_2.get(), ArdasSculkItemsMenu2Screen::new);
		event.register(SculksOfArdaModMenus.ARDAS_SCULKS_ITEMS_MENU_3.get(), ArdasSculksItemsMenu3Screen::new);
		event.register(SculksOfArdaModMenus.ARDAS_SCULKS_ITEMS_MENU_4.get(), ArdasSculksItemsMenu4Screen::new);
		event.register(SculksOfArdaModMenus.ARDAS_SCULKS_ITEMS_MENU_5.get(), ArdasSculksItemsMenu5Screen::new);
		event.register(SculksOfArdaModMenus.EXPERIMENTAL_SCREEN.get(), ExperimentalScreenScreen::new);
		event.register(SculksOfArdaModMenus.EXPERIMENTAL_SCREEN_FURNACE.get(), ExperimentalScreenFurnaceScreen::new);
		event.register(SculksOfArdaModMenus.SCULK_CHEST_GUI.get(), SculkChestGuiScreen::new);
		event.register(SculksOfArdaModMenus.DEEPSLATE_DISPENSER_GUI.get(), DeepslateDispenserGUIScreen::new);
	}
}
