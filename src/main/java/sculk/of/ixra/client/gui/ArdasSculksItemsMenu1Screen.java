package sculk.of.ixra.client.gui;

import sculk.of.ixra.world.inventory.ArdasSculksItemsMenu1Menu;
import sculk.of.ixra.network.ArdasSculksItemsMenu1ButtonMessage;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class ArdasSculksItemsMenu1Screen extends AbstractContainerScreen<ArdasSculksItemsMenu1Menu> {
	private final static HashMap<String, Object> guistate = ArdasSculksItemsMenu1Menu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	ImageButton imagebutton_pagearrow;
	ImageButton imagebutton_pagearrow2;

	public ArdasSculksItemsMenu1Screen(ArdasSculksItemsMenu1Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 88;
		this.imageHeight = 88;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("sculks_of_arda:textures/screens/ardas_sculks_items_menu_1.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/book4.png"), this.leftPos + -101, this.topPos + -41, 0, 0, 292, 180, 292, 180);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/sculk_piece2.png"), this.leftPos + -56, this.topPos + -23, 0, 0, 64, 64, 64, 64);
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/sculk_sensor_part2.png"), this.leftPos + 79, this.topPos + -23, 0, 0, 64, 64, 64, 64);
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
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_items_menu_1.label_sculk_piece"), -56, 49, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_items_menu_1.label_sculk_sensor_part"), 61, 49, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_items_menu_1.label_creatures_like_warden"), -83, 67, -13421773, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_items_menu_1.label_sculk_creatures"), -83, 76, -13421773, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_items_menu_1.label_this_item_you_can"), -83, 85, -13421773, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_items_menu_1.label_find_it_in_ancient"), -83, 94, -13421773, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_items_menu_1.label_you_can_make_sculk"), -83, 103, -13421773, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_items_menu_1.label_items_with_it"), -65, 112, -13421773, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_items_menu_1.label_creatures_like_warden1"), 52, 67, -13421773, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_items_menu_1.label_and_sculk_creatures_drop"), 52, 76, -13421773, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_items_menu_1.label_this_item_it_can_also"), 52, 85, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_items_menu_1.label_be_found_in_the_ancient"), 52, 94, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_items_menu_1.label_city_you_can_also_make"), 52, 103, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_items_menu_1.label_sculk_items_with_it"), 52, 112, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		imagebutton_pagearrow = new ImageButton(this.leftPos + -83, this.topPos + 112, 18, 10,
				new WidgetSprites(ResourceLocation.parse("sculks_of_arda:textures/screens/pagearrow.png"), ResourceLocation.parse("sculks_of_arda:textures/screens/pagearrow.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new ArdasSculksItemsMenu1ButtonMessage(0, x, y, z));
						ArdasSculksItemsMenu1ButtonMessage.handleButtonAction(entity, 0, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_pagearrow", imagebutton_pagearrow);
		this.addRenderableWidget(imagebutton_pagearrow);
		imagebutton_pagearrow2 = new ImageButton(this.leftPos + 151, this.topPos + 112, 18, 10,
				new WidgetSprites(ResourceLocation.parse("sculks_of_arda:textures/screens/pagearrow2.png"), ResourceLocation.parse("sculks_of_arda:textures/screens/pagearrow2.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new ArdasSculksItemsMenu1ButtonMessage(1, x, y, z));
						ArdasSculksItemsMenu1ButtonMessage.handleButtonAction(entity, 1, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_pagearrow2", imagebutton_pagearrow2);
		this.addRenderableWidget(imagebutton_pagearrow2);
	}
}
