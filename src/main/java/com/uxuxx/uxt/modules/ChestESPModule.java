package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class ChestESPModule extends Module {
    
    public static boolean enabled = false;
    public static List<BlockPos> chestPositions = new ArrayList<>();
    
    public ChestESPModule() {
        super("ChestESP");
    }
    
    @Override
    public void onEnable() {
        enabled = true;
    }
    
    @Override
    public void onDisable() {
        enabled = false;
        chestPositions.clear();
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        
        chestPositions.clear();
        BlockPos playerPos = client.player.getBlockPos();
        
        for (int x = -50; x <= 50; x++) {
            for (int y = -50; y <= 50; y++) {
                for (int z = -50; z <= 50; z++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    if (client.world.getBlockState(pos).getBlock() == Blocks.CHEST) {
                        chestPositions.add(pos);
                    }
                }
            }
        }
    }
}
