package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class FreecamModule extends Module {

    private boolean prevAllowFlying = false;
    private boolean prevFlying = false;

    public FreecamModule() {
        super("Freecam");
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        prevAllowFlying = client.player.abilities.allowFlying;
        prevFlying = client.player.abilities.flying;

        client.player.noClip = true;
        client.player.abilities.flying = true;
        client.player.abilities.allowFlying = true;
        client.player.setVelocity(0, 0, 0);
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        client.player.noClip = false;
        client.player.abilities.flying = prevFlying;
        client.player.abilities.allowFlying = prevAllowFlying;
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;

        client.player.noClip = true;
        client.player.abilities.flying = true;
    }
}
