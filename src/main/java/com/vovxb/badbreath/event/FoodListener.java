package com.vovxb.badbreath.event;

import com.vovxb.badbreath.BadBreathMod;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potions;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public class FoodListener {

    private static int badBreathTicks = 0;

    public static void register() {
        UseItemCallback.EVENT.register((PlayerEntity player, World world, Hand hand) -> {
            ItemStack stack = player.getStackInHand(hand);

            // Check if the item is food
            FoodComponent food = stack.getItem().getFoodComponent();
            if (food != null) {
                badBreathTicks = 0;
                player.addStatusEffect(new StatusEffectInstance(
                        BadBreathMod.BAD_BREATH,
                        Integer.MAX_VALUE,
                        0,
                        false,
                        true
                ));
            }

            // Check if it’s a water potion
            if (stack.getItem() instanceof PotionItem potionItem) {
                if (potionItem.getPotion(stack) == Potions.WATER) {
                    player.removeStatusEffect(BadBreathMod.BAD_BREATH);
                    badBreathTicks = 0;
                }
            }

            return ActionResult.PASS;
        });

        // Increase bad breath ticks every world tick
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
