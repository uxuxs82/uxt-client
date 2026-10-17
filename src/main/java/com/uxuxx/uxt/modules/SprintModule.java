package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class SprintModule extends Module {
    
    public SprintModule() {
        super("Sprint");
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        
        if (client.player.input.movementForward > 0 && !client.player.isSneaking()) {
            client.player.setSprinting(true);
        }
    }
}
