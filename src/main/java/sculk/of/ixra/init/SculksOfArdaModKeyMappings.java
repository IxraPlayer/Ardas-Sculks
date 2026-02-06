
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package sculk.of.ixra.init;

import sculk.of.ixra.network.WardenFeelingsButtonMessage;
import sculk.of.ixra.network.SonicBoomButtonMessage;

import org.lwjgl.glfw.GLFW;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class SculksOfArdaModKeyMappings {
	public static final KeyMapping SONIC_BOOM_BUTTON = new KeyMapping("key.sculks_of_arda.sonic_boom_button", GLFW.GLFW_KEY_R, "key.categories.gameplay") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new SonicBoomButtonMessage(0, 0));
				SonicBoomButtonMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping WARDEN_FEELINGS_BUTTON = new KeyMapping("key.sculks_of_arda.warden_feelings_button", GLFW.GLFW_KEY_H, "key.categories.gameplay") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new WardenFeelingsButtonMessage(0, 0));
				WardenFeelingsButtonMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(SONIC_BOOM_BUTTON);
		event.register(WARDEN_FEELINGS_BUTTON);
	}

	@EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			if (Minecraft.getInstance().screen == null) {
				SONIC_BOOM_BUTTON.consumeClick();
				WARDEN_FEELINGS_BUTTON.consumeClick();
			}
		}
	}
}
