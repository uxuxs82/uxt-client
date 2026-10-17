package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class ESPModule extends Module {

    public static boolean enabled = false;
    public static double range = 48.0;

    private Setting rangeSetting;

    public ESPModule() {
        super("ESP");
        rangeSetting = new Setting("Range", 48.0, 16.0, 96.0, 8.0);
        settings.add(rangeSetting);
    }

    @Override
    public void onEnable() {
        enabled = true;
    }

    @Override
    public void onDisable() {
        enabled = false;
    }

    @Override
    public void onTick(MinecraftClient client) {
        range = rangeSetting.getValue();
    }
}
