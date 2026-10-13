package sculk.of.ixra.block.renderer;

import software.bernie.geckolib.renderer.GeoItemRenderer;

import sculk.of.ixra.block.model.RSculkChestDisplayModel;
import sculk.of.ixra.block.display.RSculkChestDisplayItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

public class RSculkChestDisplayItemRenderer extends GeoItemRenderer<RSculkChestDisplayItem> {
	public RSculkChestDisplayItemRenderer() {
		super(new RSculkChestDisplayModel());
	}

	@Override
	public RenderType getRenderType(RSculkChestDisplayItem animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
