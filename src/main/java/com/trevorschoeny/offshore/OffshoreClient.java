package com.trevorschoeny.offshore;

import com.trevorschoeny.offshore.config.OffshoreConfig;
import com.trevorschoeny.offshore.hud.BoatHealth;

import net.fabricmc.api.ClientModInitializer;

/** Client entry point: the boat health bar. Held-item view is a client mixin and needs no registration. */
public class OffshoreClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        OffshoreConfig.load();
        BoatHealth.register();
        Offshore.LOGGER.info("[offshore] client init: health HUD {}, held-item view {}",
                OffshoreConfig.boatHealthHud() ? "on" : "off",
                OffshoreConfig.heldItemView() ? "on" : "off");
    }
}
