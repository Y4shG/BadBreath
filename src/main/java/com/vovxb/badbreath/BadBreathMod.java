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
    public static final StatusEffect BAD_BREATH = new BadBreathEffect();

    @Override
    public void onInitialize() {
        // hmmm effecty boy give me bad stinky breath
        Registry.register(Registries.STATUS_EFFECT, new Identifier(MODID, "bad_breath"), BAD_BREATH);

        // listner lol
        FoodListener.register();
    }
}
