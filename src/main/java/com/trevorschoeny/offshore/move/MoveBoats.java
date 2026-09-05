package com.trevorschoeny.offshore.move;

import com.trevorschoeny.offshore.config.OffshoreConfig;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.entity.vehicle.boat.AbstractChestBoat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

/**
 * Move without breaking: shift-right-click an empty boat, empty-handed, and
 * it tows behind you; shift-right-click it again to let go.
 *
 * <p>Boats are leashable in vanilla since 1.21.5, so the whole "follow the
 * player" physics already exists. This just attaches the boat to the player
 * with no lead item involved, and detaches it without dropping one. Every
 * other rule (snap distance, elasticity, what happens when you log out) is
 * vanilla's leash behaviour.
 *
 * <p>Excluded on purpose: chest boats, because shift-right-click on the hull
 * opens the chest in vanilla and that interaction wins. Tow those with a real
 * lead. Also excluded: a boat with anyone in it, which is a boat someone is
 * using.
 */
public final class MoveBoats {

    private MoveBoats() {}

    public static void register() {
        UseEntityCallback.EVENT.register(MoveBoats::onUse);
    }

    private static InteractionResult onUse(Player player, Level level, InteractionHand hand,
                                           Entity target, EntityHitResult hit) {
        if (!OffshoreConfig.moveBoats()) return InteractionResult.PASS;
        if (!player.isSecondaryUseActive() || player.isSpectator()) return InteractionResult.PASS;
        if (!(target instanceof AbstractBoat boat) || boat instanceof AbstractChestBoat) return InteractionResult.PASS;
        if (boat.isVehicle() || !player.getItemInHand(hand).isEmpty()) return InteractionResult.PASS;

        if (boat.isLeashed()) {
            // Only the holder lets go; someone else's tow is not ours to cut.
            if (boat.getLeashHolder() != player) return InteractionResult.PASS;
            if (!level.isClientSide()) boat.removeLeash();   // no lead item drops
            return InteractionResult.SUCCESS;
        }
        if (!level.isClientSide()) boat.setLeashedTo(player, true);
        return InteractionResult.SUCCESS;
    }
}
