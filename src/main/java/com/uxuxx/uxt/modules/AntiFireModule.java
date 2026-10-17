package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class AntiFireModule extends Module {
    
    public AntiFireModule() {
        super("AntiFire");
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        
        if (client.player.isOnFire()) {
            client.player.setFireTicks(0);
        }
    }
}
