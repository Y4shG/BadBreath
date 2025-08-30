package com.vovxb.badbreath.event;

import com.vovxb.badbreath.BadBreathMod;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potions;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public class FoodListener {

    private static int badBreathTicks = 0;

    public static void register() {
        // Listen for item usage
        UseItemCallback.EVENT.register((PlayerEntity player, World world, Hand hand) -> {
            ItemStack stack = player.getStackInHand(hand);

            // If item is food, give bad breath
            if (stack.getItem().getFoodComponent() != null) {
                badBreathTicks = 0;
                player.addStatusEffect(new StatusEffectInstance(
                        BadBreathMod.BAD_BREATH,
                        Integer.MAX_VALUE,
                        0,
                        false,
                        true
                ));
            }

            // If water potion, remove bad breath
            if (stack.getItem() instanceof PotionItem potionItem) {
                if (potionItem.getPotion(stack) == Potions.WATER) {
                    player.removeStatusEffect(BadBreathMod.BAD_BREATH);
                    badBreathTicks = 0;
                }
            }

            return ActionResult.PASS;
        });

        // Tick event to increase bad breath over time
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            world.getPlayers().forEach(player -> {
                if (player.hasStatusEffect(BadBreathMod.BAD_BREATH)) {
                    badBreathTicks++;
                    player.addStatusEffect(new StatusEffectInstance(
                            BadBreathMod.BAD_BREATH,
                            Integer.MAX_VALUE,
                            badBreathTicks,
                            false,
                            true
                    ));
                }
            });
        });
    }
}
