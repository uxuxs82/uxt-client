package com.uxuxx.uxt.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.uxuxx.uxt.Module;
import com.uxuxx.uxt.UXTClient;
import com.uxuxx.uxt.modules.HudModule;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;

public class HudRenderer {

    private static int frames = 0;
    private static int fps = 0;
    private static long lastFpsUpdate = 0;

    public static void register() {
        HudRenderCallback.EVENT.register(HudRenderer::render);
    }

    private static void render(MatrixStack matrices, float tickDelta) {
        updateFps();

        MinecraftClient client = MinecraftClient.getInstance();
        if (!HudModule.enabled) return;
        if (UXTClient.moduleManager == null) return;
        if (client.player == null || client.world == null) return;
        if (client.options.hudHidden) return;

        drawText(client, matrices, "UXT Client", 4, 4, 0x00FF00);
        drawText(client, matrices, "FPS: " + fps, 4, 16, 0xFFFFFF);
        drawText(client, matrices, "XYZ: "
            + (int) Math.floor(client.player.getX()) + " "
            + (int) Math.floor(client.player.getY()) + " "
            + (int) Math.floor(client.player.getZ()), 4, 28, 0xFFFFFF);

        int screenWidth = client.getWindow().getScaledWidth();
        int y = 4;
        for (Module module : UXTClient.moduleManager.getModules()) {
            if (!module.isEnabled() || module instanceof HudModule) continue;
            String name = module.getName();
            int width = client.textRenderer.getWidth(name);
            int right = screenWidth - 4;
            int left = right - width - 8;
            fill(matrices, left, y - 1, right, y + 9, 0x80101010);
            fill(matrices, left, y - 1, left + 2, y + 9, 0xFF00FF00);
            client.textRenderer.drawWithShadow(matrices, name, left + 5, y, 0xFFFFFF);
            y += 11;
        }
    }

    private static void updateFps() {
        frames++;
        long now = System.currentTimeMillis();
        if (now - lastFpsUpdate >= 1000) {
            fps = frames;
            frames = 0;
            lastFpsUpdate = now;
        }
    }

    private static void drawText(MinecraftClient client, MatrixStack matrices, String text, int x, int y, int color) {
        int width = client.textRenderer.getWidth(text);
        fill(matrices, x - 2, y - 1, x + width + 2, y + 9, 0x80101010);
        client.textRenderer.drawWithShadow(matrices, text, x, y, color);
    }

    private static void fill(MatrixStack matrices, float x1, float y1, float x2, float y2, int color) {
        float alpha = (color >> 24 & 255) / 255.0f;
        float red = (color >> 16 & 255) / 255.0f;
        float green = (color >> 8 & 255) / 255.0f;
        float blue = (color & 255) / 255.0f;

        Matrix4f model = matrices.peek().getModel();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();

        buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(model, x1, y2, 0.0f).color(red, green, blue, alpha).next();
        buffer.vertex(model, x2, y2, 0.0f).color(red, green, blue, alpha).next();
        buffer.vertex(model, x2, y1, 0.0f).color(red, green, blue, alpha).next();
        buffer.vertex(model, x1, y1, 0.0f).color(red, green, blue, alpha).next();
        Tessellator.getInstance().draw();

        RenderSystem.enableTexture();
    }
}
