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
    
    private Module.Setting dragSetting = null;
    private int dragSliderX = 0;
    private int dragSliderWidth = 1;
    
    private Module lastClickedModule = null;
    private long lastClickTime = 0;
    private static final long DOUBLE_CLICK_DELAY = 500;
    
    private boolean showInfo = false;
    
    private static final int COLUMN_WIDTH = 160;
    private static final int MODULE_HEIGHT = 20;
    private static final int HEADER_HEIGHT = 24;
    private static final int SETTING_HEIGHT = 18;
    private static final int MODULES_PER_COLUMN = 10;
    private static final int INFO_BUTTON_WIDTH = 45;
    private static final int INFO_BUTTON_HEIGHT = 18;
    
    private static final int COLOR_HEADER = 0xFF0d1117;
    private static final int COLOR_BG = 0xE0161b22;
    private static final int COLOR_MODULE_OFF = 0xFF0d1117;
    private static final int COLOR_MODULE_ON = 0xFF1c2128;
    private static final int COLOR_BORDER = 0xFF30363d;
    private static final int COLOR_TEXT_ENABLED = 0xFF58a6ff;
    private static final int COLOR_TEXT_DISABLED = 0xFF8b949e;
    private static final int COLOR_SETTING_BG = 0xFF010409;
    private static final int COLOR_SLIDER_BG = 0xFF21262d;
    private static final int COLOR_SLIDER_FILL = 0xFF58a6ff;
    
    public ClickGUI() {
        super(new LiteralText("UXT"));
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        List<Module> modules = UXTClient.moduleManager.getModules();
        int totalColumns = (int) Math.ceil((double) modules.size() / MODULES_PER_COLUMN);
        int totalWidth = totalColumns * COLUMN_WIDTH;
        
        int maxHeight = HEADER_HEIGHT + MODULES_PER_COLUMN * MODULE_HEIGHT;
        if (expandedModule != null && !expandedModule.getSettings().isEmpty()) {
            maxHeight += expandedModule.getSettings().size() * SETTING_HEIGHT;
        }
        
        // Header background
        fill(matrices, dragX, dragY, dragX + totalWidth, dragY + HEADER_HEIGHT, COLOR_HEADER);
        drawLine(matrices, dragX, dragY + HEADER_HEIGHT, dragX + totalWidth, dragY + HEADER_HEIGHT, 0xFF30363d);
        
        drawCenteredText(matrices, textRenderer, "UXT Client", dragX + totalWidth / 2 - 50, dragY + 8, COLOR_TEXT_ENABLED);
        
        // Info button
        int infoButtonX = dragX + totalWidth - INFO_BUTTON_WIDTH - 5;
        int infoButtonY = dragY + 3;
        fill(matrices, infoButtonX, infoButtonY, infoButtonX + INFO_BUTTON_WIDTH, infoButtonY + INFO_BUTTON_HEIGHT, COLOR_MODULE_OFF);
        drawLine(matrices, infoButtonX, infoButtonY, infoButtonX + INFO_BUTTON_WIDTH, infoButtonY, COLOR_BORDER);
        drawLine(matrices, infoButtonX, infoButtonY, infoButtonX, infoButtonY + INFO_BUTTON_HEIGHT, COLOR_BORDER);
        drawLine(matrices, infoButtonX + INFO_BUTTON_WIDTH, infoButtonY, infoButtonX + INFO_BUTTON_WIDTH, infoButtonY + INFO_BUTTON_HEIGHT, COLOR_BORDER);
        drawLine(matrices, infoButtonX, infoButtonY + INFO_BUTTON_HEIGHT, infoButtonX + INFO_BUTTON_WIDTH, infoButtonY + INFO_BUTTON_HEIGHT, COLOR_BORDER);
        drawCenteredText(matrices, textRenderer, "INFO", infoButtonX + INFO_BUTTON_WIDTH / 2, infoButtonY + 4, COLOR_TEXT_ENABLED);
        
        // Main background
        fill(matrices, dragX, dragY + HEADER_HEIGHT, dragX + totalWidth, dragY + maxHeight, COLOR_BG);
        
        // Info panel
        if (showInfo) {
            int infoPanelWidth = 320;
            int infoPanelHeight = 140;
            int infoPanelX = dragX + totalWidth / 2 - infoPanelWidth / 2;
            int infoPanelY = dragY + HEADER_HEIGHT + 20;
            
            fill(matrices, infoPanelX, infoPanelY, infoPanelX + infoPanelWidth, infoPanelY + infoPanelHeight, 0xE0010409);
            drawLine(matrices, infoPanelX, infoPanelY, infoPanelX + infoPanelWidth, infoPanelY, COLOR_BORDER);
            drawLine(matrices, infoPanelX, infoPanelY, infoPanelX, infoPanelY + infoPanelHeight, COLOR_BORDER);
            drawLine(matrices, infoPanelX + infoPanelWidth, infoPanelY, infoPanelX + infoPanelWidth, infoPanelY + infoPanelHeight, COLOR_BORDER);
            drawLine(matrices, infoPanelX, infoPanelY + infoPanelHeight, infoPanelX + infoPanelWidth, infoPanelY + infoPanelHeight, COLOR_BORDER);
            
            drawCenteredText(matrices, textRenderer, "UXT Client Info", infoPanelX + infoPanelWidth / 2, infoPanelY + 10, COLOR_TEXT_ENABLED);
            textRenderer.draw(matrices, "Author: uxuxx", infoPanelX + 20, infoPanelY + 35, COLOR_TEXT_DISABLED);
            textRenderer.draw(matrices, "Version: 1.0.4", infoPanelX + 20, infoPanelY + 50, COLOR_TEXT_DISABLED);
            textRenderer.draw(matrices, "Minecraft: 1.16.5", infoPanelX + 20, infoPanelY + 65, COLOR_TEXT_DISABLED);
            textRenderer.draw(matrices, "Modules: " + modules.size(), infoPanelX + 20, infoPanelY + 80, COLOR_TEXT_DISABLED);
            drawCenteredText(matrices, textRenderer, "Click anywhere to close", infoPanelX + infoPanelWidth / 2, infoPanelY + infoPanelHeight - 15, 0xFF6e7681);
            
            super.render(matrices, mouseX, mouseY, delta);
            return;
        }
        
        // Draw modules
        int moduleIndex = 0;
        for (int col = 0; col < totalColumns; col++) {
            int colX = dragX + col * COLUMN_WIDTH;
            int yOffset = dragY + HEADER_HEIGHT;
            
            for (int row = 0; row < MODULES_PER_COLUMN && moduleIndex < modules.size(); row++, moduleIndex++) {
                Module module = modules.get(moduleIndex);
                
                int bgColor = module.isEnabled() ? COLOR_MODULE_ON : COLOR_MODULE_OFF;
                fill(matrices, colX, yOffset, colX + COLUMN_WIDTH, yOffset + MODULE_HEIGHT, bgColor);
                drawLine(matrices, colX, yOffset, colX + COLUMN_WIDTH, yOffset, COLOR_BORDER);
                drawLine(matrices, colX, yOffset, colX, yOffset + MODULE_HEIGHT, COLOR_BORDER);
                drawLine(matrices, colX + COLUMN_WIDTH, yOffset, colX + COLUMN_WIDTH, yOffset + MODULE_HEIGHT, COLOR_BORDER);
                
                int textColor = module.isEnabled() ? COLOR_TEXT_ENABLED : COLOR_TEXT_DISABLED;
                String name = module.getName();
                if (textRenderer.getWidth(name) > COLUMN_WIDTH - 35) {
                    name = name.substring(0, Math.min(name.length(), 12)) + "..";
                }
                textRenderer.draw(matrices, name, colX + 8, yOffset + 6, textColor);
                
                if (!module.getSettings().isEmpty()) {
                    String arrow = module == expandedModule ? "▼" : "▶";
                    textRenderer.draw(matrices, arrow, colX + COLUMN_WIDTH - 15, yOffset + 6, COLOR_TEXT_DISABLED);
                }
                
                yOffset += MODULE_HEIGHT;
                
                if (module == expandedModule && !module.getSettings().isEmpty()) {
                    int settingY = yOffset;
                    for (Module.Setting setting : module.getSettings()) {
                        fill(matrices, colX, settingY, colX + COLUMN_WIDTH, settingY + SETTING_HEIGHT, COLOR_SETTING_BG);
                        drawLine(matrices, colX, settingY, colX + COLUMN_WIDTH, settingY, COLOR_BORDER);
                        
                        double value = setting.getValue();
                        String valueText = value == Math.floor(value)
                            ? String.format("%.0f", value)
                            : String.format("%.1f", value);
                        String settingText = setting.getName() + ": " + valueText;
                        textRenderer.draw(matrices, settingText, colX + 8, settingY + 4, COLOR_TEXT_DISABLED);
                        
                        int sliderX = colX + 8;
                        int sliderY = settingY + SETTING_HEIGHT - 6;
                        int sliderWidth = COLUMN_WIDTH - 16;
                        
                        fill(matrices, sliderX, sliderY, sliderX + sliderWidth, sliderY + 3, COLOR_SLIDER_BG);
                        double percent = (setting.getValue() - setting.getMin()) / (setting.getMax() - setting.getMin());
                        int filledWidth = (int)(sliderWidth * percent);
                        fill(matrices, sliderX, sliderY, sliderX + filledWidth, sliderY + 3, COLOR_SLIDER_FILL);
                        
                        settingY += SETTING_HEIGHT;
                    }
                    yOffset += module.getSettings().size() * SETTING_HEIGHT;
                }
            }
        }
        
        drawLine(matrices, dragX, dragY + maxHeight, dragX + totalWidth, dragY + maxHeight, COLOR_BORDER);
        
        super.render(matrices, mouseX, mouseY, delta);
    }
    
    private void drawLine(MatrixStack matrices, int x1, int y1, int x2, int y2, int color) {
        if (x1 == x2) {
            fill(matrices, x1, Math.min(y1, y2), x1 + 1, Math.max(y1, y2), color);
        } else {
            fill(matrices, Math.min(x1, x2), y1, Math.max(x1, x2), y1 + 1, color);
        }
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (showInfo) {
            showInfo = false;
            return true;
        }
        
        List<Module> modules = UXTClient.moduleManager.getModules();
        int totalColumns = (int) Math.ceil((double) modules.size() / MODULES_PER_COLUMN);
        int totalWidth = totalColumns * COLUMN_WIDTH;
        
        int infoButtonX = dragX + totalWidth - INFO_BUTTON_WIDTH - 5;
        int infoButtonY = dragY + 3;
        if (mouseX >= infoButtonX && mouseX <= infoButtonX + INFO_BUTTON_WIDTH &&
            mouseY >= infoButtonY && mouseY <= infoButtonY + INFO_BUTTON_HEIGHT) {
            showInfo = true;
            return true;
        }
        
        if (mouseX >= dragX && mouseX <= dragX + totalWidth && mouseY >= dragY && mouseY <= dragY + HEADER_HEIGHT) {
            dragging = true;
            dragOffsetX = (int)(mouseX - dragX);
            dragOffsetY = (int)(mouseY - dragY);
            return true;
        }
        
        int moduleIndex = 0;
        for (int col = 0; col < totalColumns; col++) {
            int colX = dragX + col * COLUMN_WIDTH;
            int yOffset = dragY + HEADER_HEIGHT;
            
            for (int row = 0; row < MODULES_PER_COLUMN && moduleIndex < modules.size(); row++, moduleIndex++) {
                Module module = modules.get(moduleIndex);
                
                if (mouseX >= colX && mouseX <= colX + COLUMN_WIDTH && 
                    mouseY >= yOffset && mouseY <= yOffset + MODULE_HEIGHT) {
                    if (button == 0) {
                        long currentTime = System.currentTimeMillis();
                        if (module == lastClickedModule && 
                            currentTime - lastClickTime < DOUBLE_CLICK_DELAY &&
                            !module.getSettings().isEmpty()) {
                            expandedModule = expandedModule == module ? null : module;
                            lastClickedModule = null;
                            lastClickTime = 0;
                        } else {
                            module.toggle();
                            lastClickedModule = module;
                            lastClickTime = currentTime;
                        }
                    } else if (button == 1 && !module.getSettings().isEmpty()) {
                        expandedModule = expandedModule == module ? null : module;
                    }
                    return true;
                }
                
                int nextYOffset = yOffset + MODULE_HEIGHT;
                
                if (module == expandedModule && !module.getSettings().isEmpty()) {
                    int settingY = nextYOffset;
                    for (Module.Setting setting : module.getSettings()) {
                        int sliderX = colX + 8;
                        int sliderWidth = COLUMN_WIDTH - 16;
                        
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
