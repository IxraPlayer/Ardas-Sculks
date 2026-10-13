
package sculk.of.ixra.network;

import sculk.of.ixra.SculksOfArdaMod;
import sculk.of.ixra.client.ShadowHunterScreenShakeClient;

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
 * Sunucudan sadece tekmeyi yiyen oyuncuya gonderilir: ekranda hafif,
 * kisa sureli bir sallanma (camera shake) tetiklemek icin.
 */
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record ShadowHunterKickShakeMessage(int durationTicks) implements CustomPacketPayload {
	public static final Type<ShadowHunterKickShakeMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(SculksOfArdaMod.MODID, "shadowhunter_kick_shake"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ShadowHunterKickShakeMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, ShadowHunterKickShakeMessage message) -> {
		buffer.writeInt(message.durationTicks);
	}, (RegistryFriendlyByteBuf buffer) -> new ShadowHunterKickShakeMessage(buffer.readInt()));

	@Override
	public Type<ShadowHunterKickShakeMessage> type() {
		return TYPE;
	}

	public static void handleData(final ShadowHunterKickShakeMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> {
				ShadowHunterScreenShakeClient.startShake(message.durationTicks);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		SculksOfArdaMod.addNetworkMessage(ShadowHunterKickShakeMessage.TYPE, ShadowHunterKickShakeMessage.STREAM_CODEC, ShadowHunterKickShakeMessage::handleData);
	}
}
