package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class FreecamModule extends Module {
    
    public FreecamModule() {
        super("Freecam");
    }
    
    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.noClip = true;
            client.player.abilities.flying = true;
            client.player.abilities.allowFlying = true;
            client.player.setVelocity(0, 0, 0);
        }
    }
    
    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.noClip = false;
            client.player.abilities.flying = false;
            client.player.abilities.allowFlying = false;
        }
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        
        client.player.noClip = true;
        client.player.abilities.flying = true;
    }
}
