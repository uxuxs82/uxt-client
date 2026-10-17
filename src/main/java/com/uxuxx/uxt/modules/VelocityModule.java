package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class VelocityModule extends Module {

    private Setting percentSetting;

    public VelocityModule() {
        super("Velocity");
        percentSetting = new Setting("Percent", 50.0, 0.0, 100.0, 10.0);
        settings.add(percentSetting);
    }

    public double getMultiplier() {
        return percentSetting.getValue() / 100.0;
    }
}
