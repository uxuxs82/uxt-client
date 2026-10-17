package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.FishingBobberEntity;

public class AutoFishModule extends Module {
    
    private int waitTicks = 0;
    
    public AutoFishModule() {
        super("AutoFish");
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        
        FishingBobberEntity bobber = client.player.fishHook;
        
        if (bobber != null) {
            waitTicks++;
            
            if (waitTicks > 20 && bobber.getVelocity().length() < 0.01) {
                // Fish caught, reel in
                client.options.keyUse.setPressed(true);
                waitTicks = 0;
            }
        } else {
            // Cast
            if (waitTicks > 10) {
                client.options.keyUse.setPressed(true);
                waitTicks = 0;
            } else {
                client.options.keyUse.setPressed(false);
                waitTicks++;
            }
        }
    }
}
