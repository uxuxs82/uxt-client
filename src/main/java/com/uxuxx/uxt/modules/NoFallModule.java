package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class NoFallModule extends Module {
    
    public NoFallModule() {
        super("NoFall");
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (client.player != null) {
            client.player.fallDistance = 0;
        }
    }
}
