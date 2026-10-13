package sculk.of.ixra.procedures;

import sculk.of.ixra.init.SculksOfArdaModItems;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;

import javax.annotation.Nullable;

@EventBusSubscriber
public class SculkBowPullingAnimationProcedure {
	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		if (event.getHand() != event.getEntity().getUsedItemHand())
			return;
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains(new ItemStack(SculksOfArdaModItems.SCULK_ARROW_ITEM.get())) : false)
				&& (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == SculksOfArdaModItems.SCULK_BOW.get()) {
			if (entity instanceof LivingEntity _entity) {
				ItemStack _setstack = new ItemStack(SculksOfArdaModItems.SCULK_BOW_PULLING.get()).copy();
				_setstack.setCount(1);
				_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
				if (_entity instanceof Player _player)
					_player.getInventory().setChanged();
			}
		}
		if ((entity instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains(new ItemStack(SculksOfArdaModItems.SCULK_ARROW_ITEM.get())) : false)
				&& (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == SculksOfArdaModItems.SCULK_BOW.get()) {
			if (entity instanceof LivingEntity _entity) {
				ItemStack _setstack = new ItemStack(SculksOfArdaModItems.SCULK_BOW_PULLING.get()).copy();
				_setstack.setCount(1);
				_entity.setItemInHand(InteractionHand.OFF_HAND, _setstack);
				if (_entity instanceof Player _player)
					_player.getInventory().setChanged();
			}
		}
	}
}
