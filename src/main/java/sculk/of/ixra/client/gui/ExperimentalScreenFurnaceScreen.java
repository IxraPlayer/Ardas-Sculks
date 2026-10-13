package sculk.of.ixra.client.gui;

import sculk.of.ixra.world.inventory.ExperimentalScreenFurnaceMenu;
import sculk.of.ixra.network.ExperimentalScreenFurnaceButtonMessage;

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

public class ExperimentalScreenFurnaceScreen extends AbstractContainerScreen<ExperimentalScreenFurnaceMenu> {
	private final static HashMap<String, Object> guistate = ExperimentalScreenFurnaceMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	ImageButton imagebutton_bookbutton;

	public ExperimentalScreenFurnaceScreen(ExperimentalScreenFurnaceMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 88;
		this.imageHeight = 88;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("sculks_of_arda:textures/screens/experimental_screen_furnace.png");

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
		guiGraphics.blit(ResourceLocation.parse("sculks_of_arda:textures/screens/book2.png"), this.leftPos + -29, this.topPos + -41, 0, 0, 146, 180, 146, 180);
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
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.experimental_screen_furnace.label_danger"), 20, -27, -1966080, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.experimental_screen_furnace.label_it_is_experimental_there_may_be"), -11, -5, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.experimental_screen_furnace.label_there_may_be_errors"), -11, 4, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.experimental_screen_furnace.label_do_you_still_want"), -11, 31, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.experimental_screen_furnace.label_want_to_use_it"), -11, 40, -16764109, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.sculks_of_arda.experimental_screen_furnace.label_yes"), 32, 105, -16764109, false);
	}

	@Override
	public void init() {
		super.init();
		imagebutton_bookbutton = new ImageButton(this.leftPos + 19, this.topPos + 103, 44, 15,
				new WidgetSprites(ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png"), ResourceLocation.parse("sculks_of_arda:textures/screens/bookbutton.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new ExperimentalScreenFurnaceButtonMessage(0, x, y, z));
						ExperimentalScreenFurnaceButtonMessage.handleButtonAction(entity, 0, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_bookbutton", imagebutton_bookbutton);
		this.addRenderableWidget(imagebutton_bookbutton);
	}
}
