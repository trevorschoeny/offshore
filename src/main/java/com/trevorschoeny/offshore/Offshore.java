package com.trevorschoeny.offshore;

import com.trevorschoeny.offshore.config.OffshoreConfig;
import com.trevorschoeny.offshore.eject.Eject;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common entry point, runs on both physical sides. Loads the config so the
 * switches are known before the first boat ticks, and registers eject through
 * Fabric's entity-use event. The tick-path features (step up, horses, no
 * drift) are mixins and need no registration.
 */
public class Offshore implements ModInitializer {

    public static final String MOD_ID = "offshore";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        OffshoreConfig.load();
        Eject.register();
        LOGGER.info("[offshore] init: eject {}, step up {}, horses {}, no drift {}",
                on(OffshoreConfig.eject()), on(OffshoreConfig.stepUp()),
                on(OffshoreConfig.horsesInBoats()), on(OffshoreConfig.noDrift()));
    }

    private static String on(boolean b) { return b ? "on" : "off"; }
}
