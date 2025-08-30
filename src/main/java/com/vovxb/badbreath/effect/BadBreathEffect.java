package com.vovxb.badbreath.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.util.math.Box;
import net.minecraft.entity.damage.DamageSource;

public class BadBreathEffect extends StatusEffect {

    public BadBreathEffect() {
        super(StatusEffectCategory.HARMFUL, 0x00FF00); // green color
    }

    // Called every tick on entity with this effect
    public void applyUpdateEffect(LivingEntity entity, StatusEffectInstance effect) {
        int ticksElapsed = effect.getAmplifier();
        float damage = 1.0F + (ticksElapsed / 600.0F);
        double radius = 2.0D + (ticksElapsed / 1200.0D);

        World world = entity.getWorld();

        if (!world.isClient) {
            // Damage nearby living entities
            Box box = entity.getBoundingBox().expand(radius);
            world.getOtherEntities(entity, box).forEach(e -> {
                if (e instanceof LivingEntity living && living != entity) {
                    living.damage(DamageSource.indirectMagic(entity, entity), damage);
                }
            });
        } else {
            // Spawn green slime particles on client
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
