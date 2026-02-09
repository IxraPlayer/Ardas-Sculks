package sculk.of.ixra.client.gui;

import sculk.of.ixra.world.inventory.ArdasSculkBookMobsMenu1Menu;
import sculk.of.ixra.procedures.DisplayEntityProcedureProcedure;
import sculk.of.ixra.procedures.DisplayEntityProcedure2Procedure;
import sculk.of.ixra.network.ArdasSculkBookMobsMenu1ButtonMessage;

import org.joml.Vector3f;
import org.joml.Quaternionf;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class ArdasSculkBookMobsMenu1Screen extends AbstractContainerScreen<ArdasSculkBookMobsMenu1Menu> {
	private final static HashMap<String, Object> guistate = ArdasSculkBookMobsMenu1Menu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	ImageButton imagebutton_pagearrow;

	public ArdasSculkBookMobsMenu1Screen(ArdasSculkBookMobsMenu1Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 88;
		this.imageHeight = 88;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("sculks_of_arda:textures/screens/ardas_sculk_book_mobs_menu_1.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		if (DisplayEntityProcedureProcedure.execute(world) instanceof LivingEntity livingEntity) {
			this.renderEntityInInventoryFollowsAngle(guiGraphics, this.leftPos + -28, this.topPos + 60, 30, 0f + (float) Math.atan((this.leftPos + -28 - mouseX) / 40.0), (float) Math.atan((this.topPos + 11 - mouseY) / 40.0), livingEntity);
		}
		if (DisplayEntityProcedure2Procedure.execute(world) instanceof LivingEntity livingEntity) {
			this.renderEntityInInventoryFollowsAngle(guiGraphics, this.leftPos + 116, this.topPos + 60, 30, 0f + (float) Math.atan((this.leftPos + 116 - mouseX) / 40.0), (float) Math.atan((this.topPos + 11 - mouseY) / 40.0), livingEntity);
		}
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/book4.png"), this.leftPos + -101, this.topPos + -41, 0, 0, 292, 180, 292, 180);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/heart.png"), this.leftPos + -15, this.topPos + 67, 0, 0, 9, 9, 9, 9);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/heart.png"), this.leftPos + -5, this.topPos + 67, 0, 0, 9, 9, 9, 9);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/heart.png"), this.leftPos + 5, this.topPos + 67, 0, 0, 9, 9, 9, 9);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/heart.png"), this.leftPos + 15, this.topPos + 67, 0, 0, 9, 9, 9, 9);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/heart.png"), this.leftPos + 25, this.topPos + 67, 0, 0, 9, 9, 9, 9);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/heart.png"), this.leftPos + 122, this.topPos + 67, 0, 0, 9, 9, 9, 9);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/heart.png"), this.leftPos + 132, this.topPos + 67, 0, 0, 9, 9, 9, 9);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/heart.png"), this.leftPos + 142, this.topPos + 67, 0, 0, 9, 9, 9, 9);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/heart.png"), this.leftPos + 152, this.topPos + 67, 0, 0, 9, 9, 9, 9);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/heart.png"), this.leftPos + 162, this.topPos + 67, 0, 0, 9, 9, 9, 9);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/heart.png"), this.leftPos + 122, this.topPos + 77, 0, 0, 9, 9, 9, 9);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/heart.png"), this.leftPos + 132, this.topPos + 77, 0, 0, 9, 9, 9, 9);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/heart.png"), this.leftPos + 142, this.topPos + 77, 0, 0, 9, 9, 9, 9);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/sculk_piece.png"), this.leftPos + -38, this.topPos + 94, 0, 0, 16, 16, 16, 16);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/warden_claw.png"), this.leftPos + 97, this.topPos + 94, 0, 0, 16, 16, 16, 16);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/bone.png"), this.leftPos + 115, this.topPos + 94, 0, 0, 16, 16, 16, 16);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculk_book_mobs_menu_1.label_ardas_sculks_mobs"), -73, -28, -16737895, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculk_book_mobs_menu_1.label_sculk_skeleton"), 79, -14, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculk_book_mobs_menu_1.label_sculk_creeper"), -56, -14, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculk_book_mobs_menu_1.label_heart_count"), -83, 67, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculk_book_mobs_menu_1.label_heart_count1"), 52, 67, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculk_book_mobs_menu_1.label_drop"), -82, 97, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculk_book_mobs_menu_1.label_drop1"), 55, 98, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		imagebutton_pagearrow = new ImageButton(this.leftPos + -83, this.topPos + 112, 18, 10,
				new WidgetSprites(ResourceLocation.parse("sculks_of_arda:textures/screens/pagearrow.png"), ResourceLocation.parse("sculks_of_arda:textures/screens/pagearrow.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new ArdasSculkBookMobsMenu1ButtonMessage(0, x, y, z));
						ArdasSculkBookMobsMenu1ButtonMessage.handleButtonAction(entity, 0, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_pagearrow", imagebutton_pagearrow);
		this.addRenderableWidget(imagebutton_pagearrow);
	}

	private void renderEntityInInventoryFollowsAngle(GuiGraphics guiGraphics, int x, int y, int scale, float angleXComponent, float angleYComponent, LivingEntity entity) {
		Quaternionf pose = new Quaternionf().rotateZ((float) Math.PI);
		Quaternionf cameraOrientation = new Quaternionf().rotateX(angleYComponent * 20 * ((float) Math.PI / 180F));
		pose.mul(cameraOrientation);
		float f2 = entity.yBodyRot;
		float f3 = entity.getYRot();
		float f4 = entity.getXRot();
		float f5 = entity.yHeadRotO;
		float f6 = entity.yHeadRot;
		entity.yBodyRot = 180.0F + angleXComponent * 20.0F;
		entity.setYRot(180.0F + angleXComponent * 40.0F);
		entity.setXRot(-angleYComponent * 20.0F);
		entity.yHeadRot = entity.getYRot();
		entity.yHeadRotO = entity.getYRot();
		InventoryScreen.renderEntityInInventory(guiGraphics, x, y, scale, new Vector3f(0, 0, 0), pose, cameraOrientation, entity);
		entity.yBodyRot = f2;
		entity.setYRot(f3);
		entity.setXRot(f4);
		entity.yHeadRotO = f5;
		entity.yHeadRot = f6;
	}
}
