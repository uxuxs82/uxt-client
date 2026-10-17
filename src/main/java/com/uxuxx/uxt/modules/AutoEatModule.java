package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class AutoEatModule extends Module {

    private Setting hungerSetting;
    private int prevSlot = -1;
    private int cooldown = 0;

    public AutoEatModule() {
        super("AutoEat");
        hungerSetting = new Setting("Hunger", 15.0, 5.0, 19.0, 1.0);
        settings.add(hungerSetting);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null || client.interactionManager == null) return;

        if (cooldown > 0) {
            cooldown--;
            return;
        }

        if (client.player.isUsingItem()) return;

        HungerManager hunger = client.player.getHungerManager();
        if (hunger.getFoodLevel() >= hungerSetting.getValue()) {
            if (prevSlot != -1) {
                client.player.inventory.selectedSlot = prevSlot;
                prevSlot = -1;
            }
            return;
        }

        int foodSlot = findFoodSlot(client);
        if (foodSlot == -1) return;

        if (prevSlot == -1) {
            prevSlot = client.player.inventory.selectedSlot;
        }

        client.player.inventory.selectedSlot = foodSlot;
        client.interactionManager.interactItem(client.player, client.world, Hand.MAIN_HAND);
        cooldown = 5;
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (prevSlot != -1 && client.player != null) {
            client.player.inventory.selectedSlot = prevSlot;
        }
        prevSlot = -1;
        cooldown = 0;
    }

    private int findFoodSlot(MinecraftClient client) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.inventory.getStack(i);
            FoodComponent food = stack.getItem().getFoodComponent();
            if (food != null) {
                return i;
            }
        }
        return -1;
    }
}
