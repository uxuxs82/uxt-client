package com.uxuxx.uxt.modules;

import com.uxuxx.uxt.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class NoSlowModule extends Module {

    private Setting speedSetting;

    public NoSlowModule() {
        super("NoSlow");
        speedSetting = new Setting("Speed", 1.0, 1.0, 2.0, 0.5);
        settings.add(speedSetting);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled() || client.player == null) return;
        if (!client.player.isUsingItem() || client.player.hasVehicle()) return;
        if (client.player.input.movementForward == 0 && client.player.input.movementSideways == 0) return;

        Vec3d velocity = client.player.getVelocity();
        double horizontal = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
        double target = 0.2159 * speedSetting.getValue();

        if (horizontal > 0.001 && horizontal < target) {
            double scale = target / horizontal;
            client.player.setVelocity(velocity.x * scale, velocity.y, velocity.z * scale);
        }
    }
}
