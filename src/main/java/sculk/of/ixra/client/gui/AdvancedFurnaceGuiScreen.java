package sculk.of.ixra.client.gui;

import sculk.of.ixra.world.inventory.AdvancedFurnaceGuiMenu;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class AdvancedFurnaceGuiScreen extends AbstractContainerScreen<AdvancedFurnaceGuiMenu> {
	private final static HashMap<String, Object> guistate = AdvancedFurnaceGuiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;

	public AdvancedFurnaceGuiScreen(AdvancedFurnaceGuiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 207;
		this.imageHeight = 218;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("sculks_of_arda:textures/screens/advanced_furnace_gui.png");

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

		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/afurnacemcgui.png"), this.leftPos + 0, this.topPos + 0, 0, 0, 207, 218, 207, 218);

		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/afurnaceslot1.png"), this.leftPos + 134, this.topPos + 55, 0, 0, 28, 28, 28, 28);

		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/afurnaceslot2.png"), this.leftPos + 76, this.topPos + 94, 0, 0, 18, 18, 18, 18);

		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/afurnaceslot4.png"), this.leftPos + 103, this.topPos + 24, 0, 0, 18, 18, 18, 18);

		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/afurnaceslot4.png"), this.leftPos + 49, this.topPos + 24, 0, 0, 18, 18, 18, 18);

		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/furnaceongui.png"), this.leftPos + 76, this.topPos + 76, 0, 0, 17, 17, 17, 17);

		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/afurnacearrow10.png"), this.leftPos + 103, this.topPos + 61, 0, 0, 22, 16, 22, 16);

		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/afurnacearrow1.png"), this.leftPos + 67, this.topPos + 35, 0, 0, 35, 17, 35, 17);

		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/afurnaceslot5.png"), this.leftPos + 169, this.topPos + 55, 0, 0, 28, 28, 28, 28);

		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/craftrepiceguiadvancedfurnace.png"), this.leftPos + -86, this.topPos + 6, 0, 0, 76, 94, 76, 94);

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
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.advanced_furnace_gui.label_inventory"), 14, 123, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.advanced_furnace_gui.label_advanced_furnace"), 8, 7, -16777216, false);
	}

	@Override
	public void init() {
		super.init();
	}
}
