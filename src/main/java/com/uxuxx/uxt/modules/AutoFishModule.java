package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.util.Hand;

public class AutoFishModule extends Module {

    private int cooldown = 0;
    private int waitTicks = 0;
    private double lastBobberY = Double.NaN;

    public AutoFishModule() {
        super("AutoFish");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null || client.interactionManager == null) return;

        if (cooldown > 0) {
            cooldown--;
        }

        int rodSlot = findRodSlot(client);
        if (rodSlot == -1) return;

        if (client.player.inventory.selectedSlot != rodSlot) {
            client.player.inventory.selectedSlot = rodSlot;
        }

        FishingBobberEntity bobber = client.player.fishHook;

        if (bobber == null) {
            lastBobberY = Double.NaN;
            waitTicks = 0;

            if (cooldown == 0) {
                client.interactionManager.interactItem(client.player, client.world, Hand.MAIN_HAND);
                cooldown = 15;
            }
            return;
        }

        waitTicks++;
        double y = bobber.getY();

        boolean bite = waitTicks > 20
            && bobber.isTouchingWater()
            && !Double.isNaN(lastBobberY)
            && y < lastBobberY - 0.08;

        lastBobberY = y;

        if (bite && cooldown == 0) {
            client.interactionManager.interactItem(client.player, client.world, Hand.MAIN_HAND);
            cooldown = 15;
            waitTicks = 0;
        }
    }

    @Override
    public void onDisable() {
        cooldown = 0;
        waitTicks = 0;
        lastBobberY = Double.NaN;
    }

    private int findRodSlot(MinecraftClient client) {
        for (int i = 0; i < 9; i++) {
            if (client.player.inventory.getStack(i).getItem() instanceof FishingRodItem) {
                return i;
            }
        }
        return -1;
    }
}
