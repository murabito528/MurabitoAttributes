package com.murabito.murabitoattributesmod.damage.stages;

import com.murabito.murabitoattributesmod.attributes.CustomAttributes;
import com.murabito.murabitoattributesmod.damage.*;
import com.murabito.murabitoattributesmod.potion.ModEffects;
import com.murabito.murabitoattributesmod.util.Util;

public class ModifierStage implements DamageStage {
    @Override
    public boolean apply(HitData hitData) {
        //DamageLog.log(hitData,"[modifier]未完成、処理なし");

        if (hitData.attacker == null) {
            DamageLog.log(hitData,"[modifier]アタッカーが不在");
            return true;
        }

        if (hitData.components == null || hitData.components.isEmpty()) {
            DamageLog.log(hitData, "[convDamage]ダメージコンポーネントがありません");
            return true;
        }

        double physInc = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.PHYS_DAMAGE_INC.get());
        double lightningInc = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.LIGHTNING_DAMAGE_INC.get());
        double coldInc = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.COLD_DAMAGE_INC.get());
        double fireInc = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.FIRE_DAMAGE_INC.get());
        double chaosInc = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.CHAOS_DAMAGE_INC.get());

        double physMore = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.PHYS_DAMAGE_MORE.get());
        double lightningMore = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.LIGHTNING_DAMAGE_MORE.get());
        double coldMore = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.COLD_DAMAGE_MORE.get());
        double fireMore = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.FIRE_DAMAGE_MORE.get());
        double chaosMore = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.CHAOS_DAMAGE_MORE.get());


        double criticalMultiGlobal = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.CRITICAL_MULTI_GLOBAL.get());
        double criticalMultiSpell = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.CRITICAL_MULTI_SPELL.get());

        double criticalMultiLess = Util.getAttributeValueOrZero(hitData.target , CustomAttributes.CRITICAL_DAMAGE_LESS.get());

        for(DamageComponent dc : hitData.components) {
            if(dc.affectedTypes.contains(ModDamageType.PHYSICAL)) dc.increasedTotal+=physInc;
            if(dc.affectedTypes.contains(ModDamageType.LIGHTNING)) dc.increasedTotal+=lightningInc;
            if(dc.affectedTypes.contains(ModDamageType.COLD)) dc.increasedTotal+=coldInc;
            if(dc.affectedTypes.contains(ModDamageType.FIRE)) dc.increasedTotal+=fireInc;
            if(dc.affectedTypes.contains(ModDamageType.CHAOS)) dc.increasedTotal+=chaosInc;

            //ここのログちゃんと残してない
            //後でちゃんとする
            if(dc.affectedTypes.contains(ModDamageType.PHYSICAL)) dc.base*=1+physMore;
            if(dc.affectedTypes.contains(ModDamageType.LIGHTNING)) dc.base*=1+lightningMore;
            if(dc.affectedTypes.contains(ModDamageType.COLD)) dc.base*=1+coldMore;
            if(dc.affectedTypes.contains(ModDamageType.FIRE)) dc.base*=1+fireMore;
            if(dc.affectedTypes.contains(ModDamageType.CHAOS)) dc.base*=1+chaosMore;

            DamageLog.log(hitData,
                    "[modifier]INC:%s %.2f x%.2f"
                            .formatted(
                                    dc.affectedTypes,
                                    dc.base,
                                    1 + dc.increasedTotal
                            )
            );
            dc.base*=1+dc.increasedTotal;

            if(hitData.flags.critical) {
                dc.criticalMultiTotal+=criticalMultiGlobal;
                if(hitData.flags.spell)dc.criticalMultiTotal+=criticalMultiSpell;

                DamageLog.log(hitData,
                        "[modifier]CRITICAL %.2f x%.2f"
                                .formatted(
                                        dc.base,
                                        dc.criticalMultiTotal
                                )
                );
                dc.base*=dc.criticalMultiTotal;

                dc.base=Math.max(dc.base * (1-criticalMultiLess),0);
            }

            if(hitData.target.hasEffect(ModEffects.SHOCK.get())){
                int level = hitData.target.getEffect(ModEffects.SHOCK.get()).getAmplifier();
                dc.base *=1+((double)level/100);
            }

            if(hitData.flags.evaded) dc.base*=0.33;//部分回避の場合ダメージ1/3
        }
        if(hitData.flags.evaded)DamageLog.log(hitData,"[modifier]部分回避によりダメージ1/3");
        if(hitData.target.hasEffect(ModEffects.SHOCK.get()))DamageLog.log(hitData,"[modifier]感電によりダメージx%.2f".formatted((double)hitData.target.getEffect(ModEffects.SHOCK.get()).getAmplifier()/100));
        hitData.rebuildPreMitigationTotals();
        return true;
    }
}
