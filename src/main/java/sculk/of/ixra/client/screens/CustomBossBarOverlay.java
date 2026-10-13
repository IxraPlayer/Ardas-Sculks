package sculk.of.ixra.client.screens;

import sculk.of.ixra.entity.ShadowHunterEntity;

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Bosslar icin ozel boss bar. Vanilla bar yerine background -> fill -> frame katmanlarini cizer.
 * Hangi boss'un hangi texture setini kullanacagi asagidaki STYLES tablosunda; yeni boss eklemek icin
 * tek satir yeterli. Tum frame'ler ayni olcude olmali: 428x34, ic bosluk 364x10 ve (32,12) konumunda.
 *
 * Faz destegi: Nyxar'da sunucu faz 2'de bar rengini PURPLE yapar (ShadowHunterEntity#customServerAiStep),
 * bu yuzden faz bilgisi ekstra paket gerekmeden bar renginden okunur.
 */
@EventBusSubscriber(value = {Dist.CLIENT})
public class CustomBossBarOverlay {

	/** Bir boss'un texture seti. phase2 null ise boss'un ikinci fazi yoktur. */
	/** Boss'a ozel bar efekti turu. NONE = duz bar. */
	private enum FxKind {
		NONE, NYXAR, GOLEM, WARDEN
	}

	private record Style(Layers phase1, Layers phase2, FxKind fx) {
	}

	private record Layers(ResourceLocation background, ResourceLocation fill, ResourceLocation frame) {
	}

	private static ResourceLocation tex(String name) {
		return ResourceLocation.parse("sculks_of_arda:textures/screens/" + name + ".png");
	}

	private static Layers layers(String prefix, String suffix) {
		return new Layers(tex(prefix + "_background" + suffix), tex(prefix + "_fill" + suffix), tex(prefix + "_frame" + suffix));
	}

	/** Anahtar: entity'nin ceviri anahtari (boss bar adi bu anahtardan gelir). */
	private static final Map<String, Style> STYLES = Map.of(
			// Nyxar: mavi -> mor (faz 2). Dosya adlari: nyxar_bar_background[_phase2], nyxar_bar_fill_phase1/2, nyxar_bar_frame_phase1/2
			"entity.sculks_of_arda.shadow_hunter",
			new Style(
					new Layers(tex("nyxar_bar_background"), tex("nyxar_bar_fill_phase1"), tex("nyxar_bar_frame_phase1")),
					new Layers(tex("nyxar_bar_background_phase2"), tex("nyxar_bar_fill_phase2"), tex("nyxar_bar_frame_phase2")), FxKind.NYXAR),
			// Sculk Golem: deepslate + turkuaz sculk
			"entity.sculks_of_arda.sculk_boss_1_t", new Style(layers("sculk_golem_bar", ""), null, FxKind.GOLEM),
			// Radioactive Warden: toksik yesil
			"entity.sculks_of_arda.radioactive_warden", new Style(layers("radioactive_warden_bar", ""), null, FxKind.WARDEN));

	// Texture olculeri
	private static final int FRAME_W = 428;
	private static final int FRAME_H = 34;
	private static final int BAR_W = 364;
	private static final int BAR_H = 10;
	// Frame'in ic bosluk (cukur) konumu: frame icinde (32, 12)
	private static final int BAR_OFFSET_X = 32;
	private static final int BAR_OFFSET_Y = 12;


	// ------------------------------------------------------------------ boss'a ozel efektler

	/** Boss bar basina (UUID) animasyon durumu. */
	private static final class FxState {
		float ghost;          // kaybedilen cani gosteren iz; gercek cana dogru erir
		float lastProgress;
		float lastUpdate;
		float lastSeen;
		float hitTime = -1000.0F;
		float shakeUntil = -1000.0F;
		float shakeAmp = 1.0F;
		float flashStart = -1000.0F;
		float waveStart = -1000.0F; // Golem: sculk dalgasinin basladigi an
		int waveX;                  // Golem: dalganin basladigi dolgu ucu (px)
		boolean phase2;
	}

	private static final Map<UUID, FxState> FX = new HashMap<>();

	private static final float HIT_MIN_DROP = 0.0015F;   // bu kadar can kaybi "vurulma" sayilir
	private static final float STREAK_PERIOD = 36.0F;    // Nyxar: tick, isik supurmesinin bir turu
	private static final int STREAK_LEN = 18;            // Nyxar: px

	// Boss basina ayarlar: hasar izi bekleme/erime hizi, titreme suresi/siddeti
	private static float ghostDelay(FxKind k) {
		return k == FxKind.GOLEM ? 14.0F : 8.0F;
	}

	private static float ghostCatchup(FxKind k) {
		return k == FxKind.WARDEN ? 0.28F : 0.14F;
	}

	private static float shakeTicks(FxKind k, boolean big) {
		return switch (k) {
			case GOLEM -> big ? 8.0F : 6.0F;
			case WARDEN -> big ? 7.0F : 5.0F;
			default -> big ? 8.0F : 5.0F;
		};
	}

	private static float shakeStrength(FxKind k, boolean big) {
		return k == FxKind.GOLEM ? 1.0F : (big ? 2.0F : 1.0F); // Golem agir: hep 1 px
	}

	private static int hash(int a, int b) {
		return ((a * 73856093) ^ (b * 19349663) ^ 0x5bd1e995) & 0x7fffffff;
	}

	private static FxState fxState(UUID id, float progress, int fillW, boolean phase2, float now, FxKind kind) {
		// Bitmis/uzak barlarin durumunu temizle (dunya degisince zaman geri sarabilir)
		for (Iterator<FxState> it = FX.values().iterator(); it.hasNext();) {
			FxState o = it.next();
			if (o.lastSeen < now - 200.0F || o.lastSeen > now + 5.0F)
				it.remove();
		}
		FxState st = FX.get(id);
		if (st == null) {
			st = new FxState();
			st.ghost = progress;
			st.lastProgress = progress;
			st.lastUpdate = now;
			st.phase2 = phase2;
			FX.put(id, st);
		}
		float dt = Mth.clamp(now - st.lastUpdate, 0.0F, 5.0F);
		st.lastUpdate = now;
		st.lastSeen = now;

		float drop = st.lastProgress - progress;
		if (drop > HIT_MIN_DROP) {
			boolean big = drop > 0.02F;
			st.hitTime = now;
			st.shakeUntil = now + shakeTicks(kind, big);
			st.shakeAmp = shakeStrength(kind, big);
			if (kind == FxKind.GOLEM) {
				st.waveStart = now;
				st.waveX = fillW;
			}
		}
		st.lastProgress = progress;

		if (progress >= st.ghost) {
			st.ghost = progress; // iyilesme: iz aninda yetisir
		} else if (now - st.hitTime > ghostDelay(kind)) {
			float diff = st.ghost - progress;
			st.ghost -= Math.max(diff * Math.min(1.0F, dt * ghostCatchup(kind)), 0.0015F * dt);
			if (st.ghost < progress)
				st.ghost = progress;
		}

		if (phase2 != st.phase2) { // faz gecisi: flas + sarsinti
			st.phase2 = phase2;
			st.flashStart = now;
			st.shakeUntil = now + 10.0F;
			st.shakeAmp = 2.0F;
		}
		return st;
	}

	private static int argb(float alpha, int rgb) {
		return (Mth.clamp((int) (alpha * 255.0F), 0, 255) << 24) | (rgb & 0xFFFFFF);
	}

	/** Dolgunun arkasinda kalan "hasar izi" (afterimage). Dolgudan once cizilir. */
	private static void drawGhost(GuiGraphics g, FxState st, int fillW, boolean phase2, float now, FxKind kind) {
		int ghostW = Mth.clamp(Mth.ceil(BAR_W * st.ghost), 0, BAR_W);
		if (ghostW <= fillW)
			return;
		int x0 = BAR_OFFSET_X + fillW;
		int x1 = BAR_OFFSET_X + ghostW;
		float fresh = Mth.clamp(1.0F - (now - st.hitTime - ghostDelay(kind)) / 40.0F, 0.0F, 1.0F);
		float a = 0.55F + 0.35F * fresh;
		int top;
		int bot;
		switch (kind) {
			case GOLEM -> {
				top = 0xD8FFF8;
				bot = 0x1FADA6;
			}
			case WARDEN -> {
				top = 0xF4FF9A;
				bot = 0x6AB300;
			}
			default -> {
				top = phase2 ? 0xF2DCFF : 0xCFFFFF;
				bot = phase2 ? 0x9A5CE0 : 0x2FB5C8;
			}
		}
		g.fillGradient(x0, BAR_OFFSET_Y, x1, BAR_OFFSET_Y + BAR_H, argb(a, top), argb(a * 0.8F, bot));
		// izin en ucu parlak ince cizgi
		g.fill(x1 - 1, BAR_OFFSET_Y, x1, BAR_OFFSET_Y + BAR_H, argb(a, 0xFFFFFF));
	}

	// ------------------------------------------------------------------ NYXAR

	/** Dolgu uzerinden gecen egik isik + ucta titreyen parlama + dusuk canda nabiz. Dolgudan sonra cizilir. */
	private static void drawSpeedFx(GuiGraphics g, FxState st, int fillW, float progress, boolean phase2, float now) {
		if (fillW <= 0)
			return;
		int tint = phase2 ? 0xF0D8FF : 0xCFFFFF;

		// 1) egik isik suprulmesi (soldan saga, hizli)
		float t = (now % STREAK_PERIOD) / STREAK_PERIOD;
		float head = t * (fillW + STREAK_LEN + 12) - STREAK_LEN;
		for (int row = 0; row < BAR_H; row++) {
			float slant = (BAR_H - 1 - row) * 1.3F; // alt satirlar geride -> egik gorunum
			for (int i = 0; i < STREAK_LEN; i += 2) {
				int x = Math.round(head + i - slant);
				if (x < 0 || x + 2 > fillW)
					continue;
				float prof = i < 13 ? (i + 1) / 13.0F : (STREAK_LEN - i) / 5.0F;
				float a = 0.42F * Mth.clamp(prof, 0.0F, 1.0F);
				g.fill(BAR_OFFSET_X + x, BAR_OFFSET_Y + row, BAR_OFFSET_X + x + 2, BAR_OFFSET_Y + row + 1, argb(a, tint));
			}
		}

		// 2) dolgunun ucunda titreyen parlama
		float flick = 0.75F + 0.25F * Mth.sin(now * 2.1F) * Mth.sin(now * 0.7F + 1.0F);
		int ex = BAR_OFFSET_X + fillW;
		g.fill(ex - 1, BAR_OFFSET_Y, ex, BAR_OFFSET_Y + BAR_H, argb(flick, 0xFFFFFF));
		for (int k = 2; k <= 5; k++)
			g.fill(ex - k, BAR_OFFSET_Y, ex - k + 1, BAR_OFFSET_Y + BAR_H, argb(0.34F * flick / k * 2.0F, tint));

		// 3) dusuk canda nabiz (can < %25): azaldikca hizlanir
		if (progress < 0.25F) {
			float urgency = 1.0F + (0.25F - progress) * 12.0F;
			float pulse = 0.5F + 0.5F * Mth.sin(now * 0.55F * urgency);
			g.fill(BAR_OFFSET_X, BAR_OFFSET_Y, BAR_OFFSET_X + fillW, BAR_OFFSET_Y + BAR_H, argb(0.05F + 0.18F * pulse, 0xFFFFFF));
		}
	}

	/** Faz gecisi flasi: tum cubugun uzerinde sonen beyaz patlama. */
	private static void drawFlash(GuiGraphics g, FxState st, boolean phase2, float now) {
		float t = (now - st.flashStart) / 14.0F;
		if (t < 0.0F || t >= 1.0F)
			return;
		float a = (1.0F - t) * (1.0F - t) * 0.8F;
		g.fill(BAR_OFFSET_X, BAR_OFFSET_Y, BAR_OFFSET_X + BAR_W, BAR_OFFSET_Y + BAR_H, argb(a, phase2 ? 0xE9CFFF : 0xFFFFFF));
	}

	// ------------------------------------------------------------------ SCULK GOLEM

	/** Kalp atisi parlamasi + vurulunca turkuaz sculk dalgasi + sonup yanan sculk kivilcimlari. */
	private static void drawGolemFx(GuiGraphics g, FxState st, int fillW, float progress, float now) {
		if (fillW <= 0)
			return;
		int bx = BAR_OFFSET_X;
		int by = BAR_OFFSET_Y;

		// 1) kalp atisi: dusuk canda hizlanir
		float period = progress < 0.25F ? 24.0F + progress * 104.0F : 50.0F;
		float ph = (now % period) / period;
		float sn = Mth.sin(ph * (float) (Math.PI * 2.0D));
		float beat = sn > 0.0F ? sn * sn * sn : 0.0F;
		g.fill(bx, by, bx + fillW, by + BAR_H, argb(0.05F + 0.20F * beat, 0x63F4F4));

		// 2) sculk dalgasi: dolgu ucundan sola, iki halka
		for (int w = 0; w < 2; w++) {
			float delay = w == 0 ? 0.0F : 7.0F;
			float strength = w == 0 ? 1.0F : 0.5F;
			float age = now - st.waveStart - delay;
			if (age < 0.0F || age >= 30.0F)
				continue;
			float pos = st.waveX - age * (st.waveX + 8) / 30.0F;
			float fade = (1.0F - age / 30.0F) * strength;
			int cx = Math.round(pos);
			for (int dx = -4; dx <= 4; dx++) {
				int x = cx + dx;
				if (x < 0 || x >= fillW)
					continue;
				float prof = 1.0F - Math.abs(dx) / 5.0F;
				g.fill(bx + x, by, bx + x + 1, by + BAR_H, argb(0.6F * prof * fade, 0xAFFDFA));
			}
		}

		// 3) sonup yanan sculk kivilcimlari
		if (fillW > 6) {
			for (int slot = 0; slot < 8; slot++) {
				int len = 22 + slot * 6;
				int cyc = (int) (now / len);
				float q = (now % len) / len;
				int x = hash(slot, cyc) % fillW;
				int y = hash(slot + 31, cyc) % BAR_H;
				float s = Mth.sin(q * (float) Math.PI);
				g.fill(bx + x, by + y, bx + x + 1, by + y + 1, argb(0.95F * s * s, 0xF6FFFD));
			}
		}
	}

	// ------------------------------------------------------------------ RADIOACTIVE WARDEN

	/** Dalgalanan toksik sivi + yukselen kabarciklar + vurulunca sari-yesil flas + dusuk canda yesil nabiz. */
	private static void drawWardenFx(GuiGraphics g, FxState st, int fillW, float progress, float now) {
		if (fillW <= 0)
			return;
		int bx = BAR_OFFSET_X;
		int by = BAR_OFFSET_Y;

		// 1) sivi dalgasi: sari parlak dalga + koyu karsi dalga
		for (int x = 0; x < fillW; x += 2) {
			int x2 = Math.min(x + 2, fillW);
			float a = Math.max(0.0F, 0.10F + 0.10F * Mth.sin(x * 0.10F - now * 0.30F));
			g.fill(bx + x, by, bx + x2, by + BAR_H, argb(a, 0xE4E000));
			float d = 0.14F * Math.max(0.0F, Mth.sin(x * 0.07F + now * 0.20F));
			g.fill(bx + x, by, bx + x2, by + BAR_H, argb(d, 0x0C1709));
		}

		// 2) yukselen toksik kabarciklar
		if (fillW > 6) {
			for (int slot = 0; slot < 6; slot++) {
				int len = 26 + slot * 7;
				int cyc = (int) (now / len);
				float q = (now % len) / len;
				int x = hash(slot + 7, cyc) % fillW;
				int y = BAR_H - 1 - (int) (q * (BAR_H - 1));
				g.fill(bx + x, by + y, bx + x + 1, by + y + 1, argb(0.9F * (1.0F - q * 0.7F), 0xF4FF8A));
			}
		}

		// 3) dusuk canda yesil nabiz (can < %25): azaldikca hizlanir
		if (progress < 0.25F) {
			float urgency = 1.0F + (0.25F - progress) * 12.0F;
			float pulse = 0.5F + 0.5F * Mth.sin(now * 0.6F * urgency);
			g.fill(bx, by, bx + fillW, by + BAR_H, argb(0.05F + 0.20F * pulse, 0xB7FF00));
		}

		// 4) vurulunca sari-yesil flas
		float ft = (now - st.hitTime) / 8.0F;
		if (ft >= 0.0F && ft < 1.0F)
			g.fill(bx, by, bx + fillW, by + BAR_H, argb(0.55F * (1.0F - ft) * (1.0F - ft), 0xE4FF4A));
	}

	/** Cercevenin altindan suzulen radyoaktif damlalar. Cerceveden SONRA cizilir (frame koordinatlari). */
	private static void drawWardenDrips(GuiGraphics g, int fillW, float now) {
		if (fillW < 40)
			return;
		for (int slot = 0; slot < 5; slot++) {
			int len = 40 + slot * 13;
			int cyc = (int) (now / len);
			float q = (now % len) / len;
			int x = BAR_OFFSET_X + 8 + hash(slot + 101, cyc) % (fillW - 16);
			if (q < 0.12F) { // damla dudakta sisiyor
				g.fill(x, 26, x + 1, 27, argb(0.9F, 0x6AB300));
			} else if (q < 0.60F) {
				float t = (q - 0.12F) / 0.48F;
				int yy = 26 + (int) (8.0F * t * t);
				float a = 1.0F - (float) Math.pow(t, 1.5D);
				g.fill(x, yy, x + 1, yy + 1, argb(a, 0xE4E000));
				g.fill(x, yy - 1, x + 1, yy, argb(0.8F * a, 0xB7FF00));
			}
		}
	}

	/** Faz 1 -> faz 2 renk gecisinin suresi (tick). Boss'un donusum suresiyle ayni. */
	private static final float PHASE2_FADE_TICKS = ShadowHunterEntity.PHASE2_TRANSITION_TICKS;

	/** mix 0 = sadece a, 1 = sadece b, arasi: a'nin ustune b'yi mix saydamligiyla bindirir (capraz gecis). */
	private static void blitMixed(GuiGraphics g, ResourceLocation a, ResourceLocation b, float mix, int x, int y, int w, int h, int texW, int texH) {
		if (mix >= 1.0F) {
			g.blit(b, x, y, 0, 0, w, h, texW, texH);
			return;
		}
		g.blit(a, x, y, 0, 0, w, h, texW, texH);
		if (mix > 0.0F) {
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, mix);
			g.blit(b, x, y, 0, 0, w, h, texW, texH);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@SubscribeEvent
	public static void onBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
		LerpingBossEvent boss = event.getBossEvent();
		Style style = styleFor(boss);
		if (style == null)
			return;
		// Vanilla bar + isim yazisi iptal edilir, ikisini de biz ciziyoruz.
		event.setCanceled(true);

		boolean phase2 = style.phase2() != null && boss.getColor() == BossEvent.BossBarColor.PURPLE;
		Layers l1 = style.phase1();
		Layers l2 = style.phase2() != null ? style.phase2() : l1;

		GuiGraphics g = event.getGuiGraphics();
		Minecraft mc = Minecraft.getInstance();
		int screenW = g.guiWidth();

		// Dar ekranlarda (yuksek GUI scale) frame tasmasin diye kucult.
		float scale = Math.min(1.0F, (screenW - 8) / (float) FRAME_W);
		int frameY = event.getY();

		// Isim: frame'in ustunde, vanilla gibi ortali
		Component name = boss.getName();
		int nameW = mc.font.width(name);
		g.drawString(mc.font, name, screenW / 2 - nameW / 2, frameY - 9, 0xFFFFFF);

		float progress = Mth.clamp(boss.getProgress(), 0.0F, 1.0F);
		int fillW = progress > 0.0F ? Mth.clamp(Mth.ceil(BAR_W * progress), 1, BAR_W) : 0;
		FxKind kind = style.fx();
		FxState fx = null;
		float now = 0.0F;
		int shakeX = 0, shakeY = 0;
		if (kind != FxKind.NONE && mc.level != null) {
			now = mc.level.getGameTime() + event.getPartialTick().getGameTimeDeltaPartialTick(false);
			fx = fxState(boss.getId(), progress, fillW, phase2, now, kind);
			float left = fx.shakeUntil - now;
			if (left > 0.0F) { // vurulunca kisa, sonen titreme
				float amp = Math.min(fx.shakeAmp, left * 0.5F + 0.5F);
				shakeX = Math.round(Mth.sin(now * 7.3F) * amp);
				shakeY = Math.round(Mth.cos(now * 9.1F) * amp * 0.5F);
			}
		}

		// Faz 2 gecisi: bar, bossun donusumuyle ayni surede (2 sn) maviden mora yumusakca karisir.
		float mix = phase2 ? 1.0F : 0.0F;
		if (phase2 && fx != null) {
			float t = Mth.clamp((now - fx.flashStart) / PHASE2_FADE_TICKS, 0.0F, 1.0F);
			mix = t * t * (3.0F - 2.0F * t); // smoothstep
		}
		boolean phase2Visual = mix >= 0.5F; // renk secimli efektler yarida mora gecer

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		g.pose().pushPose();
		g.pose().translate(screenW / 2.0F + shakeX, frameY + shakeY, 0.0F);
		g.pose().scale(scale, scale, 1.0F);
		g.pose().translate(-FRAME_W / 2.0F, 0.0F, 0.0F);

		// 1) arka plan
		blitMixed(g, l1.background(), l2.background(), mix, BAR_OFFSET_X, BAR_OFFSET_Y, BAR_W, BAR_H, BAR_W, BAR_H);
		// 2) dolgu (can orani kadar, soldan kirpilir)
		if (fx != null)
			drawGhost(g, fx, fillW, phase2Visual, now, kind);
		if (fillW > 0)
			blitMixed(g, l1.fill(), l2.fill(), mix, BAR_OFFSET_X, BAR_OFFSET_Y, fillW, BAR_H, BAR_W, BAR_H);
		if (fx != null) {
			switch (kind) {
				case NYXAR -> {
					drawSpeedFx(g, fx, fillW, progress, phase2Visual, now);
					drawFlash(g, fx, phase2, now);
				}
				case GOLEM -> drawGolemFx(g, fx, fillW, progress, now);
				case WARDEN -> drawWardenFx(g, fx, fillW, progress, now);
				default -> {
				}
			}
		}
		// 3) cerceve en ustte
		blitMixed(g, l1.frame(), l2.frame(), mix, 0, 0, FRAME_W, FRAME_H, FRAME_W, FRAME_H);
		if (fx != null && kind == FxKind.WARDEN)
			drawWardenDrips(g, fillW, now);

		g.pose().popPose();
		RenderSystem.disableBlend();

		// Sonraki bossbar (varsa) cercevenin altindan baslasin (isim icin 10px pay)
		event.setIncrement(Math.round(FRAME_H * scale) + 12);
	}

	private static Style styleFor(LerpingBossEvent boss) {
		if (boss.getName().getContents() instanceof TranslatableContents tc)
			return STYLES.get(tc.getKey());
		return null;
	}
}
