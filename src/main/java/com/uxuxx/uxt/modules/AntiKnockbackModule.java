package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;

public class AntiKnockbackModule extends Module {

    private Setting strengthSetting;

    public AntiKnockbackModule() {
        super("AntiKnockback");
        strengthSetting = new Setting("Strength", 50.0, 0.0, 100.0, 10.0);
        settings.add(strengthSetting);
    }

    public double getMultiplier() {
        return 1.0 - (strengthSetting.getValue() / 100.0);
    }
}
