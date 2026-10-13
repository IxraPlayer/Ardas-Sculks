package sculk.of.ixra.bossheads.client;

import sculk.of.ixra.bossheads.BossHeadType;
import sculk.of.ixra.bossheads.BossHeadRegistry;

import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BossHeadClient {
	private static BossHeadItemRenderer renderer;

	@SubscribeEvent
	public static void registerItemExtensions(RegisterClientExtensionsEvent event) {
		IClientItemExtensions extensions = new IClientItemExtensions() {
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				if (renderer == null)
					renderer = new BossHeadItemRenderer();
				return renderer;
			}
		};
		for (BossHeadType type : BossHeadType.values())
			event.registerItem(extensions, BossHeadRegistry.item(type));
	}

	@SubscribeEvent
	public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(BossHeadRegistry.blockEntityType(), BossHeadBlockEntityRenderer::new);
	}

	/** F3+T (resource reload) re-reads the head models */
	@SubscribeEvent
	public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener((net.minecraft.server.packs.resources.ResourceManagerReloadListener) resourceManager -> BossHeadModel.clearCache());
	}
}
