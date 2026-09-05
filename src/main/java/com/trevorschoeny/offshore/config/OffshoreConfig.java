package com.trevorschoeny.offshore.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import com.trevorschoeny.offshore.Offshore;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Offshore config: one switch per feature, all default on, no master switch
 * (plan.md). Same static-field + JSON shape as the other house mods;
 * persisted to {@code config/offshore/config.json}.
 *
 * <p>Server-side switches (eject, step up, move, horses, no drift) are read
 * where the boat ticks or is clicked, so on a dedicated server the server's
 * file decides. Client-side switches (held-item view, health HUD) are per
 * player.
 */
public final class OffshoreConfig {

    private OffshoreConfig() {}

    private static final int CURRENT_VERSION = 1;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // One map, key = JSON key. Keeps load/save to a loop instead of a field per switch.
    private static final Map<String, Boolean> SWITCHES = new LinkedHashMap<>();
    static {
        for (String k : new String[] {
                "eject", "heldItemView", "stepUp", "moveBoats", "horsesInBoats", "boatHealthHud", "noDrift"}) {
            SWITCHES.put(k, true);
        }
    }

    private static boolean loaded = false;

    private static Path filePath() {
        return FabricLoader.getInstance().getConfigDir().resolve("offshore").resolve("config.json");
    }

    public static void load() {
        if (loaded) return;
        loaded = true;
        Path path = filePath();
        if (!Files.exists(path)) {
            Offshore.LOGGER.info("[config] no config at {}, using defaults", path);
            return;
        }
        try {
            JsonObject root = JsonParser.parseString(Files.readString(path)).getAsJsonObject();
            for (String key : SWITCHES.keySet()) {
                if (root.has(key) && root.get(key).isJsonPrimitive()) {
                    SWITCHES.put(key, root.get(key).getAsBoolean());
                }
            }
            Offshore.LOGGER.info("[config] loaded from {}", path);
        } catch (IOException | JsonSyntaxException | IllegalStateException e) {
            Offshore.LOGGER.error("[config] failed to read {}, using defaults", path, e);
        }
    }

    private static void save() {
        Path path = filePath();
        try {
            Files.createDirectories(path.getParent());
            JsonObject root = new JsonObject();
            root.addProperty("version", CURRENT_VERSION);
            SWITCHES.forEach(root::addProperty);
            Files.writeString(path, GSON.toJson(root));
        } catch (IOException e) {
            Offshore.LOGGER.error("[config] failed to write {}, changes won't persist", path, e);
        }
    }

    static boolean get(String key) { return SWITCHES.get(key); }
    static void set(String key, boolean v) { SWITCHES.put(key, v); save(); }

    // Shift-right-click a mob in a boat to let it out.
    public static boolean eject()         { return get("eject"); }
    // Held item stays visible in first person while rowing.
    public static boolean heldItemView()  { return get("heldItemView"); }
    // Boats climb one-block rises instead of stopping.
    public static boolean stepUp()        { return get("stepUp"); }
    // Shift-right-click an empty boat, empty-handed, to tow it; again to let go.
    public static boolean moveBoats()     { return get("moveBoats"); }
    // Horses and their kin can ride a boat, taking both seats.
    public static boolean horsesInBoats() { return get("horsesInBoats"); }
    // Hull health bar while riding.
    public static boolean boatHealthHud() { return get("boatHealthHud"); }
    // The boat stops where you leave it.
    public static boolean noDrift()       { return get("noDrift"); }
}
