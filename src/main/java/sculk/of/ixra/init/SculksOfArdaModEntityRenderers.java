
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package sculk.of.ixra.init;

import sculk.of.ixra.client.renderer.TinyAgileSlimeRenderer;
import sculk.of.ixra.client.renderer.ShadowHunterRenderer;
import sculk.of.ixra.client.renderer.SculkSkeletonRenderer;
import sculk.of.ixra.client.renderer.SculkGhostRenderer;
import sculk.of.ixra.client.renderer.SculkFoxRenderer;
import sculk.of.ixra.client.renderer.SculkFishRenderer;
import sculk.of.ixra.client.renderer.SculkEndermanRenderer;
import sculk.of.ixra.client.renderer.SculkCreeperRenderer;
import sculk.of.ixra.client.renderer.SculkCreakingRenderer;
import sculk.of.ixra.client.renderer.SculkBoss1TRenderer;
import sculk.of.ixra.client.renderer.SculkArrowRenderer;
import sculk.of.ixra.client.renderer.RadioactiveWardenRenderer;
import sculk.of.ixra.client.renderer.AgileSlimeRenderer;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SculksOfArdaModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SculksOfArdaModEntities.SCULK_FOX.get(), SculkFoxRenderer::new);
		event.registerEntityRenderer(SculksOfArdaModEntities.SCULK_BOSS_1_T.get(), SculkBoss1TRenderer::new);
		event.registerEntityRenderer(SculksOfArdaModEntities.SCULK_ARROW.get(), SculkArrowRenderer::new);
		event.registerEntityRenderer(SculksOfArdaModEntities.SCULK_CREEPER.get(), SculkCreeperRenderer::new);
		event.registerEntityRenderer(SculksOfArdaModEntities.SCULK_SKELETON.get(), SculkSkeletonRenderer::new);
		event.registerEntityRenderer(SculksOfArdaModEntities.SCULK_CREAKING.get(), SculkCreakingRenderer::new);
		event.registerEntityRenderer(SculksOfArdaModEntities.SCULK_FISH.get(), SculkFishRenderer::new);
		event.registerEntityRenderer(SculksOfArdaModEntities.SCULK_GHOST.get(), SculkGhostRenderer::new);
		event.registerEntityRenderer(SculksOfArdaModEntities.AGILE_SLIME.get(), AgileSlimeRenderer::new);
		event.registerEntityRenderer(SculksOfArdaModEntities.RADIOACTIVE_WARDEN.get(), RadioactiveWardenRenderer::new);
		event.registerEntityRenderer(SculksOfArdaModEntities.TINY_AGILE_SLIME.get(), TinyAgileSlimeRenderer::new);
		event.registerEntityRenderer(SculksOfArdaModEntities.SHADOW_HUNTER.get(), ShadowHunterRenderer::new);
		event.registerEntityRenderer(SculksOfArdaModEntities.SCULK_ENDERMAN.get(), SculkEndermanRenderer::new);
	}
}
