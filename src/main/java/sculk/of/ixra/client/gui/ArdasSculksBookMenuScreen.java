package sculk.of.ixra.client.gui;

import sculk.of.ixra.world.inventory.ArdasSculksBookMenuMenu;
import sculk.of.ixra.network.ArdasSculksBookMenuButtonMessage;

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

public class ArdasSculksBookMenuScreen extends AbstractContainerScreen<ArdasSculksBookMenuMenu> {
	private final static HashMap<String, Object> guistate = ArdasSculksBookMenuMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	ImageButton imagebutton_bookbutton;
	ImageButton imagebutton_bookbutton1;
	ImageButton imagebutton_bookbutton2;
	ImageButton imagebutton_bookbutton3;
	ImageButton imagebutton_bookbutton4;
	ImageButton imagebutton_bookbutton5;
	ImageButton imagebutton_bookbutton6;
	ImageButton imagebutton_bookbutton7;
	ImageButton imagebutton_bookbutton8;
	ImageButton imagebutton_pagearrow2;

	public ArdasSculksBookMenuScreen(ArdasSculksBookMenuMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 88;
		this.imageHeight = 88;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("sculks_of_arda:textures/screens/ardas_sculks_book_menu.png");

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

		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/book5.png"), this.leftPos + -101, this.topPos + -41, 0, 0, 292, 180, 292, 180);

		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/ardassculkslogo2.png"), this.leftPos + -83, this.topPos + -32, 0, 0, 256, 64, 256, 64);

		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png"), this.leftPos + 90, this.topPos + 91, 0, 0, 44, 15, 44, 15);

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
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_mobs"), -82, 22, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_items"), -83, 40, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_machines"), -83, 58, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_food"), -83, 76, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_metals"), -83, 94, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_plants"), 52, 22, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_villagers"), 52, 40, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_weapons"), 52, 58, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_decorations"), 52, 76, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_open"), -35, 22, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_open1"), -33, 39, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_open2"), -17, 57, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_open3"), -27, 75, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_open4"), -29, 93, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_open5"), 107, 21, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_open6"), 122, 39, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_open7"), 112, 57, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_open8"), 133, 75, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_tools"), 52, 94, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu.label_read"), 100, 93, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		imagebutton_bookbutton = new ImageButton(this.leftPos + -45, this.topPos + 20, 44, 15,
				new WidgetSprites(ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png"), ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new ArdasSculksBookMenuButtonMessage(0, x, y, z));
						ArdasSculksBookMenuButtonMessage.handleButtonAction(entity, 0, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_bookbutton", imagebutton_bookbutton);
		this.addRenderableWidget(imagebutton_bookbutton);
		imagebutton_bookbutton1 = new ImageButton(this.leftPos + -43, this.topPos + 37, 44, 15,
				new WidgetSprites(ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png"), ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new ArdasSculksBookMenuButtonMessage(1, x, y, z));
						ArdasSculksBookMenuButtonMessage.handleButtonAction(entity, 1, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_bookbutton1", imagebutton_bookbutton1);
		this.addRenderableWidget(imagebutton_bookbutton1);
		imagebutton_bookbutton2 = new ImageButton(this.leftPos + -28, this.topPos + 55, 44, 15,
				new WidgetSprites(ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png"), ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png")), e -> {
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_bookbutton2", imagebutton_bookbutton2);
		this.addRenderableWidget(imagebutton_bookbutton2);
		imagebutton_bookbutton3 = new ImageButton(this.leftPos + -37, this.topPos + 73, 44, 15,
				new WidgetSprites(ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png"), ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png")), e -> {
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_bookbutton3", imagebutton_bookbutton3);
		this.addRenderableWidget(imagebutton_bookbutton3);
		imagebutton_bookbutton4 = new ImageButton(this.leftPos + -39, this.topPos + 91, 44, 15,
				new WidgetSprites(ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png"), ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png")), e -> {
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_bookbutton4", imagebutton_bookbutton4);
		this.addRenderableWidget(imagebutton_bookbutton4);
		imagebutton_bookbutton5 = new ImageButton(this.leftPos + 96, this.topPos + 19, 44, 15,
				new WidgetSprites(ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png"), ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png")), e -> {
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_bookbutton5", imagebutton_bookbutton5);
		this.addRenderableWidget(imagebutton_bookbutton5);
		imagebutton_bookbutton6 = new ImageButton(this.leftPos + 111, this.topPos + 37, 44, 15,
				new WidgetSprites(ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png"), ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png")), e -> {
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_bookbutton6", imagebutton_bookbutton6);
		this.addRenderableWidget(imagebutton_bookbutton6);
		imagebutton_bookbutton7 = new ImageButton(this.leftPos + 102, this.topPos + 55, 44, 15,
				new WidgetSprites(ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png"), ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png")), e -> {
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_bookbutton7", imagebutton_bookbutton7);
		this.addRenderableWidget(imagebutton_bookbutton7);
		imagebutton_bookbutton8 = new ImageButton(this.leftPos + 122, this.topPos + 73, 44, 15,
				new WidgetSprites(ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png"), ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png")), e -> {
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_bookbutton8", imagebutton_bookbutton8);
		this.addRenderableWidget(imagebutton_bookbutton8);
		imagebutton_pagearrow2 = new ImageButton(this.leftPos + 151, this.topPos + 112, 18, 10,
				new WidgetSprites(ResourceLocation.parse("sculks_of_arda:textures/screens/pagearrow2.png"), ResourceLocation.parse("sculks_of_arda:textures/screens/pagearrow2.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new ArdasSculksBookMenuButtonMessage(9, x, y, z));
						ArdasSculksBookMenuButtonMessage.handleButtonAction(entity, 9, x, y, z);
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
