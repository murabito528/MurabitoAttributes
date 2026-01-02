package com.murabito.murabitoattributesmod.potion.effects;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ChillEffect extends MobEffect {
    public ChillEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);

        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                "b5d3c7f0-1f0e-4c5a-9b0a-11b1f92c7d01",
                -0.01,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
        this.addAttributeModifier(
                Attributes.ATTACK_SPEED,
                "847af756-3cbb-41c6-8847-1947a941582c",
                -0.01,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 4 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) return;

        if (!(entity.level() instanceof ServerLevel server)) return;

        double x = entity.getX();
        double y = entity.getY() + entity.getBbHeight() * 0.6;
        double z = entity.getZ();

        int count = 1 + Math.min(amplifier/4,10); // レベルで量UP（例）
        server.sendParticles(
                ParticleTypes.SNOWFLAKE,
                x, y, z,
                count,
                entity.getBbWidth() * 0.3, entity.getBbHeight() * 0.3, entity.getBbWidth() * 0.3, // 拡散
                0.01 // 速度
        );
    }
}
