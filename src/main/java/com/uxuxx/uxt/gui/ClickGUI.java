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
    private Module expandedModule = null;
    
    public ClickGUI() {
        super(new LiteralText("UXT"));
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int width = 180;
        int moduleHeight = 18;
        int headerHeight = 20;
        int settingHeight = 16;
        
        // Calculate total height
        int totalHeight = headerHeight;
        for (Module module : UXTClient.moduleManager.getModules()) {
            totalHeight += moduleHeight;
            if (module == expandedModule && !module.getSettings().isEmpty()) {
                totalHeight += module.getSettings().size() * settingHeight;
            }
        }
        
        // Header
        fill(matrices, dragX, dragY, dragX + width, dragY + headerHeight, 0xFF1a1a1a);
        drawCenteredText(matrices, textRenderer, "UXT Client", dragX + width / 2, dragY + 6, 0xFFFFFF);
        
        // Background
        fill(matrices, dragX, dragY + headerHeight, dragX + width, dragY + totalHeight, 0xE0101010);
        
        int yOffset = dragY + headerHeight;
        
        for (Module module : UXTClient.moduleManager.getModules()) {
            // Module background
            int bgColor = module.isEnabled() ? 0xFF2a2a2a : 0xFF1a1a1a;
            fill(matrices, dragX + 2, yOffset + 2, dragX + width - 2, yOffset + moduleHeight - 2, bgColor);
            
            // Module name
            int textColor = module.isEnabled() ? 0x00FF00 : 0xAAAAAA;
            textRenderer.draw(matrices, module.getName(), dragX + 8, yOffset + 6, textColor);
            
            // Settings indicator
            if (!module.getSettings().isEmpty()) {
                String arrow = module == expandedModule ? "v" : ">";
                textRenderer.draw(matrices, arrow, dragX + width - 15, yOffset + 6, 0xAAAAAA);
            }
            
            yOffset += moduleHeight;
            
            // Draw settings if expanded
            if (module == expandedModule && !module.getSettings().isEmpty()) {
                for (Module.Setting setting : module.getSettings()) {
                    fill(matrices, dragX + 10, yOffset + 2, dragX + width - 10, yOffset + settingHeight - 2, 0xFF0a0a0a);
                    
                    // Setting name and value
                    String settingText = setting.getName() + ": " + String.format("%.1f", setting.getValue());
                    textRenderer.draw(matrices, settingText, dragX + 15, yOffset + 5, 0xCCCCCC);
                    
                    // Slider bar
                    int sliderX = dragX + 15;
                    int sliderY = yOffset + settingHeight - 5;
                    int sliderWidth = width - 30;
                    
                    fill(matrices, sliderX, sliderY, sliderX + sliderWidth, sliderY + 2, 0xFF555555);
                    
                    double percent = (setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin());
                    int filledWidth = (int)(sliderWidth * percent);
                    fill(matrices, sliderX, sliderY, sliderX + filledWidth, sliderY + 2, 0xFF00FF00);
                    
                    yOffset += settingHeight;
                }
            }
        }
        
        super.render(matrices, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int width = 180;
        int moduleHeight = 18;
        int headerHeight = 20;
        int settingHeight = 16;
        
        // Check header drag
        if (mouseX >= dragX && mouseX <= dragX + width && mouseY >= dragY && mouseY <= dragY + headerHeight) {
            dragging = true;
            dragOffsetX = (int)(mouseX - dragX);
            dragOffsetY = (int)(mouseY - dragY);
            return true;
        }
        
        int yOffset = dragY + headerHeight;
        
        for (Module module : UXTClient.moduleManager.getModules()) {
            // Check module click
            if (mouseX >= dragX && mouseX <= dragX + width && mouseY >= yOffset && mouseY <= yOffset + moduleHeight) {
                if (button == 0) { // Left click - toggle
                    module.toggle();
                } else if (button == 1 && !module.getSettings().isEmpty()) { // Right click - expand
                    expandedModule = expandedModule == module ? null : module;
                }
                return true;
            }
            
            yOffset += moduleHeight;
            
            // Check setting slider clicks
            if (module == expandedModule && !module.getSettings().isEmpty()) {
                for (Module.Setting setting : module.getSettings()) {
                    int sliderX = dragX + 15;
                    int sliderY = yOffset + settingHeight - 5;
                    int sliderWidth = width - 30;
                    
                    if (mouseX >= sliderX && mouseX <= sliderX + sliderWidth && 
                        mouseY >= yOffset && mouseY <= yOffset + settingHeight) {
                        
                        double percent = (mouseX - sliderX) / sliderWidth;
                        double newValue = setting.getMin() + percent * (setting.getMax() - setting.getMin());
                        setting.setValue(newValue);
                        return true;
                    }
                    
                    yOffset += settingHeight;
                }
            }
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
    
    public boolean shouldPauseGame() {
        return false;
    }
}
