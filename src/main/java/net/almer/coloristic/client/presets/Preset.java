package net.almer.coloristic.client.presets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public class Preset {
    public final String name;
    public final List<Double> options;
    public static final Codec<Preset> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(Preset::getName),
            Codec.DOUBLE.listOf().fieldOf("options").forGetter(Preset::getOptions)
    ).apply(instance, Preset::new));
    public Preset(String name, List<Double> options){
        this.name = name;
        this.options = options;
    }
    public List<Double> getOptions(){
        return this.options;
    }
    public String getName(){
        return this.name;
    }
}
