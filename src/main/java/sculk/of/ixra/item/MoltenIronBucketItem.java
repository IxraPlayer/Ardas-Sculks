
package sculk.of.ixra.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class MoltenIronBucketItem extends Item {
	public MoltenIronBucketItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.RARE));
	}
}
