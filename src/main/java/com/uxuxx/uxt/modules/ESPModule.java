package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class ESPModule extends Module {
    
    public ESPModule() {
        super("ESP");
    }
    
    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null) {
            client.worldRenderer.reload();
        }
    }
    
    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null) {
            client.worldRenderer.reload();
        }
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        // ESP rendering handled by mixin
    }
}
