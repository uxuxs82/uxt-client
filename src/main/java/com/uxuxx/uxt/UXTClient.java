package com.uxuxx.uxt;

import com.uxuxx.uxt.gui.ClickGUI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class UXTClient implements ClientModInitializer {
    
    public static ModuleManager moduleManager;
    public static ClickGUI clickGUI;
    
    private static KeyBinding guiKeyX;
    private static KeyBinding guiKeyZ;
    
    @Override
    public void onInitializeClient() {
        moduleManager = new ModuleManager();
        clickGUI = new ClickGUI();
        
        guiKeyX = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Open GUI (X)",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "UXT"
        ));
        
        guiKeyZ = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "Open GUI (Z)",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "UXT"
        ));
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            moduleManager.onTick(client);
            
            while (guiKeyX.wasPressed() || guiKeyZ.wasPressed()) {
                client.openScreen(clickGUI);
            }
        });
    }
}
