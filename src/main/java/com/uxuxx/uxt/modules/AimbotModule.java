package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class AimbotModule extends Module {
    
    private Setting rangeSetting;
    private Setting fovSetting;
    
    public AimbotModule() {
        super("Aimbot");
        rangeSetting = new Setting("Range", 50.0, 20.0, 100.0, 10.0);
        fovSetting = new Setting("FOV", 90.0, 30.0, 180.0, 10.0);
        settings.add(rangeSetting);
        settings.add(fovSetting);
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        
        Entity target = findClosestEntity(client);
        if (target != null) {
            Vec3d targetPos = target.getPos().add(0, target.getHeight() * 0.9, 0);
            Vec3d eyePos = client.player.getCameraPosVec(1.0f);
            Vec3d direction = targetPos.subtract(eyePos).normalize();
            
            float yaw = (float) Math.toDegrees(Math.atan2(direction.z, direction.x)) - 90;
            float pitch = (float) -Math.toDegrees(Math.asin(direction.y));
            
            client.player.yaw = yaw;
            client.player.pitch = pitch;
        }
    }
    
    private Entity findClosestEntity(MinecraftClient client) {
        Entity closest = null;
        double closestDist = rangeSetting.getValue();
        
        for (Entity entity : client.world.getEntities()) {
            if (entity == client.player) continue;
            if (!(entity instanceof LivingEntity)) continue;
            
            double dist = client.player.distanceTo(entity);
            if (dist > rangeSetting.getValue()) continue;
            
            Vec3d toEntity = entity.getPos().subtract(client.player.getPos()).normalize();
            Vec3d lookVec = client.player.getRotationVec(1.0f);
            double angle = Math.toDegrees(Math.acos(lookVec.dotProduct(toEntity)));
            
            if (angle > fovSetting.getValue() / 2) continue;
            
            if (dist < closestDist) {
                closestDist = dist;
                closest = entity;
            }
        }
        
        return closest;
    }
}
