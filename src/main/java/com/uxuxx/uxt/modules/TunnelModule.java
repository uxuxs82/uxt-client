package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class TunnelModule extends Module {

    private Setting delaySetting;
    private int cooldown = 0;

    public TunnelModule() {
        super("Tunnel");
        delaySetting = new Setting("Delay", 1.0, 1.0, 10.0, 1.0);
        settings.add(delaySetting);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null || client.interactionManager == null) return;

        if (cooldown > 0) {
            cooldown--;
            return;
        }

        BlockPos playerPos = client.player.getBlockPos();
        Direction facing = client.player.getHorizontalFacing();
        BlockPos target1 = playerPos.offset(facing);
        BlockPos target2 = target1.up();

        if (breakBlock(client, target1) || breakBlock(client, target2)) {
            cooldown = (int) delaySetting.getValue() - 1;
        }
    }

    private boolean breakBlock(MinecraftClient client, BlockPos pos) {
        if (client.world.getBlockState(pos).isAir()) return false;
        client.interactionManager.updateBlockBreakingProgress(pos, Direction.UP);
        client.player.swingHand(Hand.MAIN_HAND);
        return true;
    }
}
