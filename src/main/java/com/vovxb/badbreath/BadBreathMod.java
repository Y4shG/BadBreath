package com.vovxb.badbreath;

import com.vovxb.badbreath.effect.BadBreathEffect;
import com.vovxb.badbreath.event.FoodListener;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BadBreathMod implements ModInitializer {
    public static final String MODID = "badbreath";
    public static net.minecraft.registry.entry.RegistryEntry<StatusEffect> BAD_BREATH;

    @Override
    public void onInitialize() {
        // hmmm effecty boy give me bad stinky breath
        BAD_BREATH = Registries.STATUS_EFFECT.register(
            new Identifier(MODID, "bad_breath"),
            new BadBreathEffect()
        );

        // listner lol
        FoodListener.register();
    }
}
