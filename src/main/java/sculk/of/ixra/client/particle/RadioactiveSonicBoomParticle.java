
package sculk.of.ixra.client.particle;

import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;

@OnlyIn(Dist.CLIENT)
public class RadioactiveSonicBoomParticle extends TextureSheetParticle {
	public static RadioactiveSonicBoomParticleProvider provider(SpriteSet spriteSet) {
		return new RadioactiveSonicBoomParticleProvider(spriteSet);
	}

	public static class RadioactiveSonicBoomParticleProvider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet spriteSet;

		public RadioactiveSonicBoomParticleProvider(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new RadioactiveSonicBoomParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
		}
	}

	private final SpriteSet spriteSet;

	private final float startQuadSize;
	private final float endQuadSize;

	protected RadioactiveSonicBoomParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
		super(world, x, y, z);
		this.spriteSet = spriteSet;
		this.setSize(0.6f, 0.6f);
		this.startQuadSize = this.quadSize * 1.5f;
		this.endQuadSize = this.quadSize * 7f;
		this.quadSize = this.startQuadSize;
		this.lifetime = 12;
		this.gravity = 0f;
		this.hasPhysics = false;
		// vx/vy/vz carry the direction the boom travels in (see SonicBoomProcedure), so keep
		// them as the real velocity instead of zeroing them out - that's what made it stand still.
		this.xd = vx;
		this.yd = vy;
		this.zd = vz;
		this.pickSprite(spriteSet);
	}

	@Override
	public int getLightColor(float partialTick) {
		return 15728880;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_LIT;
	}

	@Override
	public void tick() {
		super.tick();
		// Grow the particle outward over its lifetime instead of staying a fixed size, and fade
		// it out near the end so the "boom" reads as an expanding wave rather than a static blob.
		float progress = Mth.clamp((float) this.age / (float) this.lifetime, 0f, 1f);
		this.quadSize = Mth.lerp(progress, this.startQuadSize, this.endQuadSize);
		this.alpha = 1f - progress;
	}
}
