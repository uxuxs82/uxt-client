package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

import java.lang.reflect.Field;

public class FastPlaceModule extends Module {
    
    public FastPlaceModule() {
        super("FastPlace");
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.interactionManager == null) return;
        
        try {
            Field field = client.interactionManager.getClass().getDeclaredField("blockBreakingCooldown");
            field.setAccessible(true);
            field.setInt(client.interactionManager, 0);
        } catch (Exception e) {
            // Ignore
        }
    }
}
