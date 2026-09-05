package com.trevorschoeny.offshore.mixin;

import com.trevorschoeny.offshore.config.OffshoreConfig;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The three tick-path features, all on the boat itself.
 *
 * <p><b>Step up.</b> {@code Entity.maxUpStep()} is 0 for anything that isn't a
 * living entity, and {@code AbstractBoat} never overrides it, so a boat stops
 * dead at a slab. Vanilla's own step logic in {@code Entity.collide} does the
 * rest once the height is non-zero: it fires when the boat is on the ground
 * (or landing) and blocked sideways. Adding the override here is not an
 * overwrite; there is nothing on the boat to overwrite.
 *
 * <p><b>No drift.</b> Same trick on {@code removePassenger}: when the last
 * rider leaves, drop the horizontal velocity. Runs on both sides, since the
 * client removes the passenger too when the packet lands.
 *
 * <p><b>Horses.</b> Two gates keep a horse out of a boat: it is wider than
 * the hull ({@code hasEnoughSpaceFor}) and, once in, a second seat would still
 * be open. A horse passes the width check, and a boat carrying one reports a
 * single seat, so it fills the boat. That also means the player cannot board
 * with it; tow the boat instead (Move boats, or a lead).
 */
@Mixin(AbstractBoat.class)
public abstract class AbstractBoatMixin extends VehicleEntity {

    private AbstractBoatMixin(EntityType<?> type, Level level) { super(type, level); }

    // ── Step up ───────────────────────────────────────────────────────────
    @Override
    public float maxUpStep() {
        return OffshoreConfig.stepUp() ? 1.0f : super.maxUpStep();
    }

    // ── No drift ──────────────────────────────────────────────────────────
    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (OffshoreConfig.noDrift() && passenger instanceof Player && !this.isVehicle()) {
            Vec3 v = this.getDeltaMovement();
            this.setDeltaMovement(0.0, v.y, 0.0);   // keep gravity and buoyancy, kill the slide
        }
    }

    // ── Horses ────────────────────────────────────────────────────────────
    @Inject(method = "hasEnoughSpaceFor", at = @At("HEAD"), cancellable = true)
    private void offshore$horsesFit(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (OffshoreConfig.horsesInBoats() && entity instanceof AbstractHorse) cir.setReturnValue(true);
    }

    @Inject(method = "getMaxPassengers", at = @At("HEAD"), cancellable = true)
    private void offshore$horseTakesBothSeats(CallbackInfoReturnable<Integer> cir) {
        if (!OffshoreConfig.horsesInBoats()) return;
        for (Entity p : this.getPassengers()) {
            if (p instanceof AbstractHorse) { cir.setReturnValue(1); return; }
        }
    }
}
