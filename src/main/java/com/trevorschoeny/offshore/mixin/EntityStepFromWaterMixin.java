package com.trevorschoeny.offshore.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import com.trevorschoeny.offshore.config.OffshoreConfig;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Step up, the water half. Vanilla's step logic in {@code Entity.collide}
 * only tries a step when the entity is on the ground or landing, and a
 * floating boat is neither, so leaving the water onto a shore block only
 * worked when the hull happened to touch bottom. This wraps that one
 * {@code onGround()} read and answers yes for a boat that is in water, so
 * the same vanilla step search runs from the surface. Every other entity
 * gets vanilla's answer.
 */
@Mixin(Entity.class)
public abstract class EntityStepFromWaterMixin {

    @WrapOperation(
            method = "collide",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;onGround()Z"))
    private boolean offshore$floatingCountsAsGrounded(Entity self, Operation<Boolean> original) {
        boolean grounded = original.call(self);
        return grounded || (OffshoreConfig.stepUp() && self instanceof AbstractBoat && self.isInWater());
    }
}
