package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class TunnelModule extends Module {
    
    private Setting speedSetting;
    
    public TunnelModule() {
        super("Tunnel");
        speedSetting = new Setting("Speed", 1.0, 1.0, 5.0, 1.0);
        settings.add(speedSetting);
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        
        BlockPos playerPos = client.player.getBlockPos();
        BlockPos target1 = playerPos.offset(client.player.getHorizontalFacing());
        BlockPos target2 = target1.up();
        
        if (!client.world.getBlockState(target1).isAir()) {
            client.interactionManager.attackBlock(target1, Direction.UP);
            client.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
        }
        
        if (!client.world.getBlockState(target2).isAir()) {
            client.interactionManager.attackBlock(target2, Direction.UP);
            client.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
        }
    }
}
