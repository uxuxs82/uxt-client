package com.uxuxx.uxt.mixin;

import com.uxuxx.uxt.modules.TimerModule;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderTickCounter.class)
public class RenderTickCounterMixin {

    @Inject(method = "beginRenderTick", at = @At("TAIL"))
    private void onBeginRenderTick(long timeMillis, CallbackInfoReturnable<Integer> cir) {
        float speed = TimerModule.timerSpeed;
        if (speed != 1.0f) {
            RenderTickCounter self = (RenderTickCounter) (Object) this;
            self.tickDelta += self.lastFrameDuration * (speed - 1.0f);
        }
    }
}
