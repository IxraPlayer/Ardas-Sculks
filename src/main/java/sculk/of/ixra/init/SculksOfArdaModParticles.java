
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package sculk.of.ixra.init;

import sculk.of.ixra.client.particle.RadioactiveSonicBoomParticle;

import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SculksOfArdaModParticles {
	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(SculksOfArdaModParticleTypes.RADIOACTIVE_SONIC_BOOM.get(), RadioactiveSonicBoomParticle::provider);
	}
}
