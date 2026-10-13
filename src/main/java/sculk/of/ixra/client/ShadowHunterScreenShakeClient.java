
package sculk.of.ixra.client;

import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

/**
 * ShadowHunter'in tekmesi isabet ettiginde, sadece isabet alan oyuncunun
 * ekraninda cok hafif ve kisa sureli bir sallanma (camera shake) verir.
 */
@EventBusSubscriber({Dist.CLIENT})
public class ShadowHunterScreenShakeClient {

	private static int ticksRemaining = 0;
	private static int totalTicks = 1;

	/** Sallanmayi baslatir. durationTicks tipik olarak 6-8 civari (~0.3-0.4 saniye). */
	public static void startShake(int durationTicks) {
		totalTicks = Math.max(1, durationTicks);
		ticksRemaining = totalTicks;
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event) {
		if (ticksRemaining > 0)
			ticksRemaining--;
	}

	@SubscribeEvent
	public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
		if (ticksRemaining <= 0)
			return;

		// Solup giden (fade-out) bir sagasol titremesi: genlik zamanla azalir.
		float progress = ticksRemaining / (float) totalTicks;
		double magnitudeDegrees = 5.0D;
		double wobble = Math.sin(ticksRemaining * 3.2D) * magnitudeDegrees * progress;

		event.setRoll((float) (event.getRoll() + wobble));
	}
}
