package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class NoSlowModule extends Module {
    
    public NoSlowModule() {
        super("NoSlow");
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        
        
        
        if (client.player.isUsingItem()) {
            client.player.input.movementForward *= 5.0f;
            client.player.input.movementSideways *= 5.0f;
        }
    }
}
