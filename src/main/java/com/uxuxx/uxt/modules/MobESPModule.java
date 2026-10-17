package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;

public class MobESPModule extends Module {
    
    private Setting rangeSetting;
    
    public MobESPModule() {
        super("MobESP");
        rangeSetting = new Setting("Range", 64.0, 32.0, 128.0, 16.0);
        settings.add(rangeSetting);
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        
        for (Entity entity : client.world.getEntities()) {
            if (entity instanceof HostileEntity) {
                double dist = client.player.distanceTo(entity);
                if (dist <= rangeSetting.getValue()) {
                    entity.setGlowing(true);
                } else {
                    entity.setGlowing(false);
                }
            }
        }
    }
    
    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null) {
            for (Entity entity : client.world.getEntities()) {
                if (entity instanceof HostileEntity) {
                    entity.setGlowing(false);
                }
            }
        }
    }
}
