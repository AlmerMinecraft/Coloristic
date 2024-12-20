package net.almer.coloristic.client;

import net.minecraft.client.option.SimpleOption;

public interface GameOptionsContainer {
    public SimpleOption<Double> getRedMatrix1();
    public SimpleOption<Double> getRedMatrix2();
    public SimpleOption<Double> getRedMatrix3();
    public SimpleOption<Double> getGreenMatrix1();
    public SimpleOption<Double> getGreenMatrix2();
    public SimpleOption<Double> getGreenMatrix3();
    public SimpleOption<Double> getBlueMatrix1();
    public SimpleOption<Double> getBlueMatrix2();
    public SimpleOption<Double> getBlueMatrix3();
    public SimpleOption<Double> getColorScale1();
    public SimpleOption<Double> getColorScale2();
    public SimpleOption<Double> getColorScale3();
    public SimpleOption<Double> getSaturation();
    public SimpleOption<Integer> getResolution();
    public SimpleOption<Integer> getMosaicSize();
    public SimpleOption<Double> getInverseAmount();
    public SimpleOption<Double> getRadius();
}
