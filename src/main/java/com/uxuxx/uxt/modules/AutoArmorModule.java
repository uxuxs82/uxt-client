package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class AutoArmorModule extends Module {
    
    private int tickCounter = 0;
    
    public AutoArmorModule() {
        super("AutoArmor");
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        
        tickCounter++;
        if (tickCounter < 40) return;
        tickCounter = 0;
        
        for (int i = 0; i < 4; i++) {
            EquipmentSlot slot = EquipmentSlot.values()[i + 2]; // HEAD, CHEST, LEGS, FEET
            ItemStack current = client.player.getEquippedStack(slot);
            
            int bestSlot = findBestArmor(client, i);
            if (bestSlot != -1) {
                client.interactionManager.clickSlot(0, bestSlot, 0, SlotActionType.QUICK_MOVE, client.player);
            }
        }
    }
    
    private int findBestArmor(MinecraftClient client, int armorType) {
        int bestSlot = -1;
        int bestProtection = -1;
        
        for (int i = 0; i < client.player.inventory.size(); i++) {
            ItemStack stack = client.player.inventory.getStack(i);
            if (stack.getItem() instanceof ArmorItem) {
                ArmorItem armor = (ArmorItem) stack.getItem();
                if (armor.getSlotType().getEntitySlotId() == armorType + 2) {
                    int protection = armor.getProtection();
                    if (protection > bestProtection) {
                        bestProtection = protection;
                        bestSlot = i;
                    }
                }
            }
        }
        
        return bestSlot;
    }
}
