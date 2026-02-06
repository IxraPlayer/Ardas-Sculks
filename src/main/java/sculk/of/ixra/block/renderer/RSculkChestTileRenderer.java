package sculk.of.ixra.block.renderer;

import software.bernie.geckolib.renderer.GeoBlockRenderer;

import sculk.of.ixra.block.model.RSculkChestBlockModel;
import sculk.of.ixra.block.entity.RSculkChestTileEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

public class RSculkChestTileRenderer extends GeoBlockRenderer<RSculkChestTileEntity> {
	public RSculkChestTileRenderer() {
		super(new RSculkChestBlockModel());
	}

	@Override
	public RenderType getRenderType(RSculkChestTileEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
