
package sculk.of.ixra.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class MoltenSculkBucketItem extends Item {
	public MoltenSculkBucketItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.RARE));
	}
}
