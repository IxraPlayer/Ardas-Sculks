
package sculk.of.ixra.item;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class DeepDarkUpgradeTemplateItem extends Item {
	public DeepDarkUpgradeTemplateItem() {
		super(new Item.Properties().stacksTo(4).fireResistant().rarity(Rarity.EPIC));
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack itemstack, BlockState state) {
		return true;
	}
}
