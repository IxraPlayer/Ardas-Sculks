
package sculk.of.ixra.world.teleporter;

import sculk.of.ixra.init.SculksOfArdaModBlocks;

import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Holder;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.BlockUtil;

import javax.annotation.Nullable;

import java.util.Optional;
import java.util.Comparator;

import com.google.common.collect.ImmutableSet;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class SculkWorldTeleporter {
	public static Holder<PoiType> poi = null;

	@SubscribeEvent
	public static void registerPointOfInterest(RegisterEvent event) {
		event.register(Registries.POINT_OF_INTEREST_TYPE, registerHelper -> {
			PoiType poiType = new PoiType(ImmutableSet.copyOf(SculksOfArdaModBlocks.SCULK_WORLD_PORTAL.get().getStateDefinition().getPossibleStates()), 0, 1);
			registerHelper.register(ResourceLocation.parse("sculks_of_arda:sculk_world_portal"), poiType);
			poi = BuiltInRegistries.POINT_OF_INTEREST_TYPE.wrapAsHolder(poiType);
		});
	}

	private final ServerLevel level;

	public SculkWorldTeleporter(ServerLevel level) {
		this.level = level;
	}

	public Optional<BlockPos> findClosestPortalPosition(BlockPos p_352378_, boolean p_352309_, WorldBorder p_352374_) {
		PoiManager poimanager = this.level.getPoiManager();
		int i = p_352309_ ? 16 : 128;
		poimanager.ensureLoadedAndValid(this.level, p_352378_, i);
		return poimanager.getInSquare(p_230634_ -> p_230634_.is(poi.unwrapKey().get()), p_352378_, i, PoiManager.Occupancy.ANY).map(PoiRecord::getPos).filter(p_352374_::isWithinBounds)
				.filter(p_352047_ -> this.level.getBlockState(p_352047_).hasProperty(BlockStateProperties.HORIZONTAL_AXIS)).min(Comparator.<BlockPos>comparingDouble(p_352046_ -> p_352046_.distSqr(p_352378_)).thenComparingInt(Vec3i::getY));
	}

	// Kk: cerceve boyutlari - uzunluk (dikey/yukseklik) 4 blok, genislik (yatay) 7 blok,
	// antik sehir (ancient city) tarzi dikey/dikdortgen bir kapi gorunumu icin.
	private static final int PORTAL_WIDTH = 7;
	private static final int PORTAL_HEIGHT = 4;

	public Optional<BlockUtil.FoundRectangle> createPortal(BlockPos p_77667_, Direction.Axis p_77668_) {
		WorldBorder worldborder = this.level.getWorldBorder();
		Direction rightDir = Direction.get(Direction.AxisDirection.POSITIVE, p_77668_);

		BlockPos corner = findVerticalPortalCorner(p_77667_, worldborder, rightDir);
		boolean forcedSite = false;
		if (corner == null) {
			// Kk: duz bir hava cebi bulunamadi -> eskiden burada Optional.empty() donup oyuncu portalda takili kaliyordu.
			// Simdi hedef yuzeye portali zorla kuruyoruz (alani temizle + altina zemin koy).
			corner = findSurfaceCorner(p_77667_, worldborder, rightDir);
			forcedSite = true;
		}
		if (corner == null) {
			return Optional.empty();
		}
		if (forcedSite) {
			prepareSurfaceSite(corner, rightDir);
		}

		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		// Build the standing frame (width along the portal's axis, height straight up).
		for (int w = -1; w <= PORTAL_WIDTH; w++) {
			for (int h = -1; h <= PORTAL_HEIGHT; h++) {
				cursor.setWithOffset(corner, w * rightDir.getStepX(), h, w * rightDir.getStepZ());
				boolean isEdge = w == -1 || w == PORTAL_WIDTH || h == -1 || h == PORTAL_HEIGHT;
				if (isEdge) {
					this.level.setBlockAndUpdate(cursor, Blocks.REINFORCED_DEEPSLATE.defaultBlockState());
				} else {
					this.level.setBlockAndUpdate(cursor, Blocks.AIR.defaultBlockState());
				}
			}
		}

		BlockState portalState = SculksOfArdaModBlocks.SCULK_WORLD_PORTAL.get().defaultBlockState().setValue(NetherPortalBlock.AXIS, p_77668_);
		for (int w = 0; w < PORTAL_WIDTH; w++) {
			for (int h = 0; h < PORTAL_HEIGHT; h++) {
				cursor.setWithOffset(corner, w * rightDir.getStepX(), h, w * rightDir.getStepZ());
				this.level.setBlock(cursor, portalState, 18);
				this.level.getPoiManager().add(cursor, poi);
			}
		}
		return Optional.of(new BlockUtil.FoundRectangle(corner.immutable(), PORTAL_WIDTH, PORTAL_HEIGHT));
	}

	/** Hedef x/z'deki gercek yuzey yuksekligine gore portal kosesi secer (ortada, yuzeyin ustunde). */
	@Nullable
	private BlockPos findSurfaceCorner(BlockPos target, WorldBorder worldborder, Direction rightDir) {
		BlockPos clamped = worldborder.clampToBounds(target);
		Direction depthDir = rightDir.getClockWise();
		int half = PORTAL_WIDTH / 2;
		int startX = clamped.getX() - rightDir.getStepX() * half;
		int startZ = clamped.getZ() - rightDir.getStepZ() * half;
		int minY = this.level.getMinBuildHeight() + 6;
		int maxY = this.level.getMaxBuildHeight() - PORTAL_HEIGHT - 3;
		if (maxY < minY) {
			return null;
		}
		int surface = minY;
		for (int w = -1; w <= PORTAL_WIDTH; w++) {
			for (int d = -2; d <= 2; d++) {
				int x = startX + w * rightDir.getStepX() + d * depthDir.getStepX();
				int z = startZ + w * rightDir.getStepZ() + d * depthDir.getStepZ();
				if (!worldborder.isWithinBounds(new BlockPos(x, clamped.getY(), z))) {
					return null;
				}
				// chunk'i zorla yukle ki heightmap gecerli olsun
				this.level.getChunk(x >> 4, z >> 4);
				surface = Math.max(surface, this.level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z));
			}
		}
		return new BlockPos(startX, Mth.clamp(surface, minY, maxY), startZ);
	}

	/** Portalin onundeki/arkasindaki alani temizler ve cerceve zemininin altini doldurur. */
	private void prepareSurfaceSite(BlockPos corner, Direction rightDir) {
		Direction depthDir = rightDir.getClockWise();
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		BlockState air = Blocks.AIR.defaultBlockState();
		BlockState floor = Blocks.DEEPSLATE.defaultBlockState();
		for (int w = -1; w <= PORTAL_WIDTH; w++) {
			for (int d = -2; d <= 2; d++) {
				int ox = w * rightDir.getStepX() + d * depthDir.getStepX();
				int oz = w * rightDir.getStepZ() + d * depthDir.getStepZ();
				// ust kisim: cerceve duzlemindeki (d == 0) hucreleri createPortal kendisi dolduruyor
				for (int h = 0; h <= PORTAL_HEIGHT; h++) {
					if (d == 0) {
						continue;
					}
					cursor.setWithOffset(corner, ox, h, oz);
					this.level.setBlock(cursor, air, 18);
				}
				// zemin: d == 0 ise h = -1 cerceve blogu olacak, onun altini doldur
				for (int h = -1; h >= -41; h--) {
					if (d == 0 && h == -1) {
						continue;
					}
					cursor.setWithOffset(corner, ox, h, oz);
					BlockState state = this.level.getBlockState(cursor);
					boolean open = state.isAir() || state.canBeReplaced() || !state.getFluidState().isEmpty();
					if (h == -1 || open) {
						this.level.setBlock(cursor, floor, 18);
					} else {
						break;
					}
				}
			}
		}
	}

	@Nullable
	private BlockPos findVerticalPortalCorner(BlockPos p_77667_, WorldBorder worldborder, Direction rightDir) {
		BlockPos clamped = worldborder.clampToBounds(p_77667_);
		// Kk: bedrock kati genelde dunyanin en altinda 5 blok kalinliginda ve
		// deliklerle olusuyor; portalin oraya/altina dogmamasi icin tabandan
		// guvenli bir tampon birakiyoruz.
		int minY = this.level.getMinBuildHeight() + 6;
		int maxY = this.level.getMaxBuildHeight() - PORTAL_HEIGHT - 2;
		if (maxY < minY) {
			return null;
		}

		// Kk: sculk dunyasinin acik gokyuzu olmayan, magara/yeralti tarzi bir
		// boyut olmasi yuzunden, eski heightmap tabanli secim bazen portali
		// dogrudan kati blogun icine gomuyordu. Bunun yerine cevredeki (x,z)
		// sutunlarinda gercekten "alti sağlam, ustu/ici bos" bir hava cebi
		// arayip, bulunan en yakin uygun noktaya insa ediyoruz.
		int searchRadius = 8;
		int verticalRadius = 16;
		BlockPos bestSpot = null;
		double bestDist = Double.MAX_VALUE;

		for (int dx = -searchRadius; dx <= searchRadius; dx++) {
			for (int dz = -searchRadius; dz <= searchRadius; dz++) {
				int x = clamped.getX() + dx;
				int z = clamped.getZ() + dz;
				if (!worldborder.isWithinBounds(new BlockPos(x, clamped.getY(), z))) {
					continue;
				}
				for (int dy = -verticalRadius; dy <= verticalRadius; dy++) {
					int y = Mth.clamp(clamped.getY() + dy, minY, maxY);
					BlockPos candidate = new BlockPos(x, y, z);
					if (isOpenPocket(candidate, rightDir)) {
						double dist = candidate.distSqr(clamped);
						if (dist < bestDist) {
							bestDist = dist;
							bestSpot = candidate;
						}
						break;
					}
				}
				if (bestDist == 0.0D) {
					return bestSpot;
				}
			}
		}

		return bestSpot;
	}

	/**
	 * Cercevenin + icinin tamamen bos/doldurulabilir oldugunu (kati blogun
	 * icine gommeyecegini) ve cercevenin hemen altinin kati/saglam oldugunu
	 * (portalin havada asili kalmayacagini) kontrol eder.
	 */
	private boolean isOpenPocket(BlockPos corner, Direction rightDir) {
		BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
		for (int w = -1; w <= PORTAL_WIDTH; w++) {
			for (int h = -1; h <= PORTAL_HEIGHT; h++) {
				cursor.setWithOffset(corner, w * rightDir.getStepX(), h, w * rightDir.getStepZ());
				if (!canPortalReplaceBlock(cursor)) {
					return false;
				}
			}
			cursor.setWithOffset(corner, w * rightDir.getStepX(), -2, w * rightDir.getStepZ());
			if (canPortalReplaceBlock(cursor)) {
				return false;
			}
		}
		return true;
	}

	private boolean canPortalReplaceBlock(BlockPos.MutableBlockPos pos) {
		BlockState blockstate = this.level.getBlockState(pos);
		return blockstate.canBeReplaced() && blockstate.getFluidState().isEmpty();
	}
}
