package sculk.of.ixra.bossheads.client;

import sculk.of.ixra.bossheads.BossHeadItem;

import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.client.model.PlayerModel;

/**
 * While a player wears one of the boss heads, the player's own head (and the hat layer) is not drawn,
 * so only the boss head is seen. The visibility is reset by the game every time it prepares the player model.
 */
@EventBusSubscriber(value = Dist.CLIENT)
public class BossHeadHidePlayerHead {
	@SubscribeEvent
	public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
		if (!(event.getEntity() instanceof Player player))
			return;
		if (!(player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof BossHeadItem))
			return;
		if (event.getRenderer().getModel() instanceof PlayerModel<?> model) {
			model.head.visible = false;
			model.hat.visible = false;
		}
	}
}
