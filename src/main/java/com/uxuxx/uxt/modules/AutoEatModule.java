package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class AutoEatModule extends Module {
    
    private Setting hungerSetting;
    
    public AutoEatModule() {
        super("AutoEat");
        hungerSetting = new Setting("Hunger", 15.0, 5.0, 19.0, 1.0);
        settings.add(hungerSetting);
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        
        HungerManager hunger = client.player.getHungerManager();
        if (hunger.getFoodLevel() < hungerSetting.getValue()) {
            int foodSlot = findFoodSlot(client);
            if (foodSlot != -1) {
                int prevSlot = client.player.inventory.selectedSlot;
                client.player.inventory.selectedSlot = foodSlot;
                client.options.keyUse.setPressed(true);
                client.player.inventory.selectedSlot = prevSlot;
            }
        } else {
            client.options.keyUse.setPressed(false);
        }
    }
    
    private int findFoodSlot(MinecraftClient client) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.inventory.getStack(i);
            Item item = stack.getItem();
            FoodComponent food = item.getFoodComponent();
            if (food != null) {
                return i;
            }
        }
        return -1;
    }
}
