package xyz.violaflower.legacy_tweaks.client.gui.screen.legacy;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xyz.violaflower.legacy_tweaks.util.client.ScreenUtil;

public class LegacyScreen extends Screen {
    protected LegacyScreen(Component title) {
        super(title);
    }

    public int getButtonWidth() {
        if (ScreenUtil.isLargeGui()) {
            return 150;
        } else {
            return 225;
        }
    }

    public int getFrameWidth() {
        if (ScreenUtil.isLargeGui()) {
            return 160;
        } else {
            return 210;
        }
    }

    public int getFrameHeight() {
        if (ScreenUtil.isLargeGui()) {
            return 192;
        } else {
            return 256;
        }
    }

    public int getButtonHeight() {
        return 20;
    }

    public int getButtonHeightPos() {
        if (ScreenUtil.isLargeGui()) {
            return 65;
        } else {
            return 125;
        }
    }

    public int getButtonSpacing() {
        return 21/4;
    }

//    // TODO ~~double check if this is even safe; may be better to go with a different system later on~~
//    // TODO: update, this is not safe - Jab125
//    public void tick() {
//        Minecraft.getInstance().resizeDisplay();
//        this.init();
////        super.tick();
//    }


    @Override
    public final void tick() {
        super.tick();
        tick2();
        checkForScreenSize();
    }

    public void tick2() {

    }

    private Boolean hadLargeGui = null;
    private void checkForScreenSize() {
        Window window = Minecraft.getInstance().getWindow();
        int screenHeight = window.getScreenHeight();
        boolean gu = screenHeight >= 720;
        if (hadLargeGui == null) {
            hadLargeGui = screenHeight >= 720;
        }
        boolean valueChanged = hadLargeGui != gu;
        if (valueChanged) {
            Minecraft.getInstance().resizeDisplay();
            hadLargeGui = gu;
            this.init();
        }
    }

    @Override
    protected void renderPanorama(GuiGraphics guiGraphics, float f) {
        PANORAMA.render(guiGraphics, this.width, this.height, 1, f);
    }
}
