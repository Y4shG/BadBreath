package com.vovxb.badbreath.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;
import net.minecraft.world.ServerWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Box;
import net.minecraft.entity.damage.DamageSource;

public class BadBreathEffect extends StatusEffect {

    public BadBreathEffect() {
        super(StatusEffectCategory.HARMFUL, 0x00FF00); // green color
    }

    // Updated for 1.21.8 API
    @Override
    public void applyUpdateEffect(LivingEntity entity, StatusEffectInstance effect) {
        int ticksElapsed = effect.getAmplifier();

        // Scaling damage and radius
        float damage = 1.0F + (ticksElapsed / 600.0F);
        double radius = 2.0D + (ticksElapsed / 1200.0D);

        World world = entity.getWorld();

        if (!world.isClient) {
            Box box = entity.getBoundingBox().expand(radius);
            world.getOtherEntities(entity, box).forEach(e -> {
                if (e instanceof LivingEntity living && living != entity) {
                    // Use standard MAGIC damage
                    living.damage(DamageSource.MAGIC, damage);
                }
            });
        } else {
            // Spit particles (green slime)
            for (int i = 0; i < 5; i++) {
                world.addParticle(
                        ParticleTypes.ITEM_SLIME,
                        entity.getX() + (world.random.nextDouble() - 0.5) * 0.5,
                        entity.getEyeY(),
                        entity.getZ() + (world.random.nextDouble() - 0.5) * 0.5,
                        (world.random.nextDouble() - 0.5) * 0.02,
                        0.05,
                        (world.random.nextDouble() - 0.5) * 0.02
                );
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true; // always tick
    }
}
