package xyz.violaflower.legacy_tweaks.tweaks.impl;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import xyz.violaflower.legacy_tweaks.tweaks.Tweak;

public class EyeCandy extends Tweak {
	public EyeCandy() {
		setTweakID("Eye Candy");
		setGroup();
		setTweakAuthor("Jab125");
		setTweakDescription("Various cosmetic changes");
		addSubTweak(new LegacyTextShadows());
		addSubTweak(new FineTunedUIScale());
	}
	public static class LegacyTextShadows extends Tweak {
		public final IntSliderOption shadowOffset;
		public LegacyTextShadows() {
			setTweakID("Legacy Text Shadows");
			setTweakAuthor("Jab125");
			setTweakDescription("Makes shadows consistent with the screen's actual size, not the scaled size.");
			shadowOffset = addSliderOption("shadowOffset", 0, 10);
			shadowOffset.set(1);
		}
	}
	public static class FineTunedUIScale extends Tweak {
		public final DoubleSliderOption uiScale;
		public FineTunedUIScale() {
			setTweakID("Fine Tuned UI Scale");
			setTweakAuthor("Jab125");
			setTweakDescription(ChatFormatting.RED + "Don't use this unless you know what you're doing!");
			setEnabled(false, false);
			uiScale = addSliderOption("uiScale", 0.8D, 5D);
			uiScale.set(1d);
			uiScale.setConsumer(d -> {if (isEnabled()) Minecraft.getInstance().resizeDisplay();});
		}

		@Override
		public void onToggled() {
			Minecraft.getInstance().resizeDisplay();
		}
	}
}
