package net.almer.coloristic.mixin;

import com.mojang.serialization.Codec;
import net.almer.coloristic.client.ColoristicFx;
import net.almer.coloristic.client.GameOptionsContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.Locale;

import static net.minecraft.client.option.GameOptions.getGenericValueText;

@Mixin(GameOptions.class)
@Environment(EnvType.CLIENT)
public class GameOptionsMixin implements GameOptionsContainer {
    @Final
    private SimpleOption<Double> redMatrix1;
    @Final
    private SimpleOption<Double> redMatrix2;
    @Final
    private SimpleOption<Double> redMatrix3;
    @Final
    private SimpleOption<Double> greenMatrix1;
    @Final
    private SimpleOption<Double> greenMatrix2;
    @Final
    private SimpleOption<Double> greenMatrix3;
    @Final
    private SimpleOption<Double> blueMatrix1;
    @Final
    private SimpleOption<Double> blueMatrix2;
    @Final
    private SimpleOption<Double> blueMatrix3;
    @Final
    private SimpleOption<Double> colorScale1;
    @Final
    private SimpleOption<Double> colorScale2;
    @Final
    private SimpleOption<Double> colorScale3;
    @Final
    private SimpleOption<Double> saturation;
    @Final
    private SimpleOption<Integer> resolution;
    @Final
    private SimpleOption<Integer> mosaicSize;
    @Final
    private SimpleOption<Double> inverseAmount;
    @Final
    private SimpleOption<Double> radius;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(MinecraftClient client, File optionsFile, CallbackInfo ci){
        this.redMatrix1 = new SimpleOption("options.redMatrix1", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(String.format(Locale.ROOT, "%.2f", (Double)value)));
        }, SimpleOption.DoubleSliderCallbacks.INSTANCE, 1.0, (value) -> {
            ColoristicFx.INSTANCE.redMatrix1 = ((Double)value).floatValue();
        });
        this.redMatrix2 = new SimpleOption("options.redMatrix2", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(String.format(Locale.ROOT, "%.2f", (Double)value)));
        }, SimpleOption.DoubleSliderCallbacks.INSTANCE, 0.0, (value) -> {
            ColoristicFx.INSTANCE.redMatrix2 = ((Double)value).floatValue();
        });
        this.redMatrix3 = new SimpleOption("options.redMatrix3", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(String.format(Locale.ROOT, "%.2f", (Double)value)));
        }, SimpleOption.DoubleSliderCallbacks.INSTANCE, 0.0, (value) -> {
            ColoristicFx.INSTANCE.redMatrix3 = ((Double)value).floatValue();
        });
        this.greenMatrix1 = new SimpleOption("options.greenMatrix1", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(String.format(Locale.ROOT, "%.2f", (Double)value)));
        }, SimpleOption.DoubleSliderCallbacks.INSTANCE, 0.0, (value) -> {
            ColoristicFx.INSTANCE.greenMatrix1 = ((Double)value).floatValue();
        });
        this.greenMatrix2 = new SimpleOption("options.greenMatrix2", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(String.format(Locale.ROOT, "%.2f", (Double)value)));
        }, SimpleOption.DoubleSliderCallbacks.INSTANCE, 1.0, (value) -> {
            ColoristicFx.INSTANCE.greenMatrix2 = ((Double)value).floatValue();
        });
        this.greenMatrix3 = new SimpleOption("options.greenMatrix3", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(String.format(Locale.ROOT, "%.2f", (Double)value)));
        }, SimpleOption.DoubleSliderCallbacks.INSTANCE, 0.0, (value) -> {
            ColoristicFx.INSTANCE.greenMatrix3 = ((Double)value).floatValue();
        });
        this.blueMatrix1 = new SimpleOption("options.blueMatrix1", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(String.format(Locale.ROOT, "%.2f", (Double)value)));
        }, SimpleOption.DoubleSliderCallbacks.INSTANCE, 0.0, (value) -> {
            ColoristicFx.INSTANCE.blueMatrix1 = ((Double)value).floatValue();
        });
        this.blueMatrix2 = new SimpleOption("options.blueMatrix2", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(String.format(Locale.ROOT, "%.2f", (Double)value)));
        }, SimpleOption.DoubleSliderCallbacks.INSTANCE, 0.0, (value) -> {
            ColoristicFx.INSTANCE.blueMatrix2 = ((Double)value).floatValue();
        });
        this.blueMatrix3 = new SimpleOption("options.blueMatrix3", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(String.format(Locale.ROOT, "%.2f", (Double)value)));
        }, SimpleOption.DoubleSliderCallbacks.INSTANCE, 1.0, (value) -> {
            ColoristicFx.INSTANCE.blueMatrix3 = ((Double)value).floatValue();
        });
        this.colorScale1 = new SimpleOption("options.colorScale1", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(String.format(Locale.ROOT, "%.2f", (Double)value)));
        }, (new SimpleOption.ValidatingIntSliderCallbacks(0, 50)).withModifier((sliderProgressValue) -> {
            return (double)sliderProgressValue / 10.0;
        }, (value) -> {
            return (int)(value * 10.0);
        }), Codec.doubleRange(0.0, 5.0), 1.0, (value) -> {
            ColoristicFx.INSTANCE.colorScale1 = ((Double)value).floatValue();
        });
        this.colorScale2 = new SimpleOption("options.colorScale2", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(String.format(Locale.ROOT, "%.2f", (Double)value)));
        }, (new SimpleOption.ValidatingIntSliderCallbacks(0, 50)).withModifier((sliderProgressValue) -> {
            return (double)sliderProgressValue / 10.0;
        }, (value) -> {
            return (int)(value * 10.0);
        }), Codec.doubleRange(0.0, 5.0), 1.0, (value) -> {
            ColoristicFx.INSTANCE.colorScale2 = ((Double)value).floatValue();
        });
        this.colorScale3 = new SimpleOption("options.colorScale3", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(String.format(Locale.ROOT, "%.2f", (Double)value)));
        }, (new SimpleOption.ValidatingIntSliderCallbacks(0, 50)).withModifier((sliderProgressValue) -> {
            return (double)sliderProgressValue / 10.0;
        }, (value) -> {
            return (int)(value * 10.0);
        }), Codec.doubleRange(0.0, 5.0), 1.0, (value) -> {
            ColoristicFx.INSTANCE.colorScale3 = ((Double)value).floatValue();
        });
        this.saturation = new SimpleOption("options.saturation", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(String.format(Locale.ROOT, "%.2f", (Double)value)));
        }, (new SimpleOption.ValidatingIntSliderCallbacks(0, 30)).withModifier((sliderProgressValue) -> {
            return (double)sliderProgressValue / 10.0;
        }, (value) -> {
            return (int)(value * 10.0);
        }), Codec.doubleRange(0.0, 3.0), 1.0, (value) -> {
            ColoristicFx.INSTANCE.saturation = ((Double)value).floatValue();
        });
        this.resolution = new SimpleOption("options.resolution", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(((Integer)value).toString()));
        }, (new SimpleOption.ValidatingIntSliderCallbacks(1, 100)), Codec.intRange(1, 100), 100, (value) -> {
            ColoristicFx.INSTANCE.resolution = ((Integer)value).floatValue();
        });
        this.mosaicSize = new SimpleOption("options.mosaicSize", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(((Integer)value).toString()));
        }, (new SimpleOption.ValidatingIntSliderCallbacks(1, 10)), Codec.intRange(1, 10), 1, (value) -> {
            ColoristicFx.INSTANCE.mosaicSize = ((Integer)value).floatValue();
        });
        this.inverseAmount = new SimpleOption("options.inverseAmount", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(String.format(Locale.ROOT, "%.0f", ((Double)value * 100)) + "%"));
        }, SimpleOption.DoubleSliderCallbacks.INSTANCE, 0.0, (value) -> {
            ColoristicFx.INSTANCE.inverseAmount = ((Double)value).floatValue();
        });
        this.radius = new SimpleOption("options.radius", SimpleOption.emptyTooltip(), (optionText, value) -> {
            return getGenericValueText(optionText, Text.literal(String.format(Locale.ROOT, "%.2f", (Double)value)));
        }, (new SimpleOption.ValidatingIntSliderCallbacks(0, 100)).withModifier((sliderProgressValue) -> {
            return (double)sliderProgressValue / 10.0;
        }, (value) -> {
            return (int)(value * 10.0);
        }), Codec.doubleRange(0.0, 10.0), 0.0, (value) -> {
            ColoristicFx.INSTANCE.radius = ((Double)value).floatValue();
        });
    }
    @Override
    public SimpleOption<Double> getRedMatrix1() {
        return this.redMatrix1;
    }
    @Override
    public SimpleOption<Double> getRedMatrix2() {
        return this.redMatrix2;
    }
    @Override
    public SimpleOption<Double> getRedMatrix3() {
        return this.redMatrix3;
    }
    @Override
    public SimpleOption<Double> getGreenMatrix1() {
        return this.greenMatrix1;
    }
    @Override
    public SimpleOption<Double> getGreenMatrix2() {
        return this.greenMatrix2;
    }
    @Override
    public SimpleOption<Double> getGreenMatrix3() {return this.greenMatrix3;}
    @Override
    public SimpleOption<Double> getBlueMatrix1() {
        return this.blueMatrix1;
    }
    @Override
    public SimpleOption<Double> getBlueMatrix2() {
        return this.blueMatrix2;
    }
    @Override
    public SimpleOption<Double> getBlueMatrix3() {
        return this.blueMatrix3;
    }
    @Override
    public SimpleOption<Double> getColorScale1() {
        return this.colorScale1;
    }
    @Override
    public SimpleOption<Double> getColorScale2() {
        return this.colorScale2;
    }
    @Override
    public SimpleOption<Double> getColorScale3() {
        return this.colorScale3;
    }
    @Override
    public SimpleOption<Double> getSaturation() {
        return this.saturation;
    }
    @Override
    public SimpleOption<Integer> getResolution() {
        return this.resolution;
    }
    @Override
    public SimpleOption<Integer> getMosaicSize() {
        return this.mosaicSize;
    }
    @Override
    public SimpleOption<Double> getInverseAmount() {
        return this.inverseAmount;
    }
    @Override
    public SimpleOption<Double> getRadius() {
        return this.radius;
    }
}
