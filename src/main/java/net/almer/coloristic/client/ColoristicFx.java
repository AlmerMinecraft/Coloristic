package net.almer.coloristic.client;

import net.almer.coloristic.Coloristic;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.Identifier;
import org.ladysnake.satin.api.event.PostWorldRenderCallback;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;
import org.ladysnake.satin.api.managed.uniform.Uniform1f;
import org.ladysnake.satin.api.managed.uniform.Uniform3f;

import java.util.Dictionary;

public class ColoristicFx implements PostWorldRenderCallback, ShaderEffectRenderCallback, ClientTickEvents.EndTick{
    public static final ManagedShaderEffect COLOR_SHADER = ShaderEffectManager.getInstance()
            .manage(Identifier.of(Coloristic.MOD_ID, "shaders/post/coloristic.json"));
    public static final ColoristicFx INSTANCE = new ColoristicFx();
    private final Uniform3f uniformRedMatrix = COLOR_SHADER.findUniform3f("RedMatrix");
    private final Uniform3f uniformGreenMatrix = COLOR_SHADER.findUniform3f("GreenMatrix");
    private final Uniform3f uniformBlueMatrix = COLOR_SHADER.findUniform3f("BlueMatrix");
    private final Uniform3f uniformColorScale = COLOR_SHADER.findUniform3f("ColorScale");
    private final Uniform1f uniformSaturation = COLOR_SHADER.findUniform1f("Saturation");
    private final Uniform1f uniformResolution = COLOR_SHADER.findUniform1f("Resolution");
    private final Uniform1f uniformMosaicSize = COLOR_SHADER.findUniform1f("MosaicSize");
    private final Uniform1f uniformInverseAmount = COLOR_SHADER.findUniform1f("InverseAmount");
    private final Uniform1f uniformRadius = COLOR_SHADER.findUniform1f("Radius");

    public float redMatrix1 = 1.0f;
    public float redMatrix2 = 0.0f;
    public float redMatrix3 = 0.0f;
    public float greenMatrix1 = 0.0f;
    public float greenMatrix2 = 1.0f;
    public float greenMatrix3 = 0.0f;
    public float blueMatrix1 = 0.0f;
    public float blueMatrix2 = 0.0f;
    public float blueMatrix3 = 1.0f;
    public float colorScale1 = 1.0f;
    public float colorScale2 = 1.0f;
    public float colorScale3 = 1.0f;
    public float saturation = 1.0f;
    public float resolution = 100.0f;
    public float mosaicSize = 1.0f;
    public float inverseAmount = 0.0f;
    public float radius = 0.0f;

    private int ticks;
    private MinecraftClient client = MinecraftClient.getInstance();
    @Override
    public void onEndTick(MinecraftClient client) {
        if (!client.isPaused()) {
            ticks++;
        }
    }
    @Override
    public void onWorldRendered(Camera camera, float tickDelta) {
        uniformRedMatrix.set(redMatrix1, redMatrix2, redMatrix3);
        uniformGreenMatrix.set(greenMatrix1, greenMatrix2, greenMatrix3);
        uniformBlueMatrix.set(blueMatrix1, blueMatrix2, blueMatrix3);
        uniformColorScale.set(colorScale1, colorScale2, colorScale3);
        uniformSaturation.set(saturation);
        uniformResolution.set(resolution);
        uniformMosaicSize.set(mosaicSize);
        uniformInverseAmount.set(inverseAmount);
        uniformRadius.set(radius);
    }
    public void setToDefault(){
        GameOptionsContainer options = (GameOptionsContainer) this.client.options;
        options.getRedMatrix1().setValue(1.0);
        options.getRedMatrix2().setValue(0.0);
        options.getRedMatrix3().setValue(0.0);
        options.getGreenMatrix1().setValue(0.0);
        options.getGreenMatrix2().setValue(1.0);
        options.getGreenMatrix3().setValue(0.0);
        options.getBlueMatrix1().setValue(0.0);
        options.getBlueMatrix2().setValue(0.0);
        options.getBlueMatrix3().setValue(1.0);
        options.getColorScale1().setValue(1.0);
        options.getColorScale2().setValue(1.0);
        options.getColorScale3().setValue(1.0);
        options.getSaturation().setValue(1.0);
        options.getResolution().setValue(100);
        options.getMosaicSize().setValue(1);
        options.getInverseAmount().setValue(0.0);
        options.getRadius().setValue(0.0);
    }
    @Override
    public void renderShaderEffects(float tickDelta) {
        COLOR_SHADER.render(tickDelta);
    }
}
