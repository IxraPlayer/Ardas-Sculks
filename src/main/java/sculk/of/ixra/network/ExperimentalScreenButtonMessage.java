
package sculk.of.ixra.network;

import sculk.of.ixra.world.inventory.ExperimentalScreenMenu;
import sculk.of.ixra.procedures.OpenMenuGuiProcedure;
import sculk.of.ixra.SculksOfArdaMod;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.BlockPos;

import java.util.HashMap;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record ExperimentalScreenButtonMessage(int buttonID, int x, int y, int z) implements CustomPacketPayload {

	public static final Type<ExperimentalScreenButtonMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(SculksOfArdaMod.MODID, "experimental_screen_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ExperimentalScreenButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, ExperimentalScreenButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}, (RegistryFriendlyByteBuf buffer) -> new ExperimentalScreenButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));
	@Override
	public Type<ExperimentalScreenButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final ExperimentalScreenButtonMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				Player entity = context.player();
				int buttonID = message.buttonID;
				int x = message.x;
				int y = message.y;
				int z = message.z;
				handleButtonAction(entity, buttonID, x, y, z);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
		Level world = entity.level();
		HashMap guistate = ExperimentalScreenMenu.guistate;
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (buttonID == 0) {

			OpenMenuGuiProcedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		SculksOfArdaMod.addNetworkMessage(ExperimentalScreenButtonMessage.TYPE, ExperimentalScreenButtonMessage.STREAM_CODEC, ExperimentalScreenButtonMessage::handleData);
	}
}
