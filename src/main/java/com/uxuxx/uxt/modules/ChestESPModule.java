package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class ChestESPModule extends Module {

    public static boolean enabled = false;
    public static double range = 32.0;
    public static double colorR = 255.0;
    public static double colorG = 153.0;
    public static double colorB = 0.0;
    public static List<BlockPos> chestPositions = new ArrayList<>();

    private Setting rangeSetting;
    private Setting redSetting;
    private Setting greenSetting;
    private Setting blueSetting;
    private int tickCounter = 0;

    public ChestESPModule() {
        super("ChestESP");
        rangeSetting = new Setting("Range", 32.0, 16.0, 64.0, 8.0);
        redSetting = new Setting("Red", 255.0, 0.0, 255.0, 15.0);
        greenSetting = new Setting("Green", 153.0, 0.0, 255.0, 15.0);
        blueSetting = new Setting("Blue", 0.0, 0.0, 255.0, 15.0);
        settings.add(rangeSetting);
        settings.add(redSetting);
        settings.add(greenSetting);
        settings.add(blueSetting);
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

        range = rangeSetting.getValue();
        colorR = redSetting.getValue();
        colorG = greenSetting.getValue();
        colorB = blueSetting.getValue();

        tickCounter++;
        if (tickCounter < 20) return;
        tickCounter = 0;

        scan(client);
    }

    private void scan(MinecraftClient client) {
        chestPositions.clear();

        BlockPos center = client.player.getBlockPos();
        int r = (int) range;
        int vertical = 16;

        for (int x = -r; x <= r; x++) {
            for (int z = -r; z <= r; z++) {
                if (!client.world.isChunkLoaded((center.getX() + x) >> 4, (center.getZ() + z) >> 4)) continue;

                for (int y = -vertical; y <= vertical; y++) {
                    BlockPos pos = center.add(x, y, z);
                    Block block = client.world.getBlockState(pos).getBlock();

                    if (block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST ||
                        block == Blocks.ENDER_CHEST || block == Blocks.BARREL ||
                        block == Blocks.SHULKER_BOX) {
                        chestPositions.add(pos);
                    }
                }
            }
        }
    }
}
