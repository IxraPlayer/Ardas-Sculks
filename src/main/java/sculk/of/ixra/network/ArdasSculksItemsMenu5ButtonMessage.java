
package sculk.of.ixra.network;

import sculk.of.ixra.world.inventory.ArdasSculksItemsMenu5Menu;
import sculk.of.ixra.procedures.OpenArdasSculksItemsMenu4GuiProcedure;
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
public record ArdasSculksItemsMenu5ButtonMessage(int buttonID, int x, int y, int z) implements CustomPacketPayload {

	public static final Type<ArdasSculksItemsMenu5ButtonMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(SculksOfArdaMod.MODID, "ardas_sculks_items_menu_5_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ArdasSculksItemsMenu5ButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, ArdasSculksItemsMenu5ButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}, (RegistryFriendlyByteBuf buffer) -> new ArdasSculksItemsMenu5ButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt()));
	@Override
	public Type<ArdasSculksItemsMenu5ButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final ArdasSculksItemsMenu5ButtonMessage message, final IPayloadContext context) {
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
		HashMap guistate = ArdasSculksItemsMenu5Menu.guistate;
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (buttonID == 0) {

			OpenArdasSculksItemsMenu4GuiProcedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		SculksOfArdaMod.addNetworkMessage(ArdasSculksItemsMenu5ButtonMessage.TYPE, ArdasSculksItemsMenu5ButtonMessage.STREAM_CODEC, ArdasSculksItemsMenu5ButtonMessage::handleData);
	}
}
