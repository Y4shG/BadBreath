package com.vovxb.badbreath;

import com.vovxb.badbreath.effect.BadBreathEffect;
import com.vovxb.badbreath.event.FoodListener;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.entity.effect.StatusEffect;

public class BadBreathMod implements ModInitializer {
    public static final String MODID = "badbreath";

    public static StatusEffect BAD_BREATH;

    @Override
    public void onInitialize() {
        // Register bad breath status effect
        BAD_BREATH = Registry.register(
                Registries.STATUS_EFFECT,
                new Identifier(MODID, "bad_breath"),
                new BadBreathEffect()
        );

        // Register the food and tick listeners
        FoodListener.register();
    }
}
