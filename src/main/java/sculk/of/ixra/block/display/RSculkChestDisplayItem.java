package sculk.of.ixra.block.display;

import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.GeoItem;

import sculk.of.ixra.block.renderer.RSculkChestDisplayItemRenderer;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

import java.util.function.Consumer;
import java.util.Properties;

public class RSculkChestDisplayItem extends BlockItem implements GeoItem {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	public RSculkChestDisplayItem(Block block, Properties settings) {
		super(block, settings);
	}

	private PlayState predicate(AnimationState event) {
		event.getController().setAnimation(RawAnimation.begin().thenLoop("0"));
		return PlayState.CONTINUE;
	}

	@Override
	public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
		consumer.accept(new GeoRenderProvider() {
			private RSculkChestDisplayItemRenderer renderer;

			@Override
			public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
				if (this.renderer == null)
					this.renderer = new RSculkChestDisplayItemRenderer();
				return this.renderer;
			}
		});
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
		data.add(new AnimationController(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
