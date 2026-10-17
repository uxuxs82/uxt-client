package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class BlinkModule extends Module {

    public static boolean enabled = false;

    public BlinkModule() {
        super("Blink");
    }

    @Override
    public void onEnable() {
        enabled = true;
    }

    @Override
    public void onDisable() {
        enabled = false;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.getNetworkHandler() != null) {
            client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Both(
                client.player.getX(), client.player.getY(), client.player.getZ(),
                client.player.yaw, client.player.pitch, client.player.isOnGround()
            ));
        }
    }
}
