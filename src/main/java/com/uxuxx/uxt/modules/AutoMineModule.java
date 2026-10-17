package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class AutoMineModule extends Module {
    
    private Setting rangeSetting;
    private List<Block> targetBlocks = new ArrayList<>();
    private BlockPos targetPos = null;
    private int breakDelay = 0;
    
    public AutoMineModule() {
        super("AutoMine");
        rangeSetting = new Setting("Range", 32.0, 16.0, 64.0, 8.0);
        settings.add(rangeSetting);
        
        // Default blocks to mine
        targetBlocks.add(Blocks.DIAMOND_ORE);
        targetBlocks.add(Blocks.IRON_ORE);
        targetBlocks.add(Blocks.GOLD_ORE);
        targetBlocks.add(Blocks.COAL_ORE);
        targetBlocks.add(Blocks.REDSTONE_ORE);
        targetBlocks.add(Blocks.LAPIS_ORE);
        targetBlocks.add(Blocks.EMERALD_ORE);
        targetBlocks.add(Blocks.ANCIENT_DEBRIS);
        targetBlocks.add(Blocks.OAK_LOG);
        targetBlocks.add(Blocks.SPRUCE_LOG);
        targetBlocks.add(Blocks.BIRCH_LOG);
        targetBlocks.add(Blocks.STONE);
    }
    
    public List<Block> getTargetBlocks() {
        return targetBlocks;
    }
    
    public void toggleBlock(Block block) {
        if (targetBlocks.contains(block)) {
            targetBlocks.remove(block);
        } else {
            targetBlocks.add(block);
        }
    }
    
    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;
        
        if (breakDelay > 0) {
            breakDelay--;
            return;
        }
        
        // Find nearest target block
        if (targetPos == null || !isTargetBlock(client, targetPos)) {
            targetPos = findNearestBlock(client);
        }
        
        if (targetPos == null) return;
        
        // Move towards target
        Vec3d targetVec = Vec3d.ofCenter(targetPos);
        Vec3d playerPos = client.player.getPos();
        
        double distance = playerPos.distanceTo(targetVec);
        
        if (distance > 4.5) {
            // Walk towards block
            Vec3d direction = targetVec.subtract(playerPos).normalize();
            
            float yaw = (float) Math.toDegrees(Math.atan2(direction.z, direction.x)) - 90;
            client.player.yaw = yaw;
            client.player.input.movementForward = 1.0f;
            
            // Jump if needed
            if (client.player.isOnGround() && client.player.horizontalCollision) {
                client.player.jump();
            }
        } else {
            // Mine the block
            client.player.input.movementForward = 0;
            
            // Look at block
            Vec3d eyePos = client.player.getCameraPosVec(1.0f);
            Vec3d lookVec = targetVec.subtract(eyePos).normalize();
            
            float yaw = (float) Math.toDegrees(Math.atan2(lookVec.z, lookVec.x)) - 90;
            float pitch = (float) -Math.toDegrees(Math.asin(lookVec.y));
            
            client.player.yaw = yaw;
            client.player.pitch = pitch;
            
            // Attack block
            if (client.interactionManager != null) {
                BlockHitResult hitResult = new BlockHitResult(targetVec, 
                    net.minecraft.util.math.Direction.UP, targetPos, false);
                
                client.interactionManager.attackBlock(targetPos, net.minecraft.util.math.Direction.UP);
                client.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
                
                // Check if block is broken
                if (client.world.getBlockState(targetPos).isAir()) {
                    targetPos = null;
                    breakDelay = 5;
                }
            }
        }
    }
    
    private BlockPos findNearestBlock(MinecraftClient client) {
        BlockPos playerPos = client.player.getBlockPos();
        BlockPos nearest = null;
        double nearestDist = Double.MAX_VALUE;
        
        int range = (int) rangeSetting.getValue();
        
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    
                    if (isTargetBlock(client, pos)) {
                        double dist = client.player.getPos().distanceTo(Vec3d.ofCenter(pos));
                        if (dist < nearestDist) {
                            nearestDist = dist;
                            nearest = pos;
                        }
                    }
                }
            }
        }
        
        return nearest;
    }
    
    private boolean isTargetBlock(MinecraftClient client, BlockPos pos) {
        Block block = client.world.getBlockState(pos).getBlock();
        return targetBlocks.contains(block);
    }
}
