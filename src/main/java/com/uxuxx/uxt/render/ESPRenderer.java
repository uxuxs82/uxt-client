package com.uxuxx.uxt.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.uxuxx.uxt.modules.ChestESPModule;
import com.uxuxx.uxt.modules.ESPModule;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ESPRenderer {

    public static void register() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(ESPRenderer::render);
    }

    private static void render(WorldRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;

        boolean chests = ChestESPModule.enabled && !ChestESPModule.chestPositions.isEmpty();
        boolean players = ESPModule.enabled;
        if (!chests && !players) return;

        MatrixStack matrices = context.matrixStack();
        if (matrices == null) return;

        Vec3d camera = context.camera().getPos();
        matrices.push();
        matrices.translate(-camera.x, -camera.y, -camera.z);

        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.multMatrix(matrices.peek().getModel());

        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.lineWidth(2.0f);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_LINES, VertexFormats.POSITION_COLOR);

        if (chests) {
            for (BlockPos pos : ChestESPModule.chestPositions) {
                Box box = new Box(
                    pos.getX(), pos.getY(), pos.getZ(),
                    pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0
                );
                emitBox(buffer, box, 1.0f, 0.6f, 0.0f, 0.8f);
            }
        }

        if (players) {
            double range = ESPModule.range;
            for (PlayerEntity player : client.world.getPlayers()) {
                if (player == client.player) continue;
                if (client.player.distanceTo(player) > range) continue;
                emitBox(buffer, player.getBoundingBox(), 1.0f, 0.2f, 0.2f, 0.8f);
            }
        }

        tessellator.draw();

        RenderSystem.lineWidth(1.0f);
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();

        RenderSystem.popMatrix();
        matrices.pop();
    }

    private static void emitBox(BufferBuilder buffer, Box box, float r, float g, float b, float a) {
        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;
        float x2 = (float) box.maxX;
        float y2 = (float) box.maxY;
        float z2 = (float) box.maxZ;

        edge(buffer, x1, y1, z1, x2, y1, z1, r, g, b, a);
        edge(buffer, x2, y1, z1, x2, y1, z2, r, g, b, a);
        edge(buffer, x2, y1, z2, x1, y1, z2, r, g, b, a);
        edge(buffer, x1, y1, z2, x1, y1, z1, r, g, b, a);

        edge(buffer, x1, y2, z1, x2, y2, z1, r, g, b, a);
        edge(buffer, x2, y2, z1, x2, y2, z2, r, g, b, a);
        edge(buffer, x2, y2, z2, x1, y2, z2, r, g, b, a);
        edge(buffer, x1, y2, z2, x1, y2, z1, r, g, b, a);

        edge(buffer, x1, y1, z1, x1, y2, z1, r, g, b, a);
        edge(buffer, x2, y1, z1, x2, y2, z1, r, g, b, a);
        edge(buffer, x2, y1, z2, x2, y2, z2, r, g, b, a);
        edge(buffer, x1, y1, z2, x1, y2, z2, r, g, b, a);
    }

    private static void edge(BufferBuilder buffer, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a) {
        buffer.vertex(x1, y1, z1).color(r, g, b, a).next();
        buffer.vertex(x2, y2, z2).color(r, g, b, a).next();
    }
}
