package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class XrayModule extends Module {
    
    public static List<Block> xrayBlocks = new ArrayList<>();
    
    public XrayModule() {
        super("Xray");
        
        
        xrayBlocks.add(Blocks.DIAMOND_ORE);
        xrayBlocks.add(Blocks.IRON_ORE);
        xrayBlocks.add(Blocks.GOLD_ORE);
        xrayBlocks.add(Blocks.COAL_ORE);
        xrayBlocks.add(Blocks.REDSTONE_ORE);
        xrayBlocks.add(Blocks.LAPIS_ORE);
        xrayBlocks.add(Blocks.EMERALD_ORE);
        xrayBlocks.add(Blocks.ANCIENT_DEBRIS);
        xrayBlocks.add(Blocks.NETHER_QUARTZ_ORE);
        
        
        xrayBlocks.add(Blocks.OAK_LOG);
        xrayBlocks.add(Blocks.SPRUCE_LOG);
        xrayBlocks.add(Blocks.BIRCH_LOG);
        xrayBlocks.add(Blocks.JUNGLE_LOG);
        xrayBlocks.add(Blocks.ACACIA_LOG);
        xrayBlocks.add(Blocks.DARK_OAK_LOG);
    }
    
    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.worldRenderer != null) {
            client.worldRenderer.reload();
        }
    }
    
    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.worldRenderer != null) {
            client.worldRenderer.reload();
        }
    }
}
