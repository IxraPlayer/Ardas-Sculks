package sculk.of.ixra.bossheads;

import net.neoforged.fml.loading.FMLEnvironment;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Direction;

/** Floor + wall block item. Drawn by BossHeadItemRenderer (spins in the inventory). */
public class BossHeadItem extends StandingAndWallBlockItem implements Equipable {
	/** Sculk Golem + Nyxar: blue name, turquoise glow */
	private static final int BLUE_BASE = 0x2F6BFF;
	private static final int BLUE_GLOW = 0x2EF2E0;
	/** Radioactive Warden: green name, light green glow */
	private static final int GREEN_BASE = 0x14F23C;
	private static final int GREEN_GLOW = 0xD4FF3D;

	/** Nyxar phase 2: purple name, light purple glow */
	private static final int PURPLE_BASE = 0x9B30FF;
	private static final int PURPLE_GLOW = 0xE9A8FF;

	/** how many letters wide the glow is on each side of its centre */
	private static final double GLOW_WIDTH = 3.5;
	/** how long (ms) the glow takes to sweep through the name once */
	private static final double SWEEP_MS = 2400.0;
	/** gap (in letters) between two sweeps so the name rests in its plain colour for a moment */
	private static final double PAUSE_LETTERS = 4.0;

	private final BossHeadType type;

	public BossHeadItem(BossHeadType type, Block floorBlock, Block wallBlock) {
		super(floorBlock, wallBlock, new Item.Properties().rarity(Rarity.RARE), Direction.DOWN);
		this.type = type;
	}

	public BossHeadType getType() {
		return type;
	}

	/** the head can be worn: it fits the helmet slot (also by shift-clicking / dragging in the inventory) */
	@Override
	public EquipmentSlot getEquipmentSlot() {
		return EquipmentSlot.HEAD;
	}

	/** right click in the air puts it on (clicking a block still places it) */
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		return this.swapWithEquipmentSlot(this, level, player, hand);
	}

	/**
	 * Bold name in the head's colour (blue, or green for the Radioactive Warden) where a brighter glow slides over the
	 * letters one by one with a smooth fade in and out. Re-evaluated every frame the name is drawn
	 * (tooltip, hotbar name), so it animates by itself.
	 */
	@Override
	public Component getName(ItemStack stack) {
		Component plain = super.getName(stack);
		// dedicated servers have no translations, keep the normal name there
		if (!FMLEnvironment.dist.isClient())
			return plain;
		String text = plain.getString();
		int len = text.length();
		if (len == 0)
			return plain;
		boolean green = type == BossHeadType.RADIOACTIVE_WARDEN;
		boolean purple = type == BossHeadType.NYXAR_PHASE2;
		int base = green ? GREEN_BASE : purple ? PURPLE_BASE : BLUE_BASE;
		int glow = green ? GREEN_GLOW : purple ? PURPLE_GLOW : BLUE_GLOW;
		double cycle = len + GLOW_WIDTH * 2 + PAUSE_LETTERS;
		double center = ((System.currentTimeMillis() % (long) SWEEP_MS) / SWEEP_MS) * cycle - GLOW_WIDTH;
		MutableComponent result = Component.empty();
		for (int i = 0; i < len; i++) {
			double t = Math.max(0.0, 1.0 - Math.abs(i - center) / GLOW_WIDTH);
			t = t * t * (3.0 - 2.0 * t); // smoothstep -> soft edges
			result.append(Component.literal(String.valueOf(text.charAt(i))).withStyle(Style.EMPTY.withColor(lerp(base, glow, t)).withBold(true)));
		}
		return result;
	}

	private static int lerp(int from, int to, double t) {
		int r = (int) Math.round(((from >> 16) & 0xFF) + (((to >> 16) & 0xFF) - ((from >> 16) & 0xFF)) * t);
		int g = (int) Math.round(((from >> 8) & 0xFF) + (((to >> 8) & 0xFF) - ((from >> 8) & 0xFF)) * t);
		int b = (int) Math.round((from & 0xFF) + ((to & 0xFF) - (from & 0xFF)) * t);
		return (r << 16) | (g << 8) | b;
	}
}
