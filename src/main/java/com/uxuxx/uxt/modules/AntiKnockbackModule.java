package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class AntiKnockbackModule extends Module {
    
    private Setting strengthSetting;
    
    public AntiKnockbackModule() {
        super("AntiKnockback");
        strengthSetting = new Setting("Strength", 50.0, 0.0, 100.0, 10.0);
        settings.add(strengthSetting);
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        
        double multiplier = 1.0 - (strengthSetting.getValue() / 100.0);
        Vec3d velocity = client.player.getVelocity();
        client.player.setVelocity(velocity.x * multiplier, velocity.y, velocity.z * multiplier);
    }
}
