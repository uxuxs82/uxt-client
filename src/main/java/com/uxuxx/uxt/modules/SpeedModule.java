package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class SpeedModule extends Module {
    
    public SpeedModule() {
        super("Speed");
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (client.player != null && client.player.input.movementForward > 0) {
            client.player.setVelocity(
                client.player.getVelocity().x * 1.8,
                client.player.getVelocity().y,
                client.player.getVelocity().z * 1.8
            );
        }
    }
}
