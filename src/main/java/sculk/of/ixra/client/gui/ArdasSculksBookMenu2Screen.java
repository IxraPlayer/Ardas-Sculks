package sculk.of.ixra.client.gui;

import sculk.of.ixra.world.inventory.ArdasSculksBookMenu2Menu;
import sculk.of.ixra.network.ArdasSculksBookMenu2ButtonMessage;

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

public class ArdasSculksBookMenu2Screen extends AbstractContainerScreen<ArdasSculksBookMenu2Menu> {
	private final static HashMap<String, Object> guistate = ArdasSculksBookMenu2Menu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	ImageButton imagebutton_bookbutton;
	ImageButton imagebutton_pagearrow;

	public ArdasSculksBookMenu2Screen(ArdasSculksBookMenu2Menu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 88;
		this.imageHeight = 88;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("sculks_of_arda:textures/screens/ardas_sculks_book_menu_2.png");

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
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu_2.label_ores"), -86, -21, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.ardas_sculks_book_menu_2.label_read"), -29, -21, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		imagebutton_bookbutton = new ImageButton(this.leftPos + -39, this.topPos + -23, 44, 15,
				new WidgetSprites(ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png"), ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png")), e -> {
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_bookbutton", imagebutton_bookbutton);
		this.addRenderableWidget(imagebutton_bookbutton);
		imagebutton_pagearrow = new ImageButton(this.leftPos + -83, this.topPos + 112, 18, 10,
				new WidgetSprites(ResourceLocation.parse("sculks_of_arda:textures/screens/pagearrow.png"), ResourceLocation.parse("sculks_of_arda:textures/screens/pagearrow.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new ArdasSculksBookMenu2ButtonMessage(1, x, y, z));
						ArdasSculksBookMenu2ButtonMessage.handleButtonAction(entity, 1, x, y, z);
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
}
