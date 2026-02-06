package sculk.of.ixra.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.EntityModel;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
public class Modelsculkbosss<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("sculks_of_arda", "modelsculkbosss"), "main");
	public final ModelPart gogus;
	public final ModelPart sagayak;
	public final ModelPart solkol;
	public final ModelPart sagkol2;
	public final ModelPart solayak;
	public final ModelPart kafa;

	public Modelsculkbosss(ModelPart root) {
		this.gogus = root.getChild("gogus");
		this.sagayak = root.getChild("sagayak");
		this.solkol = root.getChild("solkol");
		this.sagkol2 = root.getChild("sagkol2");
		this.solayak = root.getChild("solayak");
		this.kafa = root.getChild("kafa");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition gogus = partdefinition.addOrReplaceChild("gogus",
				CubeListBuilder.create().texOffs(50, 16).addBox(-3.0F, -8.0F, -14.0F, 6.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 54).addBox(-5.0F, -4.6F, -15.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(31, 29)
						.addBox(-5.0F, -6.6F, -15.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(11, 52).addBox(2.0F, -4.6F, -15.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(50, 23)
						.addBox(2.0F, -6.6F, -15.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 14.0F, 7.0F));
		PartDefinition cube_r1 = gogus.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -18.0F, -10.0F, 14.0F, 18.0F, 11.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
		PartDefinition sagayak = partdefinition.addOrReplaceChild("sagayak", CubeListBuilder.create().texOffs(49, 49).addBox(-6.0F, -11.0F, -2.0F, 4.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 2.0F));
		PartDefinition solkol = partdefinition.addOrReplaceChild("solkol",
				CubeListBuilder.create().texOffs(18, 29).addBox(7.0F, -20.0F, -4.0F, 4.0F, 18.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(55, 27).addBox(10.0F, -17.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(23, 52)
						.addBox(10.0F, -13.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(44, 49).addBox(10.0F, -9.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(36, 40)
						.addBox(6.0F, -5.0F, -5.0F, 6.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition cube_r2 = solkol.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(39, 2).addBox(0.0F, -2.0F, -1.0F, 0.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(9.0F, -3.0F, 2.0F, -0.3491F, 0.0F, 0.0F));
		PartDefinition sagkol2 = partdefinition.addOrReplaceChild("sagkol2",
				CubeListBuilder.create().texOffs(0, 29).addBox(-11.0F, -20.0F, -4.0F, 4.0F, 18.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(13, 29).addBox(-12.0F, -17.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 4)
						.addBox(-12.0F, -13.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-12.0F, -9.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(39, 0)
						.addBox(-12.0F, -5.0F, -5.0F, 6.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition cube_r3 = sagkol2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 45).addBox(0.0F, -2.0F, -1.0F, 0.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-9.0F, -3.0F, 2.0F, -0.3491F, 0.0F, 0.0F));
		PartDefinition solayak = partdefinition.addOrReplaceChild("solayak", CubeListBuilder.create().texOffs(31, 49).addBox(-1.0F, -11.0F, -2.0F, 4.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 24.0F, 2.0F));
		PartDefinition kafa = partdefinition.addOrReplaceChild("kafa",
				CubeListBuilder.create().texOffs(36, 29).addBox(-3.0F, -28.0F, -7.0F, 6.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)).texOffs(50, 9).addBox(-4.0F, -25.0F, -7.6F, 8.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int rgb) {
		gogus.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);
		sagayak.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);
		solkol.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);
		sagkol2.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);
		solayak.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);
		kafa.render(poseStack, vertexConsumer, packedLight, packedOverlay, rgb);
	}
}
