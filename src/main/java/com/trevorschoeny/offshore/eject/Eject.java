package com.trevorschoeny.offshore.eject;

import com.trevorschoeny.offshore.config.OffshoreConfig;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

/**
 * Eject: shift-right-click a mob that is sitting in a boat and it gets out.
 *
 * <p>The click lands on the passenger, not the hull, so "whatever you're
 * looking at" is literal: aim at the mob to eject it, aim at a chest boat's
 * hull to open its chest as in vanilla. Hooked through Fabric's entity-use
 * event, which fires before the mob's own interact (a villager's trade
 * screen, say), so a sneaking click never reaches it. Both sides run the
 * callback: the client returns SUCCESS so the arm swings and the packet goes
 * out; the server does the dismount.
 */
public final class Eject {

    private Eject() {}

    public static void register() {
        UseEntityCallback.EVENT.register(Eject::onUse);
    }

    private static InteractionResult onUse(Player player, Level level, InteractionHand hand,
                                           Entity target, EntityHitResult hit) {
        if (!OffshoreConfig.eject()) return InteractionResult.PASS;
        // Vanilla's own "sneak-use" predicate (shift, or crawling).
        if (!player.isSecondaryUseActive() || player.isSpectator()) return InteractionResult.PASS;
        // Only a passenger of a boat, and never another player.
        if (!(target.getVehicle() instanceof AbstractBoat) || target instanceof Player) return InteractionResult.PASS;

        // stopRiding runs vanilla's dismount placement, so the mob lands beside the boat, not in it.
        if (!level.isClientSide()) target.stopRiding();
        return InteractionResult.SUCCESS;
    }
}
