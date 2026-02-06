
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package sculk.of.ixra.init;

import sculk.of.ixra.SculksOfArdaMod;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;

public class SculksOfArdaModParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, SculksOfArdaMod.MODID);
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RADIOACTIVE_SONIC_BOOM = REGISTRY.register("radioactive_sonic_boom", () -> new SimpleParticleType(false));
}
