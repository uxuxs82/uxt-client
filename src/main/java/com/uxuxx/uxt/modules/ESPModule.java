package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class ESPModule extends Module {

    public static boolean enabled = false;
    public static double range = 48.0;
    public static double colorR = 255.0;
    public static double colorG = 51.0;
    public static double colorB = 51.0;

    private Setting rangeSetting;
    private Setting redSetting;
    private Setting greenSetting;
    private Setting blueSetting;

    public ESPModule() {
        super("ESP");
        rangeSetting = new Setting("Range", 48.0, 16.0, 128.0, 8.0);
        redSetting = new Setting("Red", 255.0, 0.0, 255.0, 15.0);
        greenSetting = new Setting("Green", 51.0, 0.0, 255.0, 15.0);
        blueSetting = new Setting("Blue", 51.0, 0.0, 255.0, 15.0);
        settings.add(rangeSetting);
        settings.add(redSetting);
        settings.add(greenSetting);
        settings.add(blueSetting);
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
        colorR = redSetting.getValue();
        colorG = greenSetting.getValue();
        colorB = blueSetting.getValue();
    }
}
