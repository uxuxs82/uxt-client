package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ScaffoldModule extends Module {
    
    public ScaffoldModule() {
        super("Scaffold");
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        
        BlockPos below = client.player.getBlockPos().down();
        BlockState state = client.world.getBlockState(below);
        
        if (!state.isAir()) return;
        
        
        int slot = findBlockSlot(client);
        if (slot == -1) return;
        
        int prevSlot = client.player.inventory.selectedSlot;
        client.player.inventory.selectedSlot = slot;
        
        
        BlockPos[] neighbors = {
            below.north(), below.south(), below.east(), below.west(), below.down()
        };
        Direction[] dirs = {
            Direction.SOUTH, Direction.NORTH, Direction.WEST, Direction.EAST, Direction.UP
        };
        
        for (int i = 0; i < neighbors.length; i++) {
            BlockPos neighbor = neighbors[i];
            if (!client.world.getBlockState(neighbor).isAir()) {
                Vec3d hitVec = Vec3d.ofCenter(neighbor).add(Vec3d.of(dirs[i].getVector()).multiply(0.5));
                BlockHitResult hit = new BlockHitResult(hitVec, dirs[i], neighbor, false);
                
                float prevYaw = client.player.yaw;
                float prevPitch = client.player.pitch;
                client.player.pitch = 80;
                
                client.interactionManager.interactBlock(client.player, client.world, Hand.MAIN_HAND, hit);
                
                client.player.yaw = prevYaw;
                client.player.pitch = prevPitch;
                break;
            }
        }
        
        client.player.inventory.selectedSlot = prevSlot;
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
