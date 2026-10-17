package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Vec3d;

public class ItemESPModule extends Module {
    
    private Setting rangeSetting;
    
    public ItemESPModule() {
        super("ItemESP");
        rangeSetting = new Setting("Range", 32.0, 16.0, 64.0, 8.0);
        settings.add(rangeSetting);
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        
        
        for (Entity entity : client.world.getEntities()) {
            if (entity instanceof ItemEntity) {
                double dist = client.player.distanceTo(entity);
                if (dist <= rangeSetting.getValue()) {
                    entity.setGlowing(true);
                }
            }
        }
    }
    
    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null) {
            for (Entity entity : client.world.getEntities()) {
                if (entity instanceof ItemEntity) {
                    entity.setGlowing(false);
                }
            }
        }
    }
}
