
package sculk.of.ixra.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class BucketFilledWithRawIronItem extends Item {
	public BucketFilledWithRawIronItem() {
		super(new Item.Properties().stacksTo(26).rarity(Rarity.RARE));
	}
}
