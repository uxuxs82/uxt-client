package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class FlyModule extends Module {
    
    public FlyModule() {
        super("Fly");
    }
    
    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.abilities.flying = true;
            client.player.sendAbilitiesUpdate();
        }
    }
    
    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && !client.player.isCreative()) {
            client.player.abilities.flying = false;
            client.player.sendAbilitiesUpdate();
        }
    }
}
