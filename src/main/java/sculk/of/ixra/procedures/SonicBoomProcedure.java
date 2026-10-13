package sculk.of.ixra.procedures;

import sculk.of.ixra.init.SculksOfArdaModItems;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Comparator;

public class SonicBoomProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;

		if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == SculksOfArdaModItems.WARDEN_ARMOR_HELMET.get()
				&& (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getItem() == SculksOfArdaModItems.WARDEN_ARMOR_CHESTPLATE.get()
				&& (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem() == SculksOfArdaModItems.WARDEN_ARMOR_LEGGINGS.get()
				&& (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem() == SculksOfArdaModItems.WARDEN_ARMOR_BOOTS.get()
				&& (entity instanceof Player _plrCldRem9 ? _plrCldRem9.getCooldowns().getCooldownPercent((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem(), 0f) * 100 : 0) == 0
				&& (entity instanceof Player _plrCldRem11
						? _plrCldRem11.getCooldowns().getCooldownPercent((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getItem(), 0f) * 100
						: 0) == 0
				&& (entity instanceof Player _plrCldRem13 ? _plrCldRem13.getCooldowns().getCooldownPercent((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem(), 0f) * 100 : 0) == 0
				&& (entity instanceof Player _plrCldRem15
						? _plrCldRem15.getCooldowns().getCooldownPercent((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem(), 0f) * 100
						: 0) == 0) {
			if (entity.isShiftKeyDown()) {
				Vec3 viewDir = entity.getViewVector(1f);
				for (int index0 = 0; index0 < 5; index0++) {
					double distance = index0 + 1;
					Vec3 eyePos = entity.getEyePosition(1f);
					Vec3 targetPos = eyePos.add(viewDir.scale(distance));
					BlockPos clipPos = entity.level()
							.clip(new ClipContext(eyePos, targetPos, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entity))
							.getBlockPos();

					// Use vanilla's own sonic boom particle so it looks and moves exactly like the
					// warden's real attack, with the real forward direction as its velocity.
					world.addParticle(ParticleTypes.SONIC_BOOM, clipPos.getX() + 0.5, clipPos.getY() + 0.5, clipPos.getZ() + 0.5, viewDir.x * 0.6, viewDir.y * 0.6, viewDir.z * 0.6);

					Vec3 center = new Vec3(clipPos.getX(), clipPos.getY(), clipPos.getZ());
					List<Entity> found = world.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(3 / 2d), e -> true)
							.stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList();
					for (Entity entityiterator : found) {
						if (!(entityiterator == entity)) {
							entityiterator.hurt(new DamageSource(world.holderOrThrow(DamageTypes.SONIC_BOOM)), 5);
						}
					}
				}
				if (entity instanceof Player _player)
					_player.getCooldowns().addCooldown((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem(), 1200);
				if (entity instanceof Player _player)
					_player.getCooldowns().addCooldown((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getItem(), 1200);
				if (entity instanceof Player _player)
					_player.getCooldowns().addCooldown((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem(), 1200);
				if (entity instanceof Player _player)
					_player.getCooldowns().addCooldown((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem(), 1200);
			}
		}
	}
}
