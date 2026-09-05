package com.trevorschoeny.offshore.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** YACL screen: one tab, one toggle per feature. */
public final class OffshoreConfigScreen {

    private OffshoreConfigScreen() {}

    private static final String SERVER = " On a dedicated server this is the server's setting.";

    public static Screen create(Screen parent) {
        OptionGroup.Builder group = OptionGroup.createBuilder()
                .name(Component.literal("Offshore"))
                .description(OptionDescription.of(Component.literal(
                        "Boats made less brittle. Each feature has its own switch.")));

        group.option(toggle("eject", "Eject",
                "Shift-right-click a mob sitting in a boat to let it out. Other players are never ejected." + SERVER));
        group.option(toggle("heldItemView", "Held-item view",
                "Keep your held item in view while rowing, instead of it sliding out of frame. Visual only; "
                        + "rowing still blocks attacking and using items as in vanilla."));
        group.option(toggle("stepUp", "Step up",
                "A boat moving into a one-block rise (slab, stair, block edge) steps up instead of stopping." + SERVER));
        group.option(toggle("horsesInBoats", "Horses in boats",
                "Horses, donkeys, mules, camels and llamas can ride a boat. A horse takes both seats, so tow the "
                        + "boat with a lead." + SERVER));
        group.option(toggle("boatHealthHud", "Boat health HUD",
                "Show the hull's remaining health above the hotbar while riding. Off by default."));
        group.option(toggle("noDrift", "No drift on dismount",
                "The boat stops where you leave it instead of sliding on." + SERVER));

        ConfigCategory features = ConfigCategory.createBuilder()
                .name(Component.literal("Features"))
                .group(group.build())
                .build();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Offshore"))
                .category(features)
                .build()
                .generateScreen(parent);
    }

    private static Option<Boolean> toggle(String key, String name, String description) {
        return Option.<Boolean>createBuilder()
                .name(Component.literal(name))
                .description(OptionDescription.of(Component.literal(description)))
                .binding(OffshoreConfig.DEFAULTS.get(key), () -> OffshoreConfig.get(key), v -> OffshoreConfig.set(key, v))
                .controller(BooleanControllerBuilder::create)
                .build();
    }
}
