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
    
    // Slider dragging
    private Module.Setting dragSetting = null;
    private int dragSliderX = 0;
    private int dragSliderWidth = 1;
    
    // Double click detection
    private Module lastClickedModule = null;
    private long lastClickTime = 0;
    private static final long DOUBLE_CLICK_DELAY = 500; // milliseconds
    
    // Info panel
    private boolean showInfo = false;
    
    private static final int COLUMN_WIDTH = 150;
    private static final int MODULE_HEIGHT = 18;
    private static final int HEADER_HEIGHT = 20;
    private static final int SETTING_HEIGHT = 16;
    private static final int MODULES_PER_COLUMN = 9;
    private static final int INFO_BUTTON_WIDTH = 40;
    private static final int INFO_BUTTON_HEIGHT = 16;
    
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
        drawCenteredText(matrices, textRenderer, "UXT Client - Press X or Z", dragX + totalWidth / 2, dragY + 6, 0xFFFFFF);
        
        // Info button
        int infoButtonX = dragX + totalWidth - INFO_BUTTON_WIDTH - 5;
        int infoButtonY = dragY + 2;
        fill(matrices, infoButtonX, infoButtonY, infoButtonX + INFO_BUTTON_WIDTH, infoButtonY + INFO_BUTTON_HEIGHT, 0xFF3a3a3a);
        drawCenteredText(matrices, textRenderer, "INFO", infoButtonX + INFO_BUTTON_WIDTH / 2, infoButtonY + 4, 0xFFFFFF);
        
        // Background
        fill(matrices, dragX, dragY + HEADER_HEIGHT, dragX + totalWidth, dragY + maxHeight, 0xE0101010);
        
        // Info panel overlay
        if (showInfo) {
            int infoPanelWidth = 300;
            int infoPanelHeight = 120;
            int infoPanelX = dragX + totalWidth / 2 - infoPanelWidth / 2;
            int infoPanelY = dragY + maxHeight / 2 - infoPanelHeight / 2;
            
            // Info background
            fill(matrices, infoPanelX, infoPanelY, infoPanelX + infoPanelWidth, infoPanelY + infoPanelHeight, 0xE0000000);
            fill(matrices, infoPanelX, infoPanelY, infoPanelX + infoPanelWidth, infoPanelY + 20, 0xFF2a2a2a);
            
            // Info text
            drawCenteredText(matrices, textRenderer, "UXT Client Info", infoPanelX + infoPanelWidth / 2, infoPanelY + 6, 0x00FF00);
            textRenderer.draw(matrices, "Author: uxuxx", infoPanelX + 20, infoPanelY + 35, 0xFFFFFF);
            textRenderer.draw(matrices, "Version: 1.0.4", infoPanelX + 20, infoPanelY + 50, 0xFFFFFF);
            textRenderer.draw(matrices, "Minecraft: 1.16.5 (Fabric)", infoPanelX + 20, infoPanelY + 65, 0xFFFFFF);
            textRenderer.draw(matrices, "Modules: " + modules.size(), infoPanelX + 20, infoPanelY + 80, 0xFFFFFF);
            
            // Close button
            String closeText = "Click anywhere to close";
            drawCenteredText(matrices, textRenderer, closeText, infoPanelX + infoPanelWidth / 2, infoPanelY + infoPanelHeight - 15, 0xAAAAAA);
            
            // Don't draw modules when info is open
            super.render(matrices, mouseX, mouseY, delta);
            return;
        }
        
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
                
                // Draw settings if expanded
                yOffset += MODULE_HEIGHT;
                
                if (module == expandedModule && !module.getSettings().isEmpty()) {
                    int settingY = yOffset;
                    for (Module.Setting setting : module.getSettings()) {
                        fill(matrices, colX + 10, settingY + 2, colX + COLUMN_WIDTH - 10, settingY + SETTING_HEIGHT - 2, 0xFF0a0a0a);
                        
                        // Setting name and value
                        double value = setting.getValue();
                        String valueText = value == Math.floor(value)
                            ? String.format("%.0f", value)
                            : String.format("%.1f", value);
                        String settingText = setting.getName() + ": " + valueText;
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
                    yOffset += module.getSettings().size() * SETTING_HEIGHT;
                }
            }
        }
        
        super.render(matrices, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // If info panel is open, close it on any click
        if (showInfo) {
            showInfo = false;
            return true;
        }
        
        List<Module> modules = UXTClient.moduleManager.getModules();
        int totalColumns = (int) Math.ceil((double) modules.size() / MODULES_PER_COLUMN);
        int totalWidth = totalColumns * COLUMN_WIDTH;
        int maxHeight = HEADER_HEIGHT + MODULES_PER_COLUMN * MODULE_HEIGHT;
        
        // Check info button click
        int infoButtonX = dragX + totalWidth - INFO_BUTTON_WIDTH - 5;
        int infoButtonY = dragY + 2;
        if (mouseX >= infoButtonX && mouseX <= infoButtonX + INFO_BUTTON_WIDTH &&
            mouseY >= infoButtonY && mouseY <= infoButtonY + INFO_BUTTON_HEIGHT) {
            showInfo = true;
            return true;
        }
        
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
                    if (button == 0) { // Left click
                        long currentTime = System.currentTimeMillis();
                        
                        // Check for double click
                        if (module == lastClickedModule && 
                            currentTime - lastClickTime < DOUBLE_CLICK_DELAY &&
                            !module.getSettings().isEmpty()) {
                            // Double click - open settings
                            expandedModule = expandedModule == module ? null : module;
                            lastClickedModule = null;
                            lastClickTime = 0;
                        } else {
                            // Single click - toggle module
                            module.toggle();
                            lastClickedModule = module;
                            lastClickTime = currentTime;
                        }
                    } else if (button == 1 && !module.getSettings().isEmpty()) { // Right click - open settings
                        expandedModule = expandedModule == module ? null : module;
                    }
                    return true;
                }
                
                int nextYOffset = yOffset + MODULE_HEIGHT;
                
                // Check setting slider clicks
                if (module == expandedModule && !module.getSettings().isEmpty()) {
                    int settingY = nextYOffset;
                    for (Module.Setting setting : module.getSettings()) {
                        int sliderX = colX + 15;
                        int sliderY = settingY + SETTING_HEIGHT - 5;
                        int sliderWidth = COLUMN_WIDTH - 30;
                        
                        if (mouseX >= sliderX && mouseX <= sliderX + sliderWidth && 
                            mouseY >= settingY && mouseY <= settingY + SETTING_HEIGHT) {
                            
                            double percent = (mouseX - sliderX) / sliderWidth;
                            double newValue = setting.getMin() + percent * (setting.getMax() - setting.getMin());
                            setting.setValue(newValue);
                            
                            dragSetting = setting;
                            dragSliderX = sliderX;
                            dragSliderWidth = sliderWidth;
                            return true;
                        }
                        
                        settingY += SETTING_HEIGHT;
                    }
                    nextYOffset += module.getSettings().size() * SETTING_HEIGHT;
                }
                
                yOffset = nextYOffset;
            }
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        dragSetting = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragSetting != null) {
            double percent = (mouseX - dragSliderX) / dragSliderWidth;
            percent = Math.max(0, Math.min(1, percent));
            double newValue = dragSetting.getMin() + percent * (dragSetting.getMax() - dragSetting.getMin());
            dragSetting.setValue(newValue);
            return true;
        }
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
