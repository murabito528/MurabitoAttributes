package com.murabito.murabitoattributesmod.damage.stages;

import com.murabito.murabitoattributesmod.attributes.CustomAttributes;
import com.murabito.murabitoattributesmod.damage.*;
import com.murabito.murabitoattributesmod.util.Util;

public class DefenseStage implements DamageStage {
    @Override
    public boolean apply(HitData hitData) {
        /*防御力(アーマー)による物理軽減はバニラ or apothic attributesに任せる*/
        /*アーマー以外の物理ダメージ軽減はここでやる*/
        double physReduction=Util.getAttributeValueOrZero(hitData.target , CustomAttributes.PHYS_REDUCTION.get());
        double lightningResistance= Util.getAttributeValueOrZero(hitData.target , CustomAttributes.LIGHTNING_RESISTANCE.get());
        double coldResistance= Util.getAttributeValueOrZero(hitData.target , CustomAttributes.COLD_RESISTANCE.get());
        double fireResistance= Util.getAttributeValueOrZero(hitData.target , CustomAttributes.FIRE_RESISTANCE.get());
        double chaosResistance= Util.getAttributeValueOrZero(hitData.target , CustomAttributes.CHAOS_RESISTANCE.get());

        double lightningResistanceMax= Util.getAttributeValueOrZero(hitData.target , CustomAttributes.LIGHTNING_RESISTANCE_MAX.get());
        double coldResistanceMax= Util.getAttributeValueOrZero(hitData.target , CustomAttributes.COLD_RESISTANCE_MAX.get());
        double fireResistanceMax= Util.getAttributeValueOrZero(hitData.target , CustomAttributes.FIRE_RESISTANCE_MAX.get());
        double chaosResistanceMax= Util.getAttributeValueOrZero(hitData.target , CustomAttributes.CHAOS_RESISTANCE_MAX.get());

        lightningResistance=Math.min(lightningResistance,lightningResistanceMax);
        coldResistance=Math.min(coldResistance,coldResistanceMax);
        fireResistance=Math.min(fireResistance,fireResistanceMax);
        chaosResistance=Math.min(chaosResistance,chaosResistanceMax);

        DamageLog.log(hitData,String.format("[Defence]物理:%.1f%% 雷:%.1f%% 冷気:%.1f%% 火:%.1f%% 混沌:%.1f%%",
                physReduction*100, lightningResistance*100, coldResistance*100, fireResistance*100, chaosResistance*100));

        hitData.postMitigationTotalDamages=hitData.preMitigationTotalDamages.clone();

        hitData.postMitigationTotalDamages.merge(ModDamageType.PHYSICAL, 1-physReduction, (oldVal, newVal) -> oldVal * newVal);
        hitData.postMitigationTotalDamages.merge(ModDamageType.LIGHTNING, 1-lightningResistance, (oldVal, newVal) -> oldVal * newVal);
        hitData.postMitigationTotalDamages.merge(ModDamageType.COLD, 1-coldResistance, (oldVal, newVal) -> oldVal * newVal);
        hitData.postMitigationTotalDamages.merge(ModDamageType.FIRE, 1-fireResistance, (oldVal, newVal) -> oldVal * newVal);
        hitData.postMitigationTotalDamages.merge(ModDamageType.CHAOS, 1-chaosResistance, (oldVal, newVal) -> oldVal * newVal);

        return true;
    }
}
