package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class FullbrightModule extends Module {
    
    private double oldGamma;
    
    public FullbrightModule() {
        super("Fullbright");
    }
    
    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        oldGamma = client.options.gamma;
        client.options.gamma = 16.0;
    }
    
    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        client.options.gamma = oldGamma;
    }
}
