package net.almer.coloristic;

import net.almer.coloristic.client.ColoristicEventHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ColoristicClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ColoristicEventHandler.registerClientEvents();
    }
}
