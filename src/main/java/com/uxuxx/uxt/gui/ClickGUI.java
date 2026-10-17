package com.uxuxx.uxt.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.uxuxx.uxt.Module;
import com.uxuxx.uxt.UXTClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.List;

public class ClickGUI extends Screen {
    
    private int dragX = 50;
    private int dragY = 50;
    private boolean dragging = false;
    private int dragOffsetX;
    private int dragOffsetY;
    private Module expandedModule = null;
    
    private static final int COLUMN_WIDTH = 150;
    private static final int MODULE_HEIGHT = 18;
    private static final int HEADER_HEIGHT = 20;
    private static final int SETTING_HEIGHT = 16;
    private static final int MODULES_PER_COLUMN = 9;
    
    public ClickGUI() {
        super(new LiteralText("UXT"));
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        List<Module> modules = UXTClient.moduleManager.getModules();
        int totalColumns = (int) Math.ceil((double) modules.size() / MODULES_PER_COLUMN);
        int totalWidth = totalColumns * COLUMN_WIDTH;
        
        // Calculate max height for expanded module
        int maxHeight = HEADER_HEIGHT + MODULES_PER_COLUMN * MODULE_HEIGHT;
        if (expandedModule != null && !expandedModule.getSettings().isEmpty()) {
            maxHeight += expandedModule.getSettings().size() * SETTING_HEIGHT;
        }
        
        // Header
        fill(matrices, dragX, dragY, dragX + totalWidth, dragY + HEADER_HEIGHT, 0xFF1a1a1a);
        drawCenteredText(matrices, textRenderer, "UXT Client - Press X", dragX + totalWidth / 2, dragY + 6, 0xFFFFFF);
        
        // Background
        fill(matrices, dragX, dragY + HEADER_HEIGHT, dragX + totalWidth, dragY + maxHeight, 0xE0101010);
        
        // Draw modules in columns
        int moduleIndex = 0;
        for (int col = 0; col < totalColumns; col++) {
            int colX = dragX + col * COLUMN_WIDTH;
            int yOffset = dragY + HEADER_HEIGHT;
            
            for (int row = 0; row < MODULES_PER_COLUMN && moduleIndex < modules.size(); row++, moduleIndex++) {
                Module module = modules.get(moduleIndex);
                
                // Module background
                int bgColor = module.isEnabled() ? 0xFF2a2a2a : 0xFF1a1a1a;
                fill(matrices, colX + 2, yOffset + 2, colX + COLUMN_WIDTH - 2, yOffset + MODULE_HEIGHT - 2, bgColor);
                
                // Module name
                int textColor = module.isEnabled() ? 0x00FF00 : 0xAAAAAA;
                String name = module.getName();
                if (textRenderer.getWidth(name) > COLUMN_WIDTH - 30) {
                    name = name.substring(0, Math.min(name.length(), 10)) + "..";
                }
                textRenderer.draw(matrices, name, colX + 8, yOffset + 6, textColor);
                
                // Settings indicator
                if (!module.getSettings().isEmpty()) {
                    String arrow = module == expandedModule ? "v" : ">";
                    textRenderer.draw(matrices, arrow, colX + COLUMN_WIDTH - 15, yOffset + 6, 0xAAAAAA);
                }
                
                // Draw settings if expanded (only for last column to avoid overlap)
                if (module == expandedModule && !module.getSettings().isEmpty() && col == totalColumns - 1) {
                    int settingY = yOffset + MODULE_HEIGHT;
                    for (Module.Setting setting : module.getSettings()) {
                        fill(matrices, colX + 10, settingY + 2, colX + COLUMN_WIDTH - 10, settingY + SETTING_HEIGHT - 2, 0xFF0a0a0a);
                        
                        // Setting name and value
                        String settingText = setting.getName() + ": " + String.format("%.1f", setting.getValue());
                        textRenderer.draw(matrices, settingText, colX + 15, settingY + 5, 0xCCCCCC);
                        
                        // Slider bar
                        int sliderX = colX + 15;
                        int sliderY = settingY + SETTING_HEIGHT - 5;
                        int sliderWidth = COLUMN_WIDTH - 30;
                        
                        fill(matrices, sliderX, sliderY, sliderX + sliderWidth, sliderY + 2, 0xFF555555);
                        
                        double percent = (setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin());
                        int filledWidth = (int)(sliderWidth * percent);
                        fill(matrices, sliderX, sliderY, sliderX + filledWidth, sliderY + 2, 0xFF00FF00);
                        
                        settingY += SETTING_HEIGHT;
                    }
                }
                
                yOffset += MODULE_HEIGHT;
            }
        }
        
        super.render(matrices, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        List<Module> modules = UXTClient.moduleManager.getModules();
        int totalColumns = (int) Math.ceil((double) modules.size() / MODULES_PER_COLUMN);
        int totalWidth = totalColumns * COLUMN_WIDTH;
        int maxHeight = HEADER_HEIGHT + MODULES_PER_COLUMN * MODULE_HEIGHT;
        
        // Check header drag
        if (mouseX >= dragX && mouseX <= dragX + totalWidth && mouseY >= dragY && mouseY <= dragY + HEADER_HEIGHT) {
            dragging = true;
            dragOffsetX = (int)(mouseX - dragX);
            dragOffsetY = (int)(mouseY - dragY);
            return true;
        }
        
        // Check module clicks
        int moduleIndex = 0;
        for (int col = 0; col < totalColumns; col++) {
            int colX = dragX + col * COLUMN_WIDTH;
            int yOffset = dragY + HEADER_HEIGHT;
            
            for (int row = 0; row < MODULES_PER_COLUMN && moduleIndex < modules.size(); row++, moduleIndex++) {
                Module module = modules.get(moduleIndex);
                
                // Check module click
                if (mouseX >= colX && mouseX <= colX + COLUMN_WIDTH && 
                    mouseY >= yOffset && mouseY <= yOffset + MODULE_HEIGHT) {
                    if (button == 0) { // Left click - toggle
                        module.toggle();
                    } else if (button == 1 && !module.getSettings().isEmpty()) { // Right click - expand
                        expandedModule = expandedModule == module ? null : module;
                    }
                    return true;
                }
                
                yOffset += MODULE_HEIGHT;
                
                // Check setting slider clicks (only for last column)
                if (module == expandedModule && !module.getSettings().isEmpty() && col == totalColumns - 1) {
                    for (Module.Setting setting : module.getSettings()) {
                        int sliderX = colX + 15;
                        int sliderY = yOffset + SETTING_HEIGHT - 5;
                        int sliderWidth = COLUMN_WIDTH - 30;
                        
                        if (mouseX >= sliderX && mouseX <= sliderX + sliderWidth && 
                            mouseY >= yOffset && mouseY <= yOffset + SETTING_HEIGHT) {
                            
                            double percent = (mouseX - sliderX) / sliderWidth;
                            double newValue = setting.getMin() + percent * (setting.getMax() - setting.getMin());
                            setting.setValue(newValue);
                            return true;
                        }
                        
                        yOffset += SETTING_HEIGHT;
                    }
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
