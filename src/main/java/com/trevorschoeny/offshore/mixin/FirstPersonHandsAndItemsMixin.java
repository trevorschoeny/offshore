package com.trevorschoeny.offshore.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import com.trevorschoeny.offshore.config.OffshoreConfig;

import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.client.player.LocalPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Held-item view. {@code LocalPlayer.rideTick} flags the hands busy whenever
 * a rowing key is down, and {@code FirstPersonHandsAndItems.tick} answers by
 * sliding both hands out of frame. That flag is also what blocks attacking
 * and item use while rowing ({@code Minecraft.startAttack} /
 * {@code startUseItem}), so this wraps only the hand-height read of it: the
 * item stays in view, the gameplay gate stays vanilla.
 */
@Mixin(FirstPersonHandsAndItems.class)
public abstract class FirstPersonHandsAndItemsMixin {

    @WrapOperation(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isHandsBusy()Z"))
    private boolean offshore$keepItemInView(LocalPlayer player, Operation<Boolean> original) {
        return OffshoreConfig.heldItemView() ? false : original.call(player);
    }
}
