
package sculk.of.ixra.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class BucketFilledWithSculkItem extends Item {
	public BucketFilledWithSculkItem() {
		super(new Item.Properties().stacksTo(26).rarity(Rarity.RARE));
	}
}
