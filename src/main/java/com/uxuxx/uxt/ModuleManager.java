package com.uxuxx.uxt;

import com.uxuxx.uxt.modules.*;
import net.minecraft.client.MinecraftClient;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        modules.add(new KillAuraModule());
        modules.add(new AimbotModule());
        modules.add(new VelocityModule());
        modules.add(new AntiKnockbackModule());

        modules.add(new SpeedModule());
        modules.add(new SprintModule());
        modules.add(new AutoSprintModule());
        modules.add(new NoFallModule());
        modules.add(new NoSlowModule());
        modules.add(new StepModule());
        modules.add(new JesusModule());
        modules.add(new BlinkModule());
        modules.add(new FreecamModule());

        modules.add(new AutoEatModule());
        modules.add(new AutoArmorModule());
        modules.add(new AntiFireModule());
        modules.add(new AntiAFKModule());

        modules.add(new ScaffoldModule());
        modules.add(new TunnelModule());
        modules.add(new FastPlaceModule());
        modules.add(new AutoFishModule());

        modules.add(new XrayModule());
        modules.add(new FullbrightModule());
        modules.add(new ESPModule());
        modules.add(new ChestESPModule());
        modules.add(new ItemESPModule());
        modules.add(new MobESPModule());

        modules.add(new TimerModule());

        modules.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
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
