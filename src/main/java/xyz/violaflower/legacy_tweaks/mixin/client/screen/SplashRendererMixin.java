package xyz.violaflower.legacy_tweaks.mixin.client.screen;

import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.violaflower.legacy_tweaks.helper.tweak.world.EyeCandyHelper;
import xyz.violaflower.legacy_tweaks.tweaks.Tweaks;
import xyz.violaflower.legacy_tweaks.util.client.ScreenUtil;

@Mixin(SplashRenderer.class)
public abstract class SplashRendererMixin {
    @Mutable
    @Final
    @Shadow
    private final String splash;

    protected SplashRendererMixin(String splash) {
        this.splash = splash;
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(GuiGraphics guiGraphics, int screenWidth, Font font, int color, CallbackInfo ci) {
        int y = -8;
        float scale = 1.5f;
        float xPos = 0;
        float yPos = 0;
        if (Tweaks.LEGACY_UI.generalScreenTweaks.useLegacyTitleLogo.isOn()) y = 0;
        if (ScreenUtil.isLargeGui()) {
            scale = 0.8f;
            xPos = -45;
            yPos = (float) -34.5;
        }
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float) screenWidth / 2.0F + 123.0F + xPos, 69.0F + yPos, 0.0F);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(-15.0F));
        float f = 1.8F - Mth.abs(Mth.sin((float) (Util.getMillis() % 1000L) / 1000.0F * ((float) Math.PI * 2F)) * 0.1F);
        f = (f * 100.0F / (float) (font.width(this.splash) + 32));
        guiGraphics.pose().scale(f * scale, f * scale, f * scale);
        EyeCandyHelper.setCompactText(true);
        EyeCandyHelper.setLegacyTextShadows(false);
        guiGraphics.drawCenteredString(font, this.splash, 0, y, 16776960 | color);
        EyeCandyHelper.setLegacyTextShadows(true);
        EyeCandyHelper.setCompactText(false);
        guiGraphics.pose().popPose();
        ci.cancel();
    }
}
