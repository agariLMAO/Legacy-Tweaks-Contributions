package xyz.violaflower.legacy_tweaks.gui;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.violaflower.legacy_tweaks.LegacyTweaks;
import xyz.violaflower.legacy_tweaks.tweaks.Tweak;
import xyz.violaflower.legacy_tweaks.tweaks.TweakManager;

import java.util.List;
import java.util.Map;

public class LTScreen extends Screen {
    private static final Component TITLE = Component.translatable("lt.screens.ltscreen.title");
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private final Screen parent;
    @Nullable
    private SettingList settingList;

    public LTScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
    }

    @Override
    public void init() {
        TweakManager manager = TweakManager.getInstance();
        this.layout.addTitleHeader(TITLE, this.font);
        this.settingList = this.layout.addToContents(new SettingList(manager));
        LinearLayout linearLayout = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
        linearLayout.addChild(Button.builder(CommonComponents.GUI_DONE, button -> this.minecraft.setScreen(parent)).build());
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        if (this.settingList != null) {
            this.settingList.updateSize(this.width, this.layout);
        }
    }


    class SettingList extends ContainerObjectSelectionList<SettingList.SettingEntry> {
        private static final int ITEM_HEIGHT = 40;

        public SettingList(final TweakManager tweakManager) {
            super(
                    Minecraft.getInstance(),
                    LTScreen.this.width,
                    LTScreen.this.layout.getContentHeight(),
                    LTScreen.this.layout.getHeaderHeight(),
                    ITEM_HEIGHT
            );

            tweakManager.tweaks
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(tweak -> {
                        this.addEntry(new SettingEntry(tweak.getValue()));
                    });
        }

        class SettingEntry extends ContainerObjectSelectionList.Entry<SettingEntry> {
            private final Component title;
            private final List<FormattedCharSequence> label;
            private final Button toggleButton;
            private final Button settingsButton;
            protected final List<AbstractWidget> children = Lists.newArrayList();

            public SettingEntry(final Tweak tweak) {
                this.title = Component.literal(tweak.getTweakID() + " - " + tweak.getTweakAuthor());
                this.label = LTScreen.this.minecraft.font.split(Component.literal(tweak.getTweakDescription()).withStyle(ChatFormatting.GRAY), 175);

                toggleButton = Button.builder(Component.translatable(tweak.isEnabled() ? "lt.main.enabled" : "lt.main.disabled"), button -> {
                    tweak.setEnabled(!tweak.isEnabled());
                    button.setMessage(Component.translatable(tweak.isEnabled() ? "lt.main.enabled" : "lt.main.disabled"));
                    TweakManager.save();
                }).size(20, 20).build();
                settingsButton = Button.builder(Component.translatable("lt.main.settings"), button -> {
                    // https://www.minecraftforum.net/forums/archive/alpha/alpha-survival-single-player/798878-dohasdoshih-analysis-of-glitched-chunks
                    LegacyTweaks.LOGGER.info("DOHASDOSHIH!");
                }).size(20, 20).build();
            }

            @Override
            public @NotNull List<? extends GuiEventListener> children() {
                return this.children;
            }

            @Override
            public @NotNull List<? extends NarratableEntry> narratables() {
                return this.children;
            }

            @Override
            public void render(GuiGraphics guiGraphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                int tOff = this.label.size() == 1 ? 5 : 0;
                guiGraphics.drawString(LTScreen.this.minecraft.font, title, x, y + tOff, 0xFFFFFFFF, false);
                if (this.label.size() == 1) {
                    guiGraphics.drawString(LTScreen.this.minecraft.font, this.label.get(0), x, y + 10 + tOff, 0xFFFFFFFF, false);
                } else if (this.label.size() >= 2) {
                    guiGraphics.drawString(LTScreen.this.minecraft.font, this.label.get(0), x, y + 10 + tOff, 0xFFFFFFFF, false);
                    guiGraphics.drawString(LTScreen.this.minecraft.font, this.label.get(1), x, y + 20 + tOff, 0xFFFFFFFF, false);
                }

                int off = 5;

                this.toggleButton.setX(x + entryWidth - 21);
                this.toggleButton.setY(y + off);
                this.settingsButton.setX(x + entryWidth - 42);
                this.settingsButton.setY(y + off);
                this.toggleButton.render(guiGraphics, mouseX, mouseY, tickDelta);
                this.settingsButton.render(guiGraphics, mouseX, mouseY, tickDelta);
            }
        }
    }
}
