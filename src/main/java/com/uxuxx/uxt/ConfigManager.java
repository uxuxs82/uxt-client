package com.uxuxx.uxt;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("uxt-client.cfg");
    private static boolean loading = false;
    private static boolean dirty = false;
    private static long lastSave = 0;

    public static void markDirty() {
        if (!loading) {
            dirty = true;
        }
    }

    public static void tick() {
        if (dirty && System.currentTimeMillis() - lastSave >= 1000) {
            save();
        }
    }

    public static void save() {
        if (UXTClient.moduleManager == null) return;

        List<String> lines = new ArrayList<>();
        for (Module module : UXTClient.moduleManager.getModules()) {
            lines.add("m|" + module.getName() + "|" + module.isEnabled());
            for (Module.Setting setting : module.getSettings()) {
                lines.add("s|" + module.getName() + "|" + setting.getName() + "|" + setting.getValue());
            }
        }

        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.write(CONFIG_PATH, lines, StandardCharsets.UTF_8);
            dirty = false;
            lastSave = System.currentTimeMillis();
        } catch (IOException e) {
        }
    }

    public static void load() {
        loading = true;
        try {
            if (!Files.exists(CONFIG_PATH)) return;
            for (String line : Files.readAllLines(CONFIG_PATH, StandardCharsets.UTF_8)) {
                applyLine(line);
            }
        } catch (IOException e) {
        } finally {
            loading = false;
        }
    }

    private static void applyLine(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 3) return;

        if (parts[0].equals("m")) {
            Module module = findModule(parts[1]);
            if (module == null) return;
            boolean target = Boolean.parseBoolean(parts[2]);
            if (module.isEnabled() != target) {
                module.toggle();
            }
        } else if (parts[0].equals("s") && parts.length >= 4) {
            Module module = findModule(parts[1]);
            if (module == null) return;
            for (Module.Setting setting : module.getSettings()) {
                if (!setting.getName().equals(parts[2])) continue;
                try {
                    setting.setValue(Double.parseDouble(parts[3]));
                } catch (NumberFormatException ignored) {
                }
                return;
            }
        }
    }

    private static Module findModule(String name) {
        if (UXTClient.moduleManager == null) return null;
        for (Module module : UXTClient.moduleManager.getModules()) {
            if (module.getName().equals(name)) return module;
        }
        return null;
    }
}
