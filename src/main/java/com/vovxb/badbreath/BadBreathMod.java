
package com.vovxb.badbreath;

import com.vovxb.badbreath.effect.BadBreathEffect;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class BadBreathMod implements ModInitializer {
    public static final String MODID = "badbreath";
    public static RegistryEntry<StatusEffect> BAD_BREATH;

    @Override
    public void onInitialize() {
        BAD_BREATH = Registry.register(
            Registries.STATUS_EFFECT,
            Identifier.of(MODID, "bad_breath"),
            new BadBreathEffect()
        );

        // Give bad breath after eating anything
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.isFood()) {
                player.addStatusEffect(new StatusEffectInstance(
                    BAD_BREATH,
                    20 * 60, // 1 minute duration
                    0,
                    false,
                    true
                ));
            }
            // Remove bad breath if drinking water bottle
            if (stack.isOf(Items.POTION) && stack.getOrCreateNbt().getString("Potion").equals("minecraft:water")) {
                player.removeStatusEffect(BAD_BREATH);
            }
            return ActionResult.PASS;
        });

        // Every tick: apply bad breath effects
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (PlayerEntity player : world.getPlayers()) {
                if (player.hasStatusEffect(BAD_BREATH)) {
                    BadBreathEffect.spreadBadBreath(player);
                }
            }
        });
    }
}
