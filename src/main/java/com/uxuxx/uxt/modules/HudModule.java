package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;

public class HudModule extends Module {

    public static boolean enabled = false;

    public HudModule() {
        super("HUD");
    }

    @Override
    public void onEnable() {
        enabled = true;
    }

    @Override
    public void onDisable() {
        enabled = false;
    }
}
