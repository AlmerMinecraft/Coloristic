package net.almer.coloristic.client.presets;

import com.google.common.hash.Hashing;
import net.almer.coloristic.Coloristic;
import net.almer.coloristic.client.GameOptionsContainer;
import net.almer.coloristic.client.screen.PresetScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.client.gui.screen.world.WorldIcon;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteLoader;
import net.minecraft.resource.ResourcePackCompatibility;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.minecraft.util.Util;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class PresetListWidget extends AlwaysSelectedEntryListWidget<PresetListWidget.PresetEntry> {
    static final Identifier MOVE_UP_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("transferable_list/move_up_highlighted");
    static final Identifier MOVE_UP_TEXTURE = Identifier.ofVanilla("transferable_list/move_up");
    static final Identifier MOVE_DOWN_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("transferable_list/move_down_highlighted");
    static final Identifier MOVE_DOWN_TEXTURE = Identifier.ofVanilla("transferable_list/move_down");
    private final Text title;
    final PresetScreen screen;
    public PresetListWidget(MinecraftClient client, PresetScreen screen, int width, int height, Text title) {
        super(client, width, height, 33, 36);
        this.screen = screen;
        this.title = title;
        this.centerListVertically = false;
        Objects.requireNonNull(client.textRenderer);
        this.setRenderHeader(true, (int)(9.0F * 1.5F));
    }
    protected void renderHeader(DrawContext context, int x, int y) {
        Text text = Text.empty().append(this.title).formatted(new Formatting[]{Formatting.UNDERLINE, Formatting.BOLD});
        context.drawText(this.client.textRenderer, text, x + this.width / 2 - this.client.textRenderer.getWidth(text) / 2, Math.min(this.getY() + 3, y), -1, false);
    }
    public int getRowWidth() {
        return this.width;
    }
    protected int getScrollbarX() {
        return this.getRight() - 6;
    }
    protected void drawSelectionHighlight(DrawContext context, int y, int entryWidth, int entryHeight, int borderColor, int fillColor) {
        if (this.isScrollbarVisible()) {
            int j = this.getRowLeft() - 2;
            int k = this.getRight() - 6 - 1;
            int l = y - 2;
            int m = y + entryHeight + 2;
            context.fill(j, l, k, m, borderColor);
            context.fill(j + 1, l + 1, k - 1, m - 1, fillColor);
        } else {
            super.drawSelectionHighlight(context, y, entryWidth, entryHeight, borderColor, fillColor);
        }
    }
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.getSelectedOrNull() != null) {
            switch (keyCode) {
                case 32:
                case 257:
                    ((PresetEntry)this.getSelectedOrNull()).toggle();
                    return true;
                default:
                    if (Screen.hasShiftDown()) {
                        switch (keyCode) {
                            case 264:
                                ((PresetEntry)this.getSelectedOrNull()).moveTowardEnd();
                                return true;
                            case 265:
                                ((PresetEntry)this.getSelectedOrNull()).moveTowardStart();
                                return true;
                        }
                    }
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    @Environment(EnvType.CLIENT)
    public static class PresetEntry extends AlwaysSelectedEntryListWidget.Entry<PresetEntry> {
        private final PresetListWidget widget;
        protected final MinecraftClient client;
        private final OrderedText displayName;
        private final Preset preset;
        public PresetEntry(MinecraftClient client, PresetListWidget widget, Preset preset) {
            this.client = client;
            this.preset = preset;
            this.widget = widget;
            this.displayName = trimTextToWidth(client, Text.of(preset.getName()));
        }
        private static OrderedText trimTextToWidth(MinecraftClient client, Text text) {
            int i = client.textRenderer.getWidth(text);
            if (i > 157) {
                StringVisitable stringVisitable = StringVisitable.concat(new StringVisitable[]{client.textRenderer.trimToWidth(text, 157 - client.textRenderer.getWidth("...")), StringVisitable.plain("...")});
                return Language.getInstance().reorder(stringVisitable);
            } else {
                return text.asOrderedText();
            }
        }
        public Text getNarration() {
            return Text.translatable("narrator.select", new Object[]{this.displayName});
        }
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            String[] nameArr = this.preset.getName().toLowerCase().split(" ");
            String name = "";
            for(int i = 0; i < nameArr.length; i++){
                name += nameArr[i];
                if(i < nameArr.length - 1){
                    name += "_";
                }
            }
            try(InputStream inputStream = Files.newInputStream(Path.of("./coloristic_presets/", name + ".png", "").toAbsolutePath().normalize())){
                NativeImage image = NativeImage.read(inputStream);
                NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
                String var10000 = Util.replaceInvalidChars(name, Identifier::isPathCharacterValid);
                Identifier identifier = Identifier.ofVanilla("pack/" + var10000 + "/" + String.valueOf(Hashing.sha1().hashUnencodedChars(name)) + "/icon");
                this.client.getTextureManager().registerTexture(identifier, texture);
                context.drawTexture(identifier, x, y, 0, 0, 32, 32, 32, 32);
            }catch(IOException var){
                context.drawTexture(Identifier.of(Coloristic.MOD_ID, "textures/gui/sprites/icon/default_icon.png"), x, y, 0, 0, 32, 32, 32, 32);
            }
            OrderedText orderedText = this.displayName;
            if (this.isSelectable() && ((Boolean)this.client.options.getTouchscreen().getValue() || hovered || this.widget.getSelectedOrNull() == this && this.widget.isFocused())) {
                context.fill(x, y, x + 32, y + 32, -1601138544);
            }
            context.drawTextWithShadow(this.client.textRenderer, orderedText, x + 32 + 2, y + 1, 16777215);
        }
        public String getName() {
            return this.preset.getName();
        }
        private boolean isSelectable() {
            return true;
        }
        public void toggle() {
            if (this.enable()) {
                this.widget.screen.switchFocusedList(this.widget);
            }
        }
        void moveTowardStart() {
        }
        void moveTowardEnd() {
        }
        private boolean enable() {
            GameOptionsContainer options = (GameOptionsContainer) this.client.options;
            options.getRedMatrix1().setValue(this.preset.getOptions().get(0));
            options.getRedMatrix2().setValue(this.preset.getOptions().get(1));
            options.getRedMatrix3().setValue(this.preset.getOptions().get(2));
            options.getGreenMatrix1().setValue(this.preset.getOptions().get(3));
            options.getGreenMatrix2().setValue(this.preset.getOptions().get(4));
            options.getGreenMatrix3().setValue(this.preset.getOptions().get(5));
            options.getBlueMatrix1().setValue(this.preset.getOptions().get(6));
            options.getBlueMatrix2().setValue(this.preset.getOptions().get(7));
            options.getBlueMatrix3().setValue(this.preset.getOptions().get(8));
            options.getColorScale1().setValue(this.preset.getOptions().get(9));
            options.getColorScale2().setValue(this.preset.getOptions().get(10));
            options.getColorScale3().setValue(this.preset.getOptions().get(11));
            options.getSaturation().setValue(this.preset.getOptions().get(12));
            options.getResolution().setValue(this.preset.getOptions().get(13).intValue());
            options.getMosaicSize().setValue(this.preset.getOptions().get(14).intValue());
            options.getInverseAmount().setValue(this.preset.getOptions().get(15));
            options.getRadius().setValue(this.preset.getOptions().get(16));
            return true;
        }
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            double d = mouseX - (double)this.widget.getRowLeft();
            double e = mouseY - (double)this.widget.getRowTop(this.widget.children().indexOf(this));
            if (this.isSelectable() && d <= 32.0) {
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }
}
