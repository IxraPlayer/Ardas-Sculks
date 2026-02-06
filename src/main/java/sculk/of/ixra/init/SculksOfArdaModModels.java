
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package sculk.of.ixra.init;

import sculk.of.ixra.client.model.Modelwardenarmorfix;
import sculk.of.ixra.client.model.Modelsculkskeleton;
import sculk.of.ixra.client.model.Modelsculkphantom;
import sculk.of.ixra.client.model.Modelsculkfox;
import sculk.of.ixra.client.model.Modelsculkbosss;
import sculk.of.ixra.client.model.Modelsculkarrow;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class SculksOfArdaModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(Modelwardenarmorfix.LAYER_LOCATION, Modelwardenarmorfix::createBodyLayer);
		event.registerLayerDefinition(Modelsculkfox.LAYER_LOCATION, Modelsculkfox::createBodyLayer);
		event.registerLayerDefinition(Modelsculkarrow.LAYER_LOCATION, Modelsculkarrow::createBodyLayer);
		event.registerLayerDefinition(Modelsculkskeleton.LAYER_LOCATION, Modelsculkskeleton::createBodyLayer);
		event.registerLayerDefinition(Modelsculkbosss.LAYER_LOCATION, Modelsculkbosss::createBodyLayer);
		event.registerLayerDefinition(Modelsculkphantom.LAYER_LOCATION, Modelsculkphantom::createBodyLayer);
	}
}
