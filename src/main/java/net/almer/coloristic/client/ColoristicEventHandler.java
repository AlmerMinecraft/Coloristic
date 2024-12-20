package net.almer.coloristic.client;

import net.almer.coloristic.Coloristic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.ladysnake.satin.api.event.PostWorldRenderCallback;
import org.ladysnake.satin.api.event.PostWorldRenderCallbackV2;
import org.ladysnake.satin.api.event.ShaderEffectRenderCallback;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.ladysnake.satin.api.managed.ShaderEffectManager;

@Environment(EnvType.CLIENT)
public class ColoristicEventHandler {
    @Environment(EnvType.CLIENT)
    public static void registerClientEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(ColoristicFx.INSTANCE);
        ShaderEffectRenderCallback.EVENT.register(ColoristicFx.INSTANCE);
        PostWorldRenderCallback.EVENT.register(ColoristicFx.INSTANCE);
        ClientEntityEvents.ENTITY_LOAD.register(ColoristicEventHandler::onClientLoad);
    }
    @Environment(EnvType.CLIENT)
    private static void onClientLoad(Entity entity, World world){
        ColoristicFx.INSTANCE.COLOR_SHADER.release();
    }
}
