package com.vovxb.badbreath.event;

import com.vovxb.badbreath.BadBreathMod;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;

public class FoodListener {

    private static int badBreathTicks = 0;

    public static void register() {
        UseItemCallback.EVENT.register((PlayerEntity player, net.minecraft.world.World world, Hand hand) -> {
            ItemStack stack = player.getStackInHand(hand);

            // Check if item is food
            if (stack.getItem() instanceof FoodItem) {
                badBreathTicks = 0;
                player.addStatusEffect(new StatusEffectInstance(
                        BadBreathMod.BAD_BREATH,
                        Integer.MAX_VALUE,
                        0,
                        false,
                        true
                ));
            }

            // Water potion cures bad breath
            if (stack.isOf(Items.POTION)) {
                if (PotionUtil.getPotion(stack) == Potions.WATER) {
                    player.removeStatusEffect(BadBreathMod.BAD_BREATH);
                    badBreathTicks = 0;
                }
            }

            return ActionResult.PASS;
        });

        // Each tick increases amplifier
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
