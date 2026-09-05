package com.trevorschoeny.offshore.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/** ModMenu entry point: opens the Offshore config screen from the mods list. */
public final class OffshoreConfigModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return OffshoreConfigScreen::create;
    }
}
