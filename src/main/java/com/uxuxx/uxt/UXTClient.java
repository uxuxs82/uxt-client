package com.uxuxx.uxt;

import com.uxuxx.uxt.gui.ClickGUI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class UXTClient implements ClientModInitializer {
    
    public static ModuleManager moduleManager;
    public static ClickGUI clickGUI;
    
    private static KeyBinding guiKey;
    
    @Override
    public void onInitializeClient() {
        moduleManager = new ModuleManager();
        clickGUI = new ClickGUI();
        
        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Open GUI",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F3,
            "UXT"
        ));
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            moduleManager.onTick(client);
            
            while (guiKey.wasPressed()) {
                client.openScreen(clickGUI);
            }
        });
    }
}
