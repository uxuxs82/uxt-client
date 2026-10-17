package com.uxuxx.uxt;

import net.minecraft.client.MinecraftClient;
import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    
    private String name;
    private boolean enabled = false;
    protected List<Setting> settings = new ArrayList<>();
    
    public Module(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void toggle() {
        enabled = !enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }
    
    public List<Setting> getSettings() {
        return settings;
    }
    
    public void onEnable() {}
    public void onDisable() {}
    public void onTick(MinecraftClient client) {}
    
    public static class Setting {
        private String name;
        private double value;
        private double min;
        private double max;
        private double increment;
        
        public Setting(String name, double value, double min, double max, double increment) {
            this.name = name;
            this.value = value;
            this.min = min;
            this.max = max;
            this.increment = increment;
        }
        
        public String getName() { return name; }
        public double getValue() { return value; }
        public double getMin() { return min; }
        public double getMax() { return max; }
        public double getIncrement() { return increment; }
        
        public void setValue(double value) {
            this.value = Math.max(min, Math.min(max, value));
        }
        
        public void increment() {
            setValue(value + increment);
        }
        
        public void decrement() {
            setValue(value - increment);
        }
    }
}
