package com.murabito.murabitoattributesmod.potion.effects;

import com.murabito.murabitoattributesmod.attributes.CustomAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ShockEffect extends MobEffect {
    public ShockEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        this.addAttributeModifier(
                CustomAttributes.DAMAGE_TAKEN_MULTI.get(),
                "12d50d8d-ab77-4e1a-8ce9-6c8b9d685cca",
                0.01,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }
}
