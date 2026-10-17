package com.uxuxx.uxt.mixin;

import com.uxuxx.uxt.UXTClient;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {
    
    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private static void onShouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, CallbackInfoReturnable<Boolean> cir) {
        boolean xrayEnabled = UXTClient.moduleManager.getModules().stream()
            .anyMatch(m -> m.getName().equals("Xray") && m.isEnabled());
        
        if (xrayEnabled) {
            Block block = state.getBlock();
            if (block == Blocks.DIAMOND_ORE || block == Blocks.IRON_ORE || 
                block == Blocks.GOLD_ORE || block == Blocks.EMERALD_ORE ||
                block == Blocks.COAL_ORE || block == Blocks.REDSTONE_ORE ||
                block == Blocks.LAPIS_ORE || block == Blocks.NETHER_QUARTZ_ORE ||
                block == Blocks.ANCIENT_DEBRIS) {
                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
        }
    }
}
