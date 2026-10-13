
package sculk.of.ixra.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class GoldenSculkSensorPartItem extends Item {
	public GoldenSculkSensorPartItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.RARE));
	}
}
