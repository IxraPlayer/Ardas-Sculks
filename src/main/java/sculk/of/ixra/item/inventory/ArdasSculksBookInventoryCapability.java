package sculk.of.ixra.item.inventory;

import sculk.of.ixra.init.SculksOfArdaModItems;
import sculk.of.ixra.client.gui.ExperimentalScreenScreen;

import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

@EventBusSubscriber(Dist.CLIENT)
public class ArdasSculksBookInventoryCapability extends ComponentItemHandler {
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onItemDropped(ItemTossEvent event) {
		if (event.getEntity().getItem().getItem() == SculksOfArdaModItems.ARDAS_SCULKS_BOOK.get()) {
			if (Minecraft.getInstance().screen instanceof ExperimentalScreenScreen) {
				Minecraft.getInstance().player.closeContainer();
			}
		}
	}

	public ArdasSculksBookInventoryCapability(MutableDataComponentHolder parent) {
		super(parent, DataComponents.CONTAINER, 0);
	}

	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return stack.getItem() != SculksOfArdaModItems.ARDAS_SCULKS_BOOK.get();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return super.getStackInSlot(slot).copy();
	}
}
