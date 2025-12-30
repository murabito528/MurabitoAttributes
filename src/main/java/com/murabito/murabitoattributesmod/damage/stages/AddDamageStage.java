package com.murabito.murabitoattributesmod.damage.stages;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import com.murabito.murabitoattributesmod.attributes.CustomAttributes;
import com.murabito.murabitoattributesmod.damage.*;
import com.murabito.murabitoattributesmod.util.Util;

import static com.murabito.murabitoattributesmod.util.Util.getAttributeValueOrZero;

public class AddDamageStage implements DamageStage {
    @Override
    public boolean apply(HitData hitData) {
        if (hitData.attacker == null) {
            DamageLog.log(hitData,"[accuracy]アタッカーが不在");
            return true;
        }

        double addPhysDamage = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.PHYS_ADD.get());
        double addFireDamage = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.FIRE_ADD.get());
        double addColdDamage = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.COLD_ADD.get());
        double addLightningDamage = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.LIGHTNING_ADD.get());
        double addChaosDamage = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.CHAOS_ADD.get());

        if(addPhysDamage>0) {
            DamageComponent dc = new DamageComponent(addPhysDamage, ModDamageType.PHYSICAL);
            hitData.components.add(dc);
            DamageLog.log(hitData,"[AddDamage]追加物理ダメージ:"+addPhysDamage);
        }
        if(addFireDamage>0) {
            DamageComponent dc = new DamageComponent(addFireDamage, ModDamageType.FIRE);
            hitData.components.add(dc);
            DamageLog.log(hitData,"[AddDamage]追加火ダメージ:"+addFireDamage);
        }
        if(addColdDamage>0) {
            DamageComponent dc = new DamageComponent(addColdDamage, ModDamageType.COLD);
            hitData.components.add(dc);
            DamageLog.log(hitData,"[AddDamage]追加冷気ダメージ:"+addColdDamage);
        }
        if(addLightningDamage>0) {
            DamageComponent dc = new DamageComponent(addLightningDamage, ModDamageType.LIGHTNING);
            hitData.components.add(dc);
            DamageLog.log(hitData,"[AddDamage]追加雷ダメージ:"+addLightningDamage);
        }
        if(addChaosDamage>0) {
            DamageComponent dc = new DamageComponent(addChaosDamage, ModDamageType.CHAOS);
            hitData.components.add(dc);
            DamageLog.log(hitData,"[AddDamage]追加混沌ダメージ:"+addChaosDamage);
        }

        return true;
    }
}
