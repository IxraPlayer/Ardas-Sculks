
package sculk.of.ixra.client.renderer;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;

import sculk.of.ixra.entity.model.AgileSlimeModel;
import sculk.of.ixra.entity.AgileSlimeEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

import java.util.Optional;

public class AgileSlimeRenderer extends GeoEntityRenderer<AgileSlimeEntity> {
	public AgileSlimeRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new AgileSlimeModel());
		this.shadowRadius = 0.5f;
	}

	@Override
	public RenderType getRenderType(AgileSlimeEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, AgileSlimeEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
		float scale = 2.6f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, color);
	}

	/**
	 * Slime gözlerinin/ağzının açıya göre "solup gitmesi" sorunu, tüm modelin (iç detay küpü
	 * + dış jöle kabuğu) tek bir saydam (translucent) katmanda çizilmesinden kaynaklanıyordu:
	 * saydam katmanda derinlik sıralaması kararsız olduğu için kamera açısına göre iç küp
	 * bazen dış kabuğun "arkasında" gibi render oluyor ve göz rengi soluyordu.
	 *
	 * Çözüm: modeli iki geçişte çiziyoruz.
	 *  1) "inner" kemiği (gövde çekirdeği + gözler + ağız) OPAK olarak çizilir -> her açıdan net görünür.
	 *  2) "outer" kemiği (dış jöle kabuğu) SAYDAM olarak üstüne çizilir -> jöle görünümü korunur.
	 */
	@Override
	public void render(AgileSlimeEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		this.animatable = entity;

		BakedGeoModel bakedModel = getGeoModel().getBakedModel(getGeoModel().getModelResource(entity));
		Optional<GeoBone> innerBone = bakedModel.getBone("inner");
		Optional<GeoBone> outerBone = bakedModel.getBone("outer");

		if (innerBone.isPresent() && outerBone.isPresent()) {
			ResourceLocation texture = getTextureLocation(entity);

			// Geçiş 1: iç küp (gözler/ağız) opak
			outerBone.get().setHidden(true);
			innerBone.get().setHidden(false);
			defaultRender(poseStack, entity, bufferSource, RenderType.entityCutoutNoCull(texture), null, entityYaw, partialTick, packedLight);

			// Geçiş 2: dış jöle kabuğu saydam
			innerBone.get().setHidden(true);
			outerBone.get().setHidden(false);
			defaultRender(poseStack, entity, bufferSource, RenderType.entityTranslucent(texture), null, entityYaw, partialTick, packedLight);

			// Bir sonraki render için görünürlükleri sıfırla
			innerBone.get().setHidden(false);
			outerBone.get().setHidden(false);
		} else {
			// Kemikler bulunamazsa eski davranışa (tek geçiş, tamamen saydam) geri dön
			defaultRender(poseStack, entity, bufferSource, null, null, entityYaw, partialTick, packedLight);
		}

		this.animatable = null;
	}
}
