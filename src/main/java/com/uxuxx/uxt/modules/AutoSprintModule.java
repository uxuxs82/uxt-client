package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class AutoSprintModule extends Module {

    public AutoSprintModule() {
        super("AutoSprint");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;

        if (client.player.input.movementForward != 0 || client.player.input.movementSideways != 0) {
            client.player.setSprinting(true);
        }
    }
}
