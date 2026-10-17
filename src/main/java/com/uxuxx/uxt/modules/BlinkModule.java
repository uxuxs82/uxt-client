package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class BlinkModule extends Module {
    
    public BlinkModule() {
        super("Blink");
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        
        
        
        if (client.getNetworkHandler() != null) {
            client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionOnly(
                client.player.getX(), 
                client.player.getY(), 
                client.player.getZ(), 
                client.player.isOnGround()
            ));
        }
    }
}
