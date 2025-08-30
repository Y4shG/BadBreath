package com.vovxb.badbreath.event;

import com.vovxb.badbreath.BadBreathMod;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.TypedActionResult;

public class FoodListener {
    private static int badBreathTicks = 0;

    public static void register() {
        // PERMANANT BAD BREATH EWWWWWW
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);

            if (stack.isFood()) {
                badBreathTicks = 0; // reset timer
                player.addStatusEffect(new StatusEffectInstance(BadBreathMod.BAD_BREATH, Integer.MAX_VALUE, 0, false, true));
            }

            // drinking tasty h2O is da cure
            if (stack.isOf(Items.POTION) &&
                stack.getOrCreateNbt().getString("Potion").equals("minecraft:water")) {
                player.removeStatusEffect(BadBreathMod.BAD_BREATH);
                badBreathTicks = 0;
            }

            return TypedActionResult.pass(stack);
        });

        // every tick makes your breath worseeeee
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            world.getPlayers().forEach(player -> {
                if (player.hasStatusEffect(BadBreathMod.BAD_BREATH)) {
                    badBreathTicks++;
                    player.addStatusEffect(new StatusEffectInstance(
                            BadBreathMod.BAD_BREATH,
                            Integer.MAX_VALUE,
                            badBreathTicks, // amplifier stores time
                            false,
                            true));
                }
            });
        });
    }
}
