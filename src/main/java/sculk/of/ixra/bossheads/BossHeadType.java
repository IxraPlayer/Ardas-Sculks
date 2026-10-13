package sculk.of.ixra.bossheads;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.Direction;

/**
 * The three boss trophy heads.
 *
 * All numbers below are in model pixels (a block is 16 px) and are produced by
 * tools/boss_heads/gen.py, which reads the boss geo models, cuts the head out,
 * centres the face on the block and scales every head to its own size.
 */
public enum BossHeadType {
	//                id                         floorBox (x1,y1,z1,x2,y2,z2)               wallBox facing NORTH (x1,y1,z1,x2,y2,z2)          centreY  guiScale pitch
	SCULK_GOLEM("sculk_golem_head", "sculk_golem_wall_head", new double[]{1.85, 0, 1.85, 14.15, 7.5, 14.15}, new double[]{2.0, 4.25, 4.6, 14.0, 11.75, 16}, 3.75, 0.8729f, 18f),
	RADIOACTIVE_WARDEN("radioactive_warden_head", "radioactive_warden_wall_head", new double[]{4, 0, 4, 12, 8, 12}, new double[]{4, 3.25, 11, 12, 11.25, 16}, 4.75, 0.8333f, 14f),
	NYXAR("nyxar_head", "nyxar_wall_head", new double[]{0.75, 0, 0.75, 15.25, 10.545, 15.25}, new double[]{1.409, 2.727, 6.114, 14.591, 13.273, 16}, 5.273, 0.7655f, 8f),
	/** same head as NYXAR, with the purple phase 2 texture and eyes (crafted from / back into the phase 1 head) */
	NYXAR_PHASE2("nyxar_phase2_head", "nyxar_phase2_wall_head", new double[]{0.75, 0, 0.75, 15.25, 10.545, 15.25}, new double[]{1.409, 2.727, 6.114, 14.591, 13.273, 16}, 5.273, 0.7655f, 8f);

	public final String floorId;
	public final String wallId;
	/** height (px) of the middle of the whole head above the floor, used to centre it in the inventory slot */
	public final double centerY;
	/** scale that makes the whole head fit the 16x16 inventory slot while it spins */
	public final float guiScale;
	/** how far the head is tilted forward in the inventory so the face stays visible */
	public final float guiPitch;
	private final VoxelShape floorShape;
	private final VoxelShape[] wallShapes = new VoxelShape[4];

	BossHeadType(String floorId, String wallId, double[] floorBox, double[] wallBox, double centerY, float guiScale, float guiPitch) {
		this.floorId = floorId;
		this.wallId = wallId;
		this.centerY = centerY;
		this.guiScale = guiScale;
		this.guiPitch = guiPitch;
		this.floorShape = Block.box(floorBox[0], floorBox[1], floorBox[2], floorBox[3], floorBox[4], floorBox[5]);
		for (Direction d : Direction.Plane.HORIZONTAL)
			this.wallShapes[d.get2DDataValue()] = rotatedBox(wallBox, d);
	}

	/**
	 * scale used when the head is worn. The worn cell of an item is only 10 px, so the heads are enlarged until the face
	 * (first element of the model) is about as wide as the player's 8 px head. The player's own head is hidden
	 * while a boss head is worn (BossHeadHidePlayerHead), and the model is centred on where the head was.
	 */
	public float wornScale() {
		return switch (this) {
			case SCULK_GOLEM -> 1.5f;
			case RADIOACTIVE_WARDEN -> 1.7f;
			case NYXAR, NYXAR_PHASE2 -> 2.0f;
		};
	}

	public VoxelShape floorShape() {
		return floorShape;
	}

	public VoxelShape wallShape(Direction facing) {
		return wallShapes[facing.get2DDataValue()];
	}

	/** wallBox is authored for a head facing NORTH (wall behind it on the south side); rotate it clockwise for the other facings. */
	private static VoxelShape rotatedBox(double[] b, Direction facing) {
		double x1 = b[0] - 8, z1 = b[2] - 8, x2 = b[3] - 8, z2 = b[5] - 8;
		double[] xs, zs;
		switch (facing) {
			case EAST -> { // (dx,dz) -> (-dz,dx)
				xs = new double[]{-z2, -z1};
				zs = new double[]{x1, x2};
			}
			case SOUTH -> { // (dx,dz) -> (-dx,-dz)
				xs = new double[]{-x2, -x1};
				zs = new double[]{-z2, -z1};
			}
			case WEST -> { // (dx,dz) -> (dz,-dx)
				xs = new double[]{z1, z2};
				zs = new double[]{-x2, -x1};
			}
			default -> {
				xs = new double[]{x1, x2};
				zs = new double[]{z1, z2};
			}
		}
		return Shapes.box((xs[0] + 8) / 16.0, b[1] / 16.0, (zs[0] + 8) / 16.0, (xs[1] + 8) / 16.0, b[4] / 16.0, (zs[1] + 8) / 16.0);
	}
}
