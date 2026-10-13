package sculk.of.ixra.block.listener;

import sculk.of.ixra.init.SculksOfArdaModBlockEntities;
import sculk.of.ixra.block.renderer.RSculkChestTileRenderer;
import sculk.of.ixra.block.entity.RSculkChestTileEntity;
import sculk.of.ixra.SculksOfArdaMod;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.block.entity.BlockEntityType;

@EventBusSubscriber(modid = SculksOfArdaMod.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientListener {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer((BlockEntityType<RSculkChestTileEntity>) SculksOfArdaModBlockEntities.SCULK_CHEST.get(), context -> new RSculkChestTileRenderer());
	}
}
