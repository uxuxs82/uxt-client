package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class AntiAFKModule extends Module {
    
    private int tickCounter = 0;
    
    public AntiAFKModule() {
        super("AntiAFK");
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        
        tickCounter++;
        if (tickCounter >= 200) {
            float random = (float) (Math.random() * 10 - 5);
            client.player.yaw += random;
            tickCounter = 0;
        }
    }
}
