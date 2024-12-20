package net.almer.coloristic.client.screen;

import net.almer.coloristic.Coloristic;
import net.almer.coloristic.client.ColoristicFx;
import net.almer.coloristic.client.GameOptionsContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.io.File;
import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public class ColorSettingsScreen extends Screen {
    private static final Text TITLE_TEXT = Text.translatable("options.colorTitle");
    public ColorSettingsScreen() {
        super(TITLE_TEXT);
    }
    @Override
    protected void init() {
        GameOptionsContainer options = (GameOptionsContainer) this.client.options;
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().margin(6, 6, 6, 0);
        GridWidget.Adder adder = gridWidget.createAdder(3);
        adder.add(options.getRedMatrix1().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner().alignLeft());
        adder.add(options.getGreenMatrix1().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner().alignLeft());
        adder.add(options.getBlueMatrix1().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner().alignLeft());
        adder.add(options.getRedMatrix2().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner().alignLeft());
        adder.add(options.getGreenMatrix2().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner().alignLeft());
        adder.add(options.getBlueMatrix2().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner().alignLeft());
        adder.add(options.getRedMatrix3().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner().alignLeft());
        adder.add(options.getGreenMatrix3().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner().alignLeft());
        adder.add(options.getBlueMatrix3().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner().alignLeft());

        adder.add(options.getColorScale1().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner().marginTop(60));
        adder.add(options.getSaturation().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner().marginTop(60));
        adder.add(options.getResolution().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner().marginTop(60));
        adder.add(options.getColorScale2().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner());
        adder.add(options.getMosaicSize().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner());
        adder.add(options.getInverseAmount().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner());
        adder.add(options.getColorScale3().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner());
        adder.add(options.getRadius().createWidget(this.client.options, 0, 0, 90), 1, gridWidget.copyPositioner());
        adder.add(ButtonWidget.builder(Text.translatable("option.setDefault"), (button) -> {
            ColoristicFx.INSTANCE.setToDefault();
        }).width(90).build(), 1, gridWidget.copyPositioner());

        GridWidget gridWidget1 = new GridWidget();
        gridWidget1.getMainPositioner().margin(6, 6, 6, 6);
        GridWidget.Adder adder1 = gridWidget1.createAdder(1);
        adder1.add(ButtonWidget.builder(Text.translatable("option.preset_title"), (button) -> {
            Path path = Path.of("./coloristic_presets/").toAbsolutePath().normalize();
            new File("coloristic_presets").mkdir();
            this.client.setScreen(new PresetScreen(path));
        }).width(100).build(), 1, gridWidget1.copyPositioner().alignTop().alignRight().margin(0, 0, 6, 0));
        adder1.add(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.client.setScreen(new GameMenuScreen(true));
        }).width(100).build(), 1, gridWidget1.copyPositioner().alignBottom().alignRight());
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.0F, 0.0F);
        gridWidget.forEachChild(this::addDrawableChild);
        gridWidget1.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget1, 0, 0, this.width, this.height, 1.0F, 1.0F);
        gridWidget1.forEachChild(this::addDrawableChild);
    }
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.client.world == null) {
            this.renderPanoramaBackground(context, delta);
        }
    }
}
