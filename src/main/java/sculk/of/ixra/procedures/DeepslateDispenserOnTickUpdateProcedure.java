package sculk.of.ixra.procedures;

import sculk.of.ixra.init.SculksOfArdaModItems;
import sculk.of.ixra.init.SculksOfArdaModBlocks;
import sculk.of.ixra.SculksOfArdaMod;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class DeepslateDispenserOnTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if ((getSlotStack(world, BlockPos.containing(x, y, z), 9)).getItem() == SculksOfArdaModItems.SCULKERITE_PAXEL.get() && (getSlotStack(world, BlockPos.containing(x, y, z), 10)).getItem() == SculksOfArdaModItems.SCULKRESINBRICK.get()) {
			if ((getSlotStack(world, BlockPos.containing(x, y, z), 0)).getItem() == Blocks.DEEPSLATE.asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 1)).getItem() == Blocks.DEEPSLATE.asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 2)).getItem() == Blocks.DEEPSLATE.asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 3)).getItem() == Blocks.DEEPSLATE.asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 4)).getItem() == Blocks.DEEPSLATE.asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 5)).getItem() == Blocks.DEEPSLATE.asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 6)).getItem() == Blocks.DEEPSLATE.asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 7)).getItem() == Blocks.DEEPSLATE.asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 8)).getItem() == Blocks.DEEPSLATE.asItem()) {
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 0;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 1;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 2;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 3;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 4;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 5;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 6;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 7;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 8;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 10;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, dispenseX(world, x, y, z), dispenseY(world, x, y, z), dispenseZ(world, x, y, z), new ItemStack(SculksOfArdaModBlocks.DEEP_GRANULITE_STONE.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
				{
					int _value = 1;
					BlockPos _pos = BlockPos.containing(x, y, z);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
						world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
				}
				SculksOfArdaMod.queueServerWork(10, () -> {
					{
						int _value = 0;
						BlockPos _pos = BlockPos.containing(x, y, z);
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
							world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
					}
				});
			}
			if ((getSlotStack(world, BlockPos.containing(x, y, z), 0)).getItem() == SculksOfArdaModBlocks.DEEP_GRANULITE_STONE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 1)).getItem() == SculksOfArdaModBlocks.DEEP_GRANULITE_STONE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 2)).getItem() == SculksOfArdaModBlocks.DEEP_GRANULITE_STONE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 3)).getItem() == SculksOfArdaModBlocks.DEEP_GRANULITE_STONE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 4)).getItem() == SculksOfArdaModBlocks.DEEP_GRANULITE_STONE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 5)).getItem() == SculksOfArdaModBlocks.DEEP_GRANULITE_STONE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 6)).getItem() == SculksOfArdaModBlocks.DEEP_GRANULITE_STONE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 7)).getItem() == SculksOfArdaModBlocks.DEEP_GRANULITE_STONE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 8)).getItem() == SculksOfArdaModBlocks.DEEP_GRANULITE_STONE.get().asItem()) {
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 0;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 1;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 2;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 3;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 4;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 5;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 6;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 7;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 8;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 10;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, dispenseX(world, x, y, z), dispenseY(world, x, y, z), dispenseZ(world, x, y, z), new ItemStack(SculksOfArdaModBlocks.POLISHED_DEEP_GRANULITE.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, dispenseX(world, x, y, z), dispenseY(world, x, y, z), dispenseZ(world, x, y, z), new ItemStack(SculksOfArdaModBlocks.POLISHED_DEEP_GRANULITE.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, dispenseX(world, x, y, z), dispenseY(world, x, y, z), dispenseZ(world, x, y, z), new ItemStack(SculksOfArdaModBlocks.POLISHED_DEEP_GRANULITE.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
				{
					int _value = 1;
					BlockPos _pos = BlockPos.containing(x, y, z);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
						world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
				}
				SculksOfArdaMod.queueServerWork(10, () -> {
					{
						int _value = 0;
						BlockPos _pos = BlockPos.containing(x, y, z);
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
							world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
					}
				});
			}
			if ((getSlotStack(world, BlockPos.containing(x, y, z), 0)).getItem() == SculksOfArdaModBlocks.POLISHED_DEEP_GRANULITE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 1)).getItem() == SculksOfArdaModBlocks.POLISHED_DEEP_GRANULITE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 2)).getItem() == SculksOfArdaModBlocks.POLISHED_DEEP_GRANULITE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 3)).getItem() == SculksOfArdaModBlocks.POLISHED_DEEP_GRANULITE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 4)).getItem() == SculksOfArdaModBlocks.POLISHED_DEEP_GRANULITE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 5)).getItem() == SculksOfArdaModBlocks.POLISHED_DEEP_GRANULITE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 6)).getItem() == SculksOfArdaModBlocks.POLISHED_DEEP_GRANULITE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 7)).getItem() == SculksOfArdaModBlocks.POLISHED_DEEP_GRANULITE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 8)).getItem() == SculksOfArdaModBlocks.POLISHED_DEEP_GRANULITE.get().asItem()) {
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 0;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 1;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 2;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 3;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 4;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 5;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 6;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 7;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 8;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 10;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, dispenseX(world, x, y, z), dispenseY(world, x, y, z), dispenseZ(world, x, y, z), new ItemStack(SculksOfArdaModBlocks.COMPRESSED_DEEP_GRANULITE.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, dispenseX(world, x, y, z), dispenseY(world, x, y, z), dispenseZ(world, x, y, z), new ItemStack(SculksOfArdaModBlocks.COMPRESSED_DEEP_GRANULITE.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
				{
					int _value = 1;
					BlockPos _pos = BlockPos.containing(x, y, z);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
						world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
				}
				SculksOfArdaMod.queueServerWork(10, () -> {
					{
						int _value = 0;
						BlockPos _pos = BlockPos.containing(x, y, z);
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
							world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
					}
				});
			}
			if ((getSlotStack(world, BlockPos.containing(x, y, z), 0)).getItem() == SculksOfArdaModBlocks.COMPRESSED_DEEP_GRANULITE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 1)).getItem() == SculksOfArdaModBlocks.COMPRESSED_DEEP_GRANULITE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 2)).getItem() == SculksOfArdaModBlocks.COMPRESSED_DEEP_GRANULITE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 3)).getItem() == SculksOfArdaModBlocks.COMPRESSED_DEEP_GRANULITE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 4)).getItem() == SculksOfArdaModBlocks.COMPRESSED_DEEP_GRANULITE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 5)).getItem() == SculksOfArdaModBlocks.COMPRESSED_DEEP_GRANULITE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 6)).getItem() == SculksOfArdaModBlocks.COMPRESSED_DEEP_GRANULITE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 7)).getItem() == SculksOfArdaModBlocks.COMPRESSED_DEEP_GRANULITE.get().asItem() && (getSlotStack(world, BlockPos.containing(x, y, z), 8)).getItem() == SculksOfArdaModBlocks.COMPRESSED_DEEP_GRANULITE.get().asItem()) {
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 0;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 1;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 2;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 3;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 4;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 5;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 6;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 7;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 8;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 10;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, dispenseX(world, x, y, z), dispenseY(world, x, y, z), dispenseZ(world, x, y, z), new ItemStack(Blocks.OBSIDIAN));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
				{
					int _value = 1;
					BlockPos _pos = BlockPos.containing(x, y, z);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
						world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
				}
				SculksOfArdaMod.queueServerWork(10, () -> {
					{
						int _value = 0;
						BlockPos _pos = BlockPos.containing(x, y, z);
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
							world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
					}
				});
			}
			if ((getSlotStack(world, BlockPos.containing(x, y, z), 0)).getItem() == SculksOfArdaModItems.SCULKERITE_NUGGET.get() && (getSlotStack(world, BlockPos.containing(x, y, z), 1)).getItem() == SculksOfArdaModItems.SCULKERITE_NUGGET.get() && (getSlotStack(world, BlockPos.containing(x, y, z), 2)).getItem() == SculksOfArdaModItems.SCULKERITE_NUGGET.get() && (getSlotStack(world, BlockPos.containing(x, y, z), 3)).getItem() == SculksOfArdaModItems.SCULKERITE_NUGGET.get() && (getSlotStack(world, BlockPos.containing(x, y, z), 4)).getItem() == Items.BOW && (getSlotStack(world, BlockPos.containing(x, y, z), 5)).getItem() == SculksOfArdaModItems.SCULKERITE_NUGGET.get() && (getSlotStack(world, BlockPos.containing(x, y, z), 6)).getItem() == SculksOfArdaModItems.SCULKERITE_NUGGET.get() && (getSlotStack(world, BlockPos.containing(x, y, z), 7)).getItem() == SculksOfArdaModItems.SCULKERITE_NUGGET.get() && (getSlotStack(world, BlockPos.containing(x, y, z), 8)).getItem() == SculksOfArdaModItems.SCULKERITE_NUGGET.get()) {
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 0;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 1;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 2;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 3;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 4;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 5;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 6;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 7;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 8;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
					int _slotid = 10;
					ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
					_stk.shrink(1);
					_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
				}
				if (world instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, dispenseX(world, x, y, z), dispenseY(world, x, y, z), dispenseZ(world, x, y, z), new ItemStack(SculksOfArdaModItems.SCULK_BOW.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
				{
					int _value = 1;
					BlockPos _pos = BlockPos.containing(x, y, z);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
						world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
				}
				SculksOfArdaMod.queueServerWork(10, () -> {
					{
						int _value = 0;
						BlockPos _pos = BlockPos.containing(x, y, z);
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
							world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
					}
				});
			}
		}
	}
	private static ItemStack getSlotStack(LevelAccessor world, BlockPos pos, int slotid) {
		if (world instanceof ILevelExtension _ext) {
			IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
			if (_itemHandler != null)
				return _itemHandler.getStackInSlot(slotid).copy();
		}
		return ItemStack.EMPTY;
	}
	private static Direction getFacing(LevelAccessor world, double x, double y, double z) {
		BlockState blockState = world.getBlockState(BlockPos.containing(x, y, z));
		if (blockState.getBlock().getStateDefinition().getProperty("facing") instanceof DirectionProperty facingProp)
			return blockState.getValue(facingProp);
		return Direction.NORTH;
	}

	private static double dispenseX(LevelAccessor world, double x, double y, double z) {
		return x + 0.5 + getFacing(world, x, y, z).getStepX() * 0.7;
	}

	private static double dispenseY(LevelAccessor world, double x, double y, double z) {
		return y + 0.5;
	}

	private static double dispenseZ(LevelAccessor world, double x, double y, double z) {
		return z + 0.5 + getFacing(world, x, y, z).getStepZ() * 0.7;
	}
}
