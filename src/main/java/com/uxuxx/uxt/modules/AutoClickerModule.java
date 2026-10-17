package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;

public class AutoClickerModule extends Module {
    
    private Setting cpsSetting;
    private int delay = 0;
    
    public AutoClickerModule() {
        super("AutoClicker");
        cpsSetting = new Setting("CPS", 12.0, 5.0, 20.0, 1.0);
        settings.add(cpsSetting);
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        
        if (delay > 0) {
            delay--;
            return;
        }
        
        if (client.options.keyAttack.isPressed()) {
            client.interactionManager.attackEntity(client.player, client.targetedEntity);
            client.player.swingHand(Hand.MAIN_HAND);
            
            int cps = (int) cpsSetting.getValue();
            delay = 20 / cps;
        }
    }
}
