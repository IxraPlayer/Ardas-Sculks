
package sculk.of.ixra.network;

import sculk.of.ixra.SculksOfArdaMod;
import sculk.of.ixra.client.ShadowHunterAfterimageClient;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

/**
 * Sunucudan istemciye gonderilir: ShadowHunter isinlandigi anda,
 * eski konumunda saydam/solan bir "iz" (afterimage) gostermek icin.
 */
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record ShadowHunterAfterimageMessage(double fromX, double fromY, double fromZ, double toX, double toY, double toZ, float yRot, float xRot, String texture, int count, int surface) implements CustomPacketPayload {
	public static final Type<ShadowHunterAfterimageMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(SculksOfArdaMod.MODID, "shadowhunter_afterimage"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ShadowHunterAfterimageMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, ShadowHunterAfterimageMessage message) -> {
		buffer.writeDouble(message.fromX);
		buffer.writeDouble(message.fromY);
		buffer.writeDouble(message.fromZ);
		buffer.writeDouble(message.toX);
		buffer.writeDouble(message.toY);
		buffer.writeDouble(message.toZ);
		buffer.writeFloat(message.yRot);
		buffer.writeFloat(message.xRot);
		buffer.writeUtf(message.texture);
		buffer.writeInt(message.count);
		buffer.writeInt(message.surface);
	}, (RegistryFriendlyByteBuf buffer) -> new ShadowHunterAfterimageMessage(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readFloat(), buffer.readFloat(), buffer.readUtf(), buffer.readInt(), buffer.readInt()));

	@Override
	public Type<ShadowHunterAfterimageMessage> type() {
		return TYPE;
	}

	public static void handleData(final ShadowHunterAfterimageMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				ShadowHunterAfterimageClient.spawnTrail(message.fromX, message.fromY, message.fromZ, message.toX, message.toY, message.toZ, message.yRot, message.xRot, message.texture, message.count, message.surface);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		SculksOfArdaMod.addNetworkMessage(ShadowHunterAfterimageMessage.TYPE, ShadowHunterAfterimageMessage.STREAM_CODEC, ShadowHunterAfterimageMessage::handleData);
	}
}
