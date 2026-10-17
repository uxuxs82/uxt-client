package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class StepModule extends Module {
    
    private Setting heightSetting;
    
    public StepModule() {
        super("Step");
        heightSetting = new Setting("Height", 1.0, 0.5, 2.0, 0.5);
        settings.add(heightSetting);
    }
    
    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.stepHeight = (float) heightSetting.getValue();
        }
    }
    
    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.stepHeight = 0.6f;
        }
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        client.player.stepHeight = (float) heightSetting.getValue();
    }
}
