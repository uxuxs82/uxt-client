package com.uxuxx.uxt;

import com.uxuxx.uxt.modules.*;
import net.minecraft.client.MinecraftClient;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    
    private List<Module> modules = new ArrayList<>();
    
    public ModuleManager() {
        modules.add(new SpeedModule());
        modules.add(new SprintModule());
        modules.add(new NoFallModule());
        modules.add(new KillAuraModule());
        modules.add(new AutoClickerModule());
        modules.add(new VelocityModule());
        modules.add(new XrayModule());
        modules.add(new FullbrightModule());
    }
    
    public List<Module> getModules() {
        return modules;
    }
    
    public void onTick(MinecraftClient client) {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onTick(client);
            }
        }
    }
}
