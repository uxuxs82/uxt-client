package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class AutoArmorModule extends Module {

    private static final EquipmentSlot[] ARMOR_SLOTS = {
        EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    private int tickCounter = 0;

    public AutoArmorModule() {
        super("AutoArmor");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.interactionManager == null) return;

        tickCounter++;
        if (tickCounter < 20) return;
        tickCounter = 0;

        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack current = client.player.getEquippedStack(slot);
            int currentProtection = getProtection(current);

            int bestIndex = -1;
            int bestProtection = currentProtection;

            for (int i = 0; i < 36; i++) {
                ItemStack stack = client.player.inventory.getStack(i);
                if (stack.getItem() instanceof ArmorItem) {
                    ArmorItem armor = (ArmorItem) stack.getItem();
                    if (armor.getSlotType() == slot) {
                        int protection = armor.getProtection();
                        if (protection > bestProtection) {
                            bestProtection = protection;
                            bestIndex = i;
                        }
                    }
                }
            }

            if (bestIndex != -1) {
                if (currentProtection >= 0) {
                    client.interactionManager.clickSlot(0, getArmorSlotId(slot), 0, SlotActionType.QUICK_MOVE, client.player);
                }
                int slotId = bestIndex < 9 ? bestIndex + 36 : bestIndex;
                client.interactionManager.clickSlot(0, slotId, 0, SlotActionType.QUICK_MOVE, client.player);
                return;
            }
        }
    }

    private int getProtection(ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem) {
            return ((ArmorItem) stack.getItem()).getProtection();
        }
        return -1;
    }

    private int getArmorSlotId(EquipmentSlot slot) {
        switch (slot) {
            case HEAD: return 5;
            case CHEST: return 6;
            case LEGS: return 7;
            case FEET: return 8;
            default: return -1;
        }
    }
}
