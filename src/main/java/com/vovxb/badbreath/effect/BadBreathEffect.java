
package com.vovxb.badbreath.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class BadBreathEffect extends StatusEffect {
    public BadBreathEffect() {
        super(StatusEffectCategory.HARMFUL, 0x00FF00); // green icon
    }

    // Called from tick event in BadBreathMod
    public static void spreadBadBreath(PlayerEntity player) {
        World world = player.getWorld();
        // Spawn green particles from mouth
        if (world.isClient) {
            world.addParticle(ParticleTypes.ITEM_SLIME,
                player.getX(),
                player.getEyeY(),
                player.getZ(),
                (world.random.nextDouble() - 0.5) * 0.2,
                0.05,
                (world.random.nextDouble() - 0.5) * 0.2);
        }
        // Harm nearby entities
        if (!world.isClient) {
            double radius = 2.0D;
            world.getOtherEntities(player, player.getBoundingBox().expand(radius)).forEach(e -> {
                if (e instanceof LivingEntity living && living != player) {
                    living.damage(living.getDamageSources().magic(), 1.0F);
                }
            });
        }
    }
}
