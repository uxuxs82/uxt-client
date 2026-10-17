package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class TimerModule extends Module {
    
    public static float timerSpeed = 1.0f;
    private Setting speedSetting;
    
    public TimerModule() {
        super("Timer");
        speedSetting = new Setting("Speed", 2.0, 0.5, 5.0, 0.5);
        settings.add(speedSetting);
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (isEnabled()) {
            timerSpeed = (float) speedSetting.getValue();
        } else {
            timerSpeed = 1.0f;
        }
    }
    
    @Override
    public void onDisable() {
        timerSpeed = 1.0f;
    }
}
