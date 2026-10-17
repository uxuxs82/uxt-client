package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class KillAuraModule extends Module {
    
    public KillAuraModule() {
        super("KillAura");
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        
        for (Entity entity : client.world.getEntities()) {
            if (entity instanceof LivingEntity && entity != client.player) {
                if (entity instanceof PlayerEntity || entity.isAlive()) {
                    double distance = client.player.distanceTo(entity);
                    if (distance <= 4.5) {
                        client.interactionManager.attackEntity(client.player, entity);
                        client.player.swingHand(Hand.MAIN_HAND);
                        break;
                    }
                }
            }
        }
    }
}
