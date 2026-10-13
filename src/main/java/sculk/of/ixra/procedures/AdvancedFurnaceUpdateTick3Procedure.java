package sculk.of.ixra.procedures;

import sculk.of.ixra.init.SculksOfArdaModItems;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.common.extensions.ILevelExtension;
import net.neoforged.neoforge.capabilities.Capabilities;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class AdvancedFurnaceUpdateTick3Procedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
		double previousRecipe = 0;
		double craftingTime = 0;
		double fireHeight = 0;
		craftingTime = getPersistentDouble(world, BlockPos.containing(x, y, z), "recipe");
		if ((getSlotStack(world, BlockPos.containing(x, y, z), 0)).getItem() == SculksOfArdaModItems.MOLTEN_SCULK_BUCKET.get() && (getSlotStack(world, BlockPos.containing(x, y, z), 1)).getItem() == SculksOfArdaModItems.MOLTEN_IRON_BUCKET.get() && (getSlotStack(world, BlockPos.containing(x, y, z), 3)).getItem() == Items.NETHERITE_INGOT && ((getSlotStack(world, BlockPos.containing(x, y, z), 4)).getItem() == SculksOfArdaModItems.SCULK_INGOT.get() && getSlotAmount(world, BlockPos.containing(x, y, z), 4) <= 62 && (getSlotStack(world, BlockPos.containing(x, y, z), 5)).getItem() == Items.BUCKET && getSlotAmount(world, BlockPos.containing(x, y, z), 5) <= 62 || getSlotAmount(world, BlockPos.containing(x, y, z), 4) == 0)) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null)
					_blockEntity.getPersistentData().putDouble("recipe", 0);
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		} else if ((getSlotStack(world, BlockPos.containing(x, y, z), 0)).getItem() == SculksOfArdaModItems.MOLTEN_IRON_BUCKET.get() && (getSlotStack(world, BlockPos.containing(x, y, z), 1)).getItem() == SculksOfArdaModItems.MOLTEN_SCULK_BUCKET.get() && (getSlotStack(world, BlockPos.containing(x, y, z), 3)).getItem() == Items.EMERALD && ((getSlotStack(world, BlockPos.containing(x, y, z), 4)).getItem() == SculksOfArdaModItems.SCULKED_EMERALD.get() && getSlotAmount(world, BlockPos.containing(x, y, z), 4) <= 62 && (getSlotStack(world, BlockPos.containing(x, y, z), 5)).getItem() == Items.BUCKET && getSlotAmount(world, BlockPos.containing(x, y, z), 5) <= 62 || getSlotAmount(world, BlockPos.containing(x, y, z), 4) == 0)) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null)
					_blockEntity.getPersistentData().putDouble("recipe", 1);
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		} else if ((getSlotStack(world, BlockPos.containing(x, y, z), 0)).getItem() == SculksOfArdaModItems.MOLTEN_SCULK_BUCKET.get() && (getSlotStack(world, BlockPos.containing(x, y, z), 1)).getItem() == SculksOfArdaModItems.MOLTEN_IRON_BUCKET.get() && (getSlotStack(world, BlockPos.containing(x, y, z), 3)).getItem() == Items.EMERALD && ((getSlotStack(world, BlockPos.containing(x, y, z), 4)).getItem() == SculksOfArdaModItems.SCULKED_EMERALD.get() && getSlotAmount(world, BlockPos.containing(x, y, z), 4) <= 62 && (getSlotStack(world, BlockPos.containing(x, y, z), 5)).getItem() == Items.BUCKET && getSlotAmount(world, BlockPos.containing(x, y, z), 5) <= 62 || getSlotAmount(world, BlockPos.containing(x, y, z), 4) == 0)) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null)
					_blockEntity.getPersistentData().putDouble("recipe", 2);
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		} else if ((getSlotStack(world, BlockPos.containing(x, y, z), 0)).getItem() == SculksOfArdaModItems.MOLTEN_IRON_BUCKET.get() && (getSlotStack(world, BlockPos.containing(x, y, z), 1)).getItem() == SculksOfArdaModItems.MOLTEN_SCULK_BUCKET.get() && (getSlotStack(world, BlockPos.containing(x, y, z), 3)).getItem() == Items.NETHERITE_INGOT && ((getSlotStack(world, BlockPos.containing(x, y, z), 4)).getItem() == SculksOfArdaModItems.SCULK_INGOT.get() && getSlotAmount(world, BlockPos.containing(x, y, z), 4) <= 62 && (getSlotStack(world, BlockPos.containing(x, y, z), 5)).getItem() == Items.BUCKET && getSlotAmount(world, BlockPos.containing(x, y, z), 5) <= 62 || getSlotAmount(world, BlockPos.containing(x, y, z), 4) == 0)) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null)
					_blockEntity.getPersistentData().putDouble("recipe", 3);
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		} else {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null)
					_blockEntity.getPersistentData().putDouble("recipe", (-1));
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		}
		if (craftingTime != getPersistentDouble(world, BlockPos.containing(x, y, z), "recipe")) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null)
					_blockEntity.getPersistentData().putDouble("timer", 0);
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		}
		if (getPersistentDouble(world, BlockPos.containing(x, y, z), "recipe") >= 0) {
			if (getPersistentDouble(world, BlockPos.containing(x, y, z), "timer") >= 20) {
				if (getPersistentDouble(world, BlockPos.containing(x, y, z), "recipe") == 0) {
					{
						int _value = 1;
						BlockPos _pos = BlockPos.containing(x, y, z);
						BlockState _bs = world.getBlockState(_pos);
						if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
							world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
					}
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
						int _slotid = 3;
						ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
						_stk.shrink(1);
						_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
					}
					if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
						ItemStack _setstack = new ItemStack(SculksOfArdaModItems.SCULK_INGOT.get()).copy();
						_setstack.setCount((int) (getSlotAmount(world, BlockPos.containing(x, y, z), 4) + 1));
						_itemHandlerModifiable.setStackInSlot(4, _setstack);
					}
					if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
						ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
						_setstack.setCount((int) (getSlotAmount(world, BlockPos.containing(x, y, z), 5) + 2));
						_itemHandlerModifiable.setStackInSlot(5, _setstack);
					}
				} else if (getPersistentDouble(world, BlockPos.containing(x, y, z), "recipe") == 1) {
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
						int _slotid = 3;
						ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
						_stk.shrink(1);
						_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
					}
					if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
						ItemStack _setstack = new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get()).copy();
						_setstack.setCount((int) (getSlotAmount(world, BlockPos.containing(x, y, z), 4) + 1));
						_itemHandlerModifiable.setStackInSlot(4, _setstack);
					}
					if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
						ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
						_setstack.setCount((int) (getSlotAmount(world, BlockPos.containing(x, y, z), 5) + 2));
						_itemHandlerModifiable.setStackInSlot(5, _setstack);
					}
				} else if (getPersistentDouble(world, BlockPos.containing(x, y, z), "recipe") == 2) {
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
						int _slotid = 3;
						ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
						_stk.shrink(1);
						_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
					}
					if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
						ItemStack _setstack = new ItemStack(SculksOfArdaModItems.SCULKED_EMERALD.get()).copy();
						_setstack.setCount((int) (getSlotAmount(world, BlockPos.containing(x, y, z), 4) + 1));
						_itemHandlerModifiable.setStackInSlot(4, _setstack);
					}
					if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
						ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
						_setstack.setCount((int) (getSlotAmount(world, BlockPos.containing(x, y, z), 5) + 2));
						_itemHandlerModifiable.setStackInSlot(5, _setstack);
					}
				} else if (getPersistentDouble(world, BlockPos.containing(x, y, z), "recipe") == 3) {
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
						int _slotid = 3;
						ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
						_stk.shrink(1);
						_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
					}
					if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
						ItemStack _setstack = new ItemStack(SculksOfArdaModItems.SCULK_INGOT.get()).copy();
						_setstack.setCount((int) (getSlotAmount(world, BlockPos.containing(x, y, z), 4) + 1));
						_itemHandlerModifiable.setStackInSlot(4, _setstack);
					}
					if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
						ItemStack _setstack = new ItemStack(Items.BUCKET).copy();
						_setstack.setCount((int) (getSlotAmount(world, BlockPos.containing(x, y, z), 5) + 2));
						_itemHandlerModifiable.setStackInSlot(5, _setstack);
					}
				}
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null)
						_blockEntity.getPersistentData().putDouble("timer", 0);
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
				{
					int _value = 2;
					BlockPos _pos = BlockPos.containing(x, y, z);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
						world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
				}
			}
			if (getPersistentDouble(world, BlockPos.containing(x, y, z), "fuel") == 0) {
				if (getSlotAmount(world, BlockPos.containing(x, y, z), 2) > 0 && getSlotAmount(world, BlockPos.containing(x, y, z), 0) != 0 && getSlotAmount(world, BlockPos.containing(x, y, z), 1) != 0 && getSlotAmount(world, BlockPos.containing(x, y, z), 3) != 0) {
					if ((getSlotStack(world, BlockPos.containing(x, y, z), 2)).getItem() == Blocks.COAL_BLOCK.asItem()) {
						if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
							int _slotid = 2;
							ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
							_stk.shrink(1);
							_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
						}
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null)
								_blockEntity.getPersistentData().putDouble("fuel", 65);
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null)
								_blockEntity.getPersistentData().putDouble("maxFuel", 65);
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					} else if ((getSlotStack(world, BlockPos.containing(x, y, z), 2)).getItem() == Items.COAL) {
						if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
							int _slotid = 2;
							ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
							_stk.shrink(1);
							_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
						}
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null)
								_blockEntity.getPersistentData().putDouble("fuel", 25);
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null)
								_blockEntity.getPersistentData().putDouble("maxFuel", 25);
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					} else if ((getSlotStack(world, BlockPos.containing(x, y, z), 2)).getItem() == Items.CHARCOAL) {
						if (world instanceof ILevelExtension _ext && _ext.getCapability(Capabilities.ItemHandler.BLOCK, BlockPos.containing(x, y, z), null) instanceof IItemHandlerModifiable _itemHandlerModifiable) {
							int _slotid = 2;
							ItemStack _stk = _itemHandlerModifiable.getStackInSlot(_slotid).copy();
							_stk.shrink(1);
							_itemHandlerModifiable.setStackInSlot(_slotid, _stk);
						}
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null)
								_blockEntity.getPersistentData().putDouble("fuel", 25);
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
						if (!world.isClientSide()) {
							BlockPos _bp = BlockPos.containing(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null)
								_blockEntity.getPersistentData().putDouble("maxFuel", 25);
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					}
				} else {
					if (!world.isClientSide()) {
						BlockPos _bp = BlockPos.containing(x, y, z);
						BlockEntity _blockEntity = world.getBlockEntity(_bp);
						BlockState _bs = world.getBlockState(_bp);
						if (_blockEntity != null)
							_blockEntity.getPersistentData().putDouble("timer", 0);
						if (world instanceof Level _level)
							_level.sendBlockUpdated(_bp, _bs, _bs, 3);
					}
				}
			}
		}
		if (getPersistentDouble(world, BlockPos.containing(x, y, z), "fuel") > 0) {
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null)
					_blockEntity.getPersistentData().putDouble("fuel", ((getPersistentDouble(world, BlockPos.containing(x, y, z), "fuel")) - 1));
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
			if (getPersistentDouble(world, BlockPos.containing(x, y, z), "recipe") >= 0) {
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(x, y, z);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null)
						_blockEntity.getPersistentData().putDouble("timer", (getPersistentDouble(world, BlockPos.containing(x, y, z), "timer") + 1));
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
		}
		if (!world.isClientSide()) {
			BlockPos _bp = BlockPos.containing(x, y, z);
			BlockEntity _blockEntity = world.getBlockEntity(_bp);
			BlockState _bs = world.getBlockState(_bp);
			if (_blockEntity != null)
				_blockEntity.getPersistentData().putDouble("fuelRemaining", (((getPersistentDouble(world, BlockPos.containing(x, y, z), "fuel")) / (getPersistentDouble(world, BlockPos.containing(x, y, z), "maxFuel"))) * 100));
			if (world instanceof Level _level)
				_level.sendBlockUpdated(_bp, _bs, _bs, 3);
		}
		if (getPersistentDouble(world, BlockPos.containing(x, y, z), "fuel") > 0) {
			if ((blockstate.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip129 ? blockstate.getValue(_getip129) : -1) == 0) {
				{
					int _value = 1;
					BlockPos _pos = BlockPos.containing(x, y, z);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
						world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
				}
			}
			fireHeight = ((getPersistentDouble(world, BlockPos.containing(x, y, z), "fuelRemaining")) / 100) * 0.0625;
			if ((getFacingDirection(world, BlockPos.containing(x, y, z))) == Direction.NORTH) {
				world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, (x + 0.5), (y + 0.2), (z - 0.1), 0, fireHeight, 0);
			} else if ((getFacingDirection(world, BlockPos.containing(x, y, z))) == Direction.SOUTH) {
				world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, (x + 0.5), (y + 0.2), (z + 1.1), 0, fireHeight, 0);
			} else if ((getFacingDirection(world, BlockPos.containing(x, y, z))) == Direction.WEST) {
				world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, (x - 0.1), (y + 0.2), (z + 0.5), 0, fireHeight, 0);
			} else if ((getFacingDirection(world, BlockPos.containing(x, y, z))) == Direction.EAST) {
				world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, (x + 1.1), (y + 0.2), (z + 0.5), 0, fireHeight, 0);
			}
			if (Math.random() < 0.2) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.furnace.fire_crackle")), SoundSource.BLOCKS, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("block.furnace.fire_crackle")), SoundSource.BLOCKS, 1, 1, false);
					}
				}
			}
		} else if (getPersistentDouble(world, BlockPos.containing(x, y, z), "fuel") == 0) {
			if ((blockstate.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _getip147 ? blockstate.getValue(_getip147) : -1) == 1) {
				{
					int _value = 0;
					BlockPos _pos = BlockPos.containing(x, y, z);
					BlockState _bs = world.getBlockState(_pos);
					if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
						world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
				}
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

	private static int getSlotAmount(LevelAccessor world, BlockPos pos, int slotid) {
		if (world instanceof ILevelExtension _ext) {
			IItemHandler _itemHandler = _ext.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
			if (_itemHandler != null)
				return _itemHandler.getStackInSlot(slotid).getCount();
		}
		return 0;
	}

	private static double getPersistentDouble(LevelAccessor world, BlockPos pos, String tag) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
			return blockEntity.getPersistentData().getDouble(tag);
		return -1;
	}

	private static Direction getFacingDirection(LevelAccessor world, BlockPos pos) {
		BlockState _bs = world.getBlockState(pos);
		Property<?> property = _bs.getBlock().getStateDefinition().getProperty("facing");
		if (property != null && _bs.getValue(property) instanceof Direction _dir)
			return _dir;
		else if (_bs.hasProperty(BlockStateProperties.AXIS))
			return Direction.fromAxisAndDirection(_bs.getValue(BlockStateProperties.AXIS), Direction.AxisDirection.POSITIVE);
		else if (_bs.hasProperty(BlockStateProperties.HORIZONTAL_AXIS))
			return Direction.fromAxisAndDirection(_bs.getValue(BlockStateProperties.HORIZONTAL_AXIS), Direction.AxisDirection.POSITIVE);
		return Direction.NORTH;
	}
}
