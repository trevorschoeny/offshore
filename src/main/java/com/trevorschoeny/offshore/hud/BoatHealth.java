package com.trevorschoeny.offshore.hud;

import com.trevorschoeny.offshore.config.OffshoreConfig;
import com.trevlar.menukit.core.PanelStyle;
import com.trevlar.menukit.hud.MKHudAnchor;
import com.trevlar.menukit.hud.MKHudPanel;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;

/**
 * HUD: while riding a boat, a bar above the hotbar shows how much hull is
 * left. Vanilla tracks boat damage as a number that climbs by 10 per hit and
 * destroys the boat past 40 ({@code VehicleEntity.hurtServer}); it is synced
 * to clients already, so health is just {@code 1 - damage / 40}. Vanilla also
 * decays the damage back toward zero each tick, which is why the bar refills
 * on its own after a hit.
 */
public final class BoatHealth {

    private BoatHealth() {}

    // VehicleEntity destroys the boat when getDamage() > 40.
    private static final float BREAK_AT = 40.0f;
    private static final int WIDTH = 182;   // the full hotbar width

    public static void register() {
        MKHudPanel.builder("offshore:boat-health")
                .anchor(MKHudAnchor.BOTTOM_CENTER, 0, -50)   // spans the hotbar, just above it
                .autoSize().padding(0)
                .style(PanelStyle.NONE)
                .hideInScreen()
                .showWhen(BoatHealth::isActive)
                .bar(0, 0, WIDTH, 7)
                    .value(BoatHealth::health)
                    .color(0xFFC08040)   // plank brown
                    .label(() -> Component.literal("Hull"))
                    .done()
                .build();
    }

    private static AbstractBoat boat() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null && mc.player.getVehicle() instanceof AbstractBoat b ? b : null;
    }

    private static boolean isActive() {
        return OffshoreConfig.boatHealthHud() && boat() != null;
    }

    private static double health() {
        AbstractBoat b = boat();
        return b == null ? 0.0 : Math.max(0.0, 1.0 - b.getDamage() / BREAK_AT);
    }
}
