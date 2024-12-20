package net.almer.coloristic.mixin;

import net.almer.coloristic.Coloristic;
import net.almer.coloristic.client.screen.ColorSettingsScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

@Mixin(GameMenuScreen.class)
@Environment(EnvType.CLIENT)
public class GameMenuScreenMixin extends Screen{
    protected GameMenuScreenMixin(Text title) {
        super(title);
    }
    @Inject(method = "initWidgets()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget;refreshPositions()V"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void initWidgets(CallbackInfo ci, GridWidget gridWidget, GridWidget.Adder adder){
        adder.add(TextIconButtonWidget.builder(Text.literal("O"), (button) -> {
            this.client.setScreen(new ColorSettingsScreen());
        }, true).width(20).texture(Identifier.of(Coloristic.MOD_ID, "icon/color"), 15, 15).build(), 2, gridWidget.copyPositioner().marginTop(-20).marginLeft(-20));
    }
}
