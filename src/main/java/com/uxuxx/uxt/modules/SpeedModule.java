package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class SpeedModule extends Module {
    
    private Setting speedSetting;
    
    public SpeedModule() {
        super("Speed");
        speedSetting = new Setting("Speed", 2.0, 1.0, 5.0, 0.1);
        settings.add(speedSetting);
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        
        double speed = speedSetting.getValue();
        if (client.player.input.movementForward != 0 || client.player.input.movementSideways != 0) {
            client.player.getVelocity().multiply(speed, 1.0, speed);
        }
    }
}
