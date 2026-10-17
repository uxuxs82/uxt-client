package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class KillAuraModule extends Module {
    
    private Setting rangeSetting;
    private int delay = 0;
    
    public KillAuraModule() {
        super("KillAura");
        rangeSetting = new Setting("Range", 4.0, 3.0, 6.0, 0.5);
        settings.add(rangeSetting);
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        
        if (delay > 0) {
            delay--;
            return;
        }
        
        double range = rangeSetting.getValue();
        
        for (Entity entity : client.world.getEntities()) {
            if (entity instanceof LivingEntity && entity != client.player) {
                if (client.player.distanceTo(entity) <= range) {
                    if (entity instanceof PlayerEntity || entity instanceof LivingEntity) {
                        client.interactionManager.attackEntity(client.player, entity);
                        client.player.swingHand(Hand.MAIN_HAND);
                        delay = 10;
                        break;
                    }
                }
            }
        }
    }
}
