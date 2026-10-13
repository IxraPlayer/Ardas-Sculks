package sculk.of.ixra.client.screens;

import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import com.mojang.blaze3d.systems.RenderSystem;

/**
 * Nyxar (shadow_hunter) icin ozel boss bar. Vanilla bar yerine background -> fill -> frame katmanlarini cizer.
 * Faz 2'de sunucu bar rengini PURPLE yaptigi icin (ShadowHunterEntity#customServerAiStep) faz bilgisi
 * ekstra paket gerekmeden buradan okunur.
 */
@EventBusSubscriber(value = {Dist.CLIENT})
public class NyxarBossBarOverlay {
	private static final String NYXAR_NAME_KEY = "entity.sculks_of_arda.shadow_hunter";

	private static final ResourceLocation BG_P1 = ResourceLocation.parse("sculks_of_arda:textures/screens/nyxar_bar_background.png");
	private static final ResourceLocation BG_P2 = ResourceLocation.parse("sculks_of_arda:textures/screens/nyxar_bar_background_phase2.png");
	private static final ResourceLocation FILL_P1 = ResourceLocation.parse("sculks_of_arda:textures/screens/nyxar_bar_fill_phase1.png");
	private static final ResourceLocation FILL_P2 = ResourceLocation.parse("sculks_of_arda:textures/screens/nyxar_bar_fill_phase2.png");
	private static final ResourceLocation FRAME_P1 = ResourceLocation.parse("sculks_of_arda:textures/screens/nyxar_bar_frame_phase1.png");
	private static final ResourceLocation FRAME_P2 = ResourceLocation.parse("sculks_of_arda:textures/screens/nyxar_bar_frame_phase2.png");

	// Texture olculeri
	private static final int FRAME_W = 428;
	private static final int FRAME_H = 34;
	private static final int BAR_W = 364;
	private static final int BAR_H = 10;
	// Frame'in ic bosluk (cukur) konumu: frame icinde (32, 12)
	private static final int BAR_OFFSET_X = 32;
	private static final int BAR_OFFSET_Y = 12;

	@SubscribeEvent
	public static void onBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
		LerpingBossEvent boss = event.getBossEvent();
		if (!isNyxar(boss))
			return;
		// Vanilla bar + isim yazisi iptal edilir, ikisini de biz ciziyoruz.
		event.setCanceled(true);

		GuiGraphics g = event.getGuiGraphics();
		Minecraft mc = Minecraft.getInstance();
		int screenW = g.guiWidth();
		boolean phase2 = boss.getColor() == BossEvent.BossBarColor.PURPLE;

		// Dar ekranlarda (yuksek GUI scale) frame tasmasin diye kucult.
		float scale = Math.min(1.0F, (screenW - 8) / (float) FRAME_W);
		int frameY = event.getY();

		// Isim: frame'in ustunde, vanilla gibi ortali
		Component name = boss.getName();
		int nameW = mc.font.width(name);
		g.drawString(mc.font, name, screenW / 2 - nameW / 2, frameY - 9, 0xFFFFFF);

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		g.pose().pushPose();
		g.pose().translate(screenW / 2.0F, frameY, 0.0F);
		g.pose().scale(scale, scale, 1.0F);
		g.pose().translate(-FRAME_W / 2.0F, 0.0F, 0.0F);

		// 1) arka plan
		g.blit(phase2 ? BG_P2 : BG_P1, BAR_OFFSET_X, BAR_OFFSET_Y, 0, 0, BAR_W, BAR_H, BAR_W, BAR_H);
		// 2) dolgu (can orani kadar, soldan kirpilir)
		float progress = Mth.clamp(boss.getProgress(), 0.0F, 1.0F);
		int fillW = progress > 0.0F ? Mth.clamp(Mth.ceil(BAR_W * progress), 1, BAR_W) : 0;
		if (fillW > 0)
			g.blit(phase2 ? FILL_P2 : FILL_P1, BAR_OFFSET_X, BAR_OFFSET_Y, 0, 0, fillW, BAR_H, BAR_W, BAR_H);
		// 3) cerceve en ustte
		g.blit(phase2 ? FRAME_P2 : FRAME_P1, 0, 0, 0, 0, FRAME_W, FRAME_H, FRAME_W, FRAME_H);

		g.pose().popPose();
		RenderSystem.disableBlend();

		// Sonraki bossbar (varsa) cercevenin altindan baslasin (isim icin 10px pay)
		event.setIncrement(Math.round(FRAME_H * scale) + 12);
	}

	private static boolean isNyxar(LerpingBossEvent boss) {
		return boss.getName().getContents() instanceof TranslatableContents tc && NYXAR_NAME_KEY.equals(tc.getKey());
	}
}
