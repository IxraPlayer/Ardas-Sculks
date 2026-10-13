package sculk.of.ixra.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;

/**
 * Klon modu halka matematigi. Sunucu (isinlanma) ve istemci (gorsel klonlar) AYNI hesabi kullanir,
 * boylece klonlarin durdugu noktalar ile boss'un isinlandigi noktalar birebir ayni olur.
 * Halka: oyuncunun etrafinda 8 nokta, aralarinda 45 derece. Boss bunlardan birinde, 7 klon digerlerinde.
 * Boss bir noktadan digerine gecince nokta kumesi degismez (45'in katlari), sadece yer degistirirler.
 */
public final class ShadowHunterCloneRing {
	public static final int POINTS = 8;
	public static final double STEP = Math.PI * 2.0D / POINTS;
	public static final double MIN_RADIUS = 5.0D;
	public static final double MAX_RADIUS = 24.0D;
	/** Zeminin ustunde aranan dikey bosluk (blok). Boss faz 2'de ~4.4 blok, kalan pay guvenlik icin. */
	public static final int CLEARANCE = 5;

	private ShadowHunterCloneRing() {
	}

	/** Halka yaricapi = boss'un oyuncuya yatay uzakligi (yakinliga senkron), makul araliga sikistirilir. */
	public static double radiusFor(double bx, double bz, double cx, double cz) {
		double d = Math.sqrt((bx - cx) * (bx - cx) + (bz - cz) * (bz - cz));
		return Math.max(MIN_RADIUS, Math.min(MAX_RADIUS, d));
	}

	/** Boss'un oyuncuya gore halkadaki acisi (radyan). */
	public static double angleOf(double bx, double bz, double cx, double cz) {
		return Math.atan2(bz - cz, bx - cx);
	}

	/**
	 * Halkadaki bir noktanin zemin uzerindeki konumu. Zeminin ustunde CLEARANCE kadar dikey bosluk
	 * + altinda kati blok bulunamazsa (duvar icinde, alcak tavan, bosluk, yuklenmemis chunk) null doner;
	 * o nokta kullanilmaz.
	 */
	public static Vec3 point(Level level, double cx, double cy, double cz, double radius, double angle) {
		double x = cx + Math.cos(angle) * radius;
		double z = cz + Math.sin(angle) * radius;
		BlockPos top = BlockPos.containing(x, cy + 2.0D, z);
		if (!level.hasChunkAt(top))
			return null;
		for (int i = 0; i < 8; i++) {
			BlockPos feet = top.below(i);
			BlockPos ground = feet.below();
			BlockState groundState = level.getBlockState(ground);
			if (groundState.getCollisionShape(level, ground).isEmpty())
				continue;
			for (int h = 0; h < CLEARANCE; h++) {
				BlockPos cell = feet.above(h);
				if (!level.hasChunkAt(cell) || !level.getBlockState(cell).getCollisionShape(level, cell).isEmpty())
					return null;
			}
			double y = ground.getY() + groundState.getCollisionShape(level, ground).max(Direction.Axis.Y);
			return new Vec3(x, y, z);
		}
		return null;
	}
}
