package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class JesusModule extends Module {
    
    public JesusModule() {
        super("Jesus");
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        
        // Walk on water
        BlockPos below = client.player.getBlockPos().down();
        if (client.world.getBlockState(below).getFluidState().isStill() || 
            client.world.getBlockState(below).getFluidState().isEmpty() == false) {
            
            if (client.player.isTouchingWater() && !client.player.isSneaking()) {
                Vec3d velocity = client.player.getVelocity();
                client.player.setVelocity(velocity.x, 0.1, velocity.z);
                client.player.fallDistance = 0;
            }
        }
    }
}
