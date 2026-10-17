package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;

public class FullbrightModule extends Module {

    private static final double DEFAULT_GAMMA = 0.5;
    private static final double FULLBRIGHT_GAMMA = 16.0;

    private double oldGamma = DEFAULT_GAMMA;

    public FullbrightModule() {
        super("Fullbright");
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        oldGamma = clampGamma(client.options.gamma);
        client.options.gamma = FULLBRIGHT_GAMMA;
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        client.options.gamma = clampGamma(oldGamma);
    }

    private double clampGamma(double gamma) {
        if (gamma < 0.0 || gamma > 1.0) {
            return DEFAULT_GAMMA;
        }
        return gamma;
    }
}
