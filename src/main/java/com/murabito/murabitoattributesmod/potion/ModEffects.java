package com.murabito.murabitoattributesmod.potion;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import com.murabito.murabitoattributesmod.potion.effects.ChillEffect;
import com.murabito.murabitoattributesmod.potion.effects.ShockEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MurabitoAttributesMod.MODID);

    public static final RegistryObject<MobEffect> SHOCK =
            EFFECTS.register("shock", () -> new ShockEffect(MobEffectCategory.HARMFUL, 0xFFFF00));

    public static final RegistryObject<MobEffect> CHILL =
            EFFECTS.register("chill", () -> new ChillEffect(MobEffectCategory.HARMFUL, 0x66CCFF));
}
