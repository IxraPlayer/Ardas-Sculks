
package sculk.of.ixra.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class RadioactiveSculkPieceItem extends Item {
	public RadioactiveSculkPieceItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON));
	}
}
