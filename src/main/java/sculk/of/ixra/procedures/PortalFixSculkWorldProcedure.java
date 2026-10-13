package sculk.of.ixra.procedures;

import sculk.of.ixra.init.SculksOfArdaModItems;

import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

@EventBusSubscriber
public class PortalFixSculkWorldProcedure {
	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getHand() != event.getEntity().getUsedItemHand())
			return;
		execute(event, event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getLevel().getBlockState(event.getPos()), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
		execute(null, world, x, y, z, blockstate, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
		if (entity == null)
			return;
		double nRadius = 0;
		double nX = 0;
		double nY = 0;
		double nZ = 0;
		BlockState blBlockToRemove = Blocks.AIR.defaultBlockState();
		BlockState blPortalBlock = Blocks.AIR.defaultBlockState();
		blPortalBlock = Blocks.REINFORCED_DEEPSLATE.defaultBlockState();
		if (blockstate.getBlock() == blPortalBlock.getBlock() && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == SculksOfArdaModItems.SCULK_WORLD.get()) {
			blBlockToRemove = Blocks.SCULK_VEIN.defaultBlockState();
			nRadius = 32;
			nX = x - nRadius;
			nY = y - nRadius;
			nZ = z - nRadius;
			for (int index0 = 0; index0 < (int) (nRadius * 2 + 1); index0++) {
				for (int index1 = 0; index1 < (int) (nRadius * 2 + 1); index1++) {
					for (int index2 = 0; index2 < (int) (nRadius * 2 + 1); index2++) {
						if ((world.getBlockState(BlockPos.containing(nX, nY, nZ))).getBlock() == blPortalBlock.getBlock()) {
							if ((world.getBlockState(BlockPos.containing(nX, nY, nZ - 1))).getBlock() == blBlockToRemove.getBlock()) {
								world.setBlock(BlockPos.containing(nX, nY, nZ - 1), Blocks.AIR.defaultBlockState(), 3);
							}
							if ((world.getBlockState(BlockPos.containing(nX + 1, nY, nZ))).getBlock() == blBlockToRemove.getBlock()) {
								world.setBlock(BlockPos.containing(nX + 1, nY, nZ), Blocks.AIR.defaultBlockState(), 3);
							}
							if ((world.getBlockState(BlockPos.containing(nX, nY, nZ + 1))).getBlock() == blBlockToRemove.getBlock()) {
								world.setBlock(BlockPos.containing(nX, nY, nZ + 1), Blocks.AIR.defaultBlockState(), 3);
							}
							if ((world.getBlockState(BlockPos.containing(nX - 1, nY, nZ))).getBlock() == blBlockToRemove.getBlock()) {
								world.setBlock(BlockPos.containing(nX - 1, nY, nZ), Blocks.AIR.defaultBlockState(), 3);
							}
							if ((world.getBlockState(BlockPos.containing(nX, nY + 1, nZ))).getBlock() == blBlockToRemove.getBlock()) {
								world.setBlock(BlockPos.containing(nX, nY + 1, nZ), Blocks.AIR.defaultBlockState(), 3);
							}
							if ((world.getBlockState(BlockPos.containing(nX, nY - 1, nZ))).getBlock() == blBlockToRemove.getBlock()) {
								world.setBlock(BlockPos.containing(nX, nY - 1, nZ), Blocks.AIR.defaultBlockState(), 3);
							}
						}
						nZ = nZ + 1;
					}
					nZ = z - nRadius;
					nX = nX + 1;
				}
				nZ = z - nRadius;
				nX = x - nRadius;
				nY = nY + 1;
			}
		}
	}
}
