package com.vovxb.badbreath.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class BadBreathEffect extends StatusEffect {
    public BadBreathEffect() {
        super(StatusEffectCategory.HARMFUL, 0x00FF00); // green icon
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        World world = entity.getWorld();
        StatusEffectInstance instance = entity.getStatusEffect(com.vovxb.badbreath.BadBreathMod.BAD_BREATH);
        if (instance == null) return;

        int ticksElapsed = amplifier; // we use amplifier to track time

        // damage players wit your stinky bad breath ewwww
        float damage = 1.0F + (ticksElapsed / 600);      // +1 damage every 30s
        double radius = 2.0D + (ticksElapsed / 1200.0D); // +1 block every 60s you can custmize the radius and speed if you want

        if (!world.isClient) {
            world.getOtherEntities(entity, entity.getBoundingBox().expand(radius))
                .forEach(e -> {
                    if (e instanceof LivingEntity living && living != entity) {
                        living.damage(net.minecraft.entity.damage.DamageSource.magic(entity), damage);
                    }
                });
        } else {
            // Green particles for da bad breath stinky
            world.addParticle(ParticleTypes.ITEM_SLIME,
                    entity.getX(),
                    entity.getEyeY(),
                    entity.getZ(),
                    (world.random.nextDouble() - 0.5) * 0.2,
                    0.05,
                    (world.random.nextDouble() - 0.5) * 0.2);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true; // always tick
    }
}
