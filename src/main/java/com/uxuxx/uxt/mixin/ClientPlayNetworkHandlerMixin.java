package com.uxuxx.uxt.mixin;

import com.uxuxx.uxt.Module;
import com.uxuxx.uxt.UXTClient;
import com.uxuxx.uxt.modules.AntiKnockbackModule;
import com.uxuxx.uxt.modules.BlinkModule;
import com.uxuxx.uxt.modules.VelocityModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        if (BlinkModule.enabled && packet instanceof PlayerMoveC2SPacket) {
            ci.cancel();
        }
    }

    @Inject(method = "onVelocityUpdate", at = @At("HEAD"), cancellable = true)
    private void onVelocityUpdate(EntityVelocityUpdateS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || packet.getId() != client.player.getEntityId()) return;
        if (UXTClient.moduleManager == null) return;

        double multiplier = 1.0;
        boolean modified = false;

        for (Module module : UXTClient.moduleManager.getModules()) {
            if (!module.isEnabled()) continue;
            if (module instanceof VelocityModule) {
                multiplier = Math.min(multiplier, ((VelocityModule) module).getMultiplier());
                modified = true;
            } else if (module instanceof AntiKnockbackModule) {
                multiplier = Math.min(multiplier, ((AntiKnockbackModule) module).getMultiplier());
                modified = true;
            }
        }

        if (modified) {
            client.player.setVelocityClient(
                packet.getVelocityX() * multiplier / 8000.0,
                packet.getVelocityY() * multiplier / 8000.0,
                packet.getVelocityZ() * multiplier / 8000.0
            );
            ci.cancel();
        }
    }
}
