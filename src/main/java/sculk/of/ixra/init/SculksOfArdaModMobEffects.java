
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package sculk.of.ixra.init;

import sculk.of.ixra.potion.WardenPowerMobEffect;
import sculk.of.ixra.SculksOfArdaMod;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.registries.Registries;

public class SculksOfArdaModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, SculksOfArdaMod.MODID);
	public static final DeferredHolder<MobEffect, MobEffect> WARDEN_POWER = REGISTRY.register("warden_power", () -> new WardenPowerMobEffect());
}
