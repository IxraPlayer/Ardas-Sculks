
package sculk.of.ixra.network;

import sculk.of.ixra.world.inventory.ArdasSculksItemsMenu4Menu;
import sculk.of.ixra.procedures.OpenArdasSculksItemsMenu5GuiProcedure;
import sculk.of.ixra.procedures.OpenArdasSculksItemsMenu3GuiProcedure;
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
public record ArdasSculksItemsMenu4ButtonMessage(int buttonID, int x, int y, int z) implements CustomPacketPayload {

	public static final Type<ArdasSculksItemsMenu4ButtonMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(SculksOfArdaMod.MODID, "ardas_sculks_items_menu_4_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ArdasSculksItemsMenu4ButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, ArdasSculksItemsMenu4ButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}, (RegistryFriendlyByteBuf buffer) -> new ArdasSculksItemsMenu4ButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));
	@Override
	public Type<ArdasSculksItemsMenu4ButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final ArdasSculksItemsMenu4ButtonMessage message, final IPayloadContext context) {
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
		HashMap guistate = ArdasSculksItemsMenu4Menu.guistate;
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (buttonID == 0) {
			OpenArdasSculksItemsMenu3GuiProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 1) {
			OpenArdasSculksItemsMenu5GuiProcedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		SculksOfArdaMod.addNetworkMessage(ArdasSculksItemsMenu4ButtonMessage.TYPE, ArdasSculksItemsMenu4ButtonMessage.STREAM_CODEC, ArdasSculksItemsMenu4ButtonMessage::handleData);
	}
}
