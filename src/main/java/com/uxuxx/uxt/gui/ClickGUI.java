package com.uxuxx.uxt.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.uxuxx.uxt.Module;
import com.uxuxx.uxt.UXTClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class ClickGUI extends Screen {
    
    private int dragX = 100;
    private int dragY = 100;
    private boolean dragging = false;
    private int dragOffsetX;
    private int dragOffsetY;
    
    public ClickGUI() {
        super(new LiteralText("UXT"));
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int width = 150;
        int moduleHeight = 18;
        int headerHeight = 20;
        int totalHeight = headerHeight + UXTClient.moduleManager.getModules().size() * moduleHeight;
        
        fill(matrices, dragX, dragY, dragX + width, dragY + headerHeight, 0xFF1a1a1a);
        drawCenteredText(matrices, textRenderer, "UXT", dragX + width / 2, dragY + 6, 0xFFFFFF);
        
        fill(matrices, dragX, dragY + headerHeight, dragX + width, dragY + totalHeight, 0xE0101010);
        
        int yOffset = dragY + headerHeight;
        for (Module module : UXTClient.moduleManager.getModules()) {
            int bgColor = module.isEnabled() ? 0xFF2a2a2a : 0xFF1a1a1a;
            fill(matrices, dragX + 2, yOffset + 2, dragX + width - 2, yOffset + moduleHeight - 2, bgColor);
            
            int textColor = module.isEnabled() ? 0x00FF00 : 0xAAAAAA;
            drawString(matrices, textRenderer, module.getName(), dragX + 8, yOffset + 6, textColor);
            
            yOffset += moduleHeight;
        }
        
        super.render(matrices, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int width = 150;
        int moduleHeight = 18;
        int headerHeight = 20;
        
        if (mouseX >= dragX && mouseX <= dragX + width && mouseY >= dragY && mouseY <= dragY + headerHeight) {
            dragging = true;
            dragOffsetX = (int)(mouseX - dragX);
            dragOffsetY = (int)(mouseY - dragY);
            return true;
        }
        
        int yOffset = dragY + headerHeight;
        for (Module module : UXTClient.moduleManager.getModules()) {
            if (mouseX >= dragX && mouseX <= dragX + width && mouseY >= yOffset && mouseY <= yOffset + moduleHeight) {
                module.toggle();
                return true;
            }
            yOffset += moduleHeight;
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging) {
            dragX = (int)(mouseX - dragOffsetX);
            dragY = (int)(mouseY - dragOffsetY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
}
