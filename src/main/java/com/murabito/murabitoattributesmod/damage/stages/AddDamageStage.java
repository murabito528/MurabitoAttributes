package com.murabito.murabitoattributesmod.damage.stages;

import com.murabito.murabitoattributesmod.attributes.CustomAttributes;
import com.murabito.murabitoattributesmod.damage.*;
import com.murabito.murabitoattributesmod.util.Util;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.Random;
import java.util.function.Supplier;

public class AddDamageStage implements DamageStage {
    @Override
    public boolean apply(HitData hitData) {
        if (hitData.attacker == null) return true;

        applyAddDamage(hitData, CustomAttributes.PHYS_ADD_MIN, CustomAttributes.PHYS_ADD_MAX, ModDamageType.PHYSICAL, "[AddDamage]追加物理ダメージ");
        applyAddDamage(hitData, CustomAttributes.FIRE_ADD_MIN, CustomAttributes.FIRE_ADD_MAX, ModDamageType.FIRE, "[AddDamage]追加火ダメージ");
        applyAddDamage(hitData, CustomAttributes.COLD_ADD_MIN, CustomAttributes.COLD_ADD_MAX, ModDamageType.COLD, "[AddDamage]追加冷気ダメージ");
        applyAddDamage(hitData, CustomAttributes.LIGHTNING_ADD_MIN, CustomAttributes.LIGHTNING_ADD_MAX, ModDamageType.LIGHTNING, "[AddDamage]追加雷ダメージ");
        applyAddDamage(hitData, CustomAttributes.CHAOS_ADD_MIN, CustomAttributes.CHAOS_ADD_MAX, ModDamageType.CHAOS, "[AddDamage]追加混沌ダメージ");

        return true;
    }

    private void applyAddDamage(HitData hitData, Supplier<Attribute> minAttr, Supplier<Attribute> maxAttr, ModDamageType type, String logPrefix) {
        double min = Util.getAttributeValueOrZero(hitData.attacker, minAttr.get());
        if (min <= 0) return;

        double max = Util.getAttributeValueOrZero(hitData.attacker, maxAttr.get());
        if (max < min) max = min;

        double randomValue = hitData.attacker.getRandom().nextDouble();
        double damage = min + (max - min) * randomValue;

        damage = Math.floor(damage * 100) / 100;

        hitData.components.add(new DamageComponent(damage, type));
        DamageLog.log(hitData, logPrefix + ":" + damage);
    }
}
