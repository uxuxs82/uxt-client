package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ScaffoldModule extends Module {

    private Setting bridgeSetting;
    private Setting towerSetting;
    private Setting delaySetting;

    private long lastPlace = 0;

    public ScaffoldModule() {
        super("Scaffold");
        bridgeSetting = new Setting("Bridge", 1, 0, 1, 1);
        towerSetting = new Setting("Tower", 1, 0, 1, 1);
        delaySetting = new Setting("Delay", 0, 0, 5, 1);
        settings.add(bridgeSetting);
        settings.add(towerSetting);
        settings.add(delaySetting);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;

        long now = System.currentTimeMillis();
        if (now - lastPlace < (long) delaySetting.getValue() * 50L) return;

        int slot = findBlockSlot(client);
        if (slot == -1) return;

        int prevSlot = client.player.inventory.selectedSlot;
        client.player.inventory.selectedSlot = slot;

        BlockPos feet = client.player.getBlockPos();
        BlockPos below = feet.down();

        if (towerSetting.getValue() >= 1 && client.player.getVelocity().y > 0.1) {
            place(client, below);
        }

        if (client.world.getBlockState(below).isAir()) {
            place(client, below);
        }

        if (bridgeSetting.getValue() >= 1 && isMoving(client)) {
            BlockPos forward = feet.offset(client.player.getHorizontalFacing());
            if (client.world.getBlockState(forward).isAir() && client.world.getBlockState(forward.down()).isAir()) {
                place(client, forward.down());
            }
        }

        client.player.inventory.selectedSlot = prevSlot;
        lastPlace = now;
    }

    private boolean isMoving(MinecraftClient client) {
        return client.player.input.movementForward != 0 || client.player.input.movementSideways != 0;
    }

    private void place(MinecraftClient client, BlockPos target) {
        BlockPos[] neighbors = {
            target.north(), target.south(), target.east(), target.west(),
            target.down(), target.up()
        };
        Direction[] dirs = {
            Direction.SOUTH, Direction.NORTH, Direction.WEST, Direction.EAST,
            Direction.UP, Direction.DOWN
        };

        for (int i = 0; i < neighbors.length; i++) {
            BlockPos neighbor = neighbors[i];
            if (!client.world.getBlockState(neighbor).isAir()) {
                Vec3d hitVec = Vec3d.ofCenter(neighbor).add(Vec3d.of(dirs[i].getVector()).multiply(0.5));
                BlockHitResult hit = new BlockHitResult(hitVec, dirs[i], neighbor, false);

                float prevYaw = client.player.yaw;
                float prevPitch = client.player.pitch;
                client.player.yaw = (float) Math.toDegrees(Math.atan2(
                    neighbor.getZ() + 0.5 - client.player.getZ(),
                    neighbor.getX() + 0.5 - client.player.getX())) - 90f;
                client.player.pitch = 80;

                client.interactionManager.interactBlock(client.player, client.world, Hand.MAIN_HAND, hit);

                client.player.yaw = prevYaw;
                client.player.pitch = prevPitch;
                return;
            }
        }
    }

    private int findBlockSlot(MinecraftClient client) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.inventory.getStack(i);
            if (stack.getItem() instanceof BlockItem) {
                BlockItem bi = (BlockItem) stack.getItem();
                if (bi.getBlock() != Blocks.AIR) {
                    return i;
                }
            }
        }
        return -1;
    }
}
