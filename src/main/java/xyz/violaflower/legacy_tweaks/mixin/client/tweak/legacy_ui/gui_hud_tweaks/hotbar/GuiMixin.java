package xyz.violaflower.legacy_tweaks.mixin.client.tweak.legacy_ui.gui_hud_tweaks.hotbar;

import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import xyz.violaflower.legacy_tweaks.helper.tweak.hud.HudElements;
import xyz.violaflower.legacy_tweaks.helper.tweak.hud.HudHelper;
import xyz.violaflower.legacy_tweaks.tweaks.Tweaks;
import xyz.violaflower.legacy_tweaks.util.common.assets.Sprites;

@Mixin(value = Gui.class, priority = -999999999)
public class GuiMixin {

    @Shadow
    private long healthBlinkTime;

    @Unique
    private int lastHotbarSelection = -1;

    @Inject(method = "renderHotbarAndDecorations", at = @At("HEAD"))
    private void startHotbarRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {

        int newSelection = Minecraft.getInstance().player != null ? Minecraft.getInstance().player.getInventory().selected : -1;
        if (lastHotbarSelection >= 0 && lastHotbarSelection != newSelection) HudHelper.lastHotbarSelectionChange = Util.getMillis();
        lastHotbarSelection = newSelection;
        HudHelper.start(guiGraphics, HudElements.HOTBAR);
    }

    @Inject(method = "renderHotbarAndDecorations", at = @At("TAIL"))
    private void endHotbarRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        HudHelper.end(guiGraphics);
    }

    @Inject(method = "renderOverlayMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V"), cancellable = true)
    private void startTooltipRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        HudHelper.start(guiGraphics, HudElements.HOTBAR);
    }

    @Inject(method = "renderOverlayMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", shift = At.Shift.AFTER))
    private void endTooltipRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        HudHelper.end(guiGraphics);
    }

    @Inject(method = "renderSelectedItemName", at = @At("HEAD"))
    private void startItemNameRender(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (HudHelper.guiHudTweaks.hotbarTweaks.legacyItemOverlay.isOn()) {
            HudHelper.startTooltip(guiGraphics, HudElements.ITEM_OVERLAY);
        }
    }

    @Inject(method = "renderSelectedItemName", at = @At("TAIL"))
    private void endItemNameRender(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (HudHelper.guiHudTweaks.hotbarTweaks.legacyItemOverlay.isOn()) {
            HudHelper.end(guiGraphics);
        }
    }

    @ModifyArgs(method = "renderItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1))
    private void changeHotbarTexture(Args args) {
        if (Tweaks.LEGACY_UI.guiHudTweaks.hotbarTweaks.useLegacyHotbarTexture.isOn()) {
            args.set(0, Sprites.HOTBAR_SELECTION);
            args.set(4, 24);
        }
    }

    @Redirect(method = /*? if (neoforge) {*//*"renderHealthLevel"*//*?} else {*/"renderPlayerHealth"/*?}*/, at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/Gui;healthBlinkTime:J", opcode = Opcodes.PUTFIELD, ordinal = 1))
    private void renderPlayerHealth(Gui instance, long value) {
        if (Tweaks.LEGACY_UI.guiHudTweaks.hotbarTweaks.fastHealthBlink.isOn()) healthBlinkTime = value - 6;
    }
}