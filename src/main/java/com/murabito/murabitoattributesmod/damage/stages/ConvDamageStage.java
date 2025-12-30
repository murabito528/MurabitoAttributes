package com.murabito.murabitoattributesmod.damage.stages;

import com.murabito.murabitoattributesmod.attributes.CustomAttributes;
import com.murabito.murabitoattributesmod.damage.*;
import com.murabito.murabitoattributesmod.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ConvDamageStage implements DamageStage {
    @Override
    public boolean apply(HitData hitData) {
        if (hitData.attacker == null) {
            DamageLog.log(hitData,"[accuracy]アタッカーが不在");
            return true;
        }

        /*conv*/
        double physToLightning = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.PHYS_TO_LIGHTNING_DAMAGE_CONV.get());
        double physToCold = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.PHYS_TO_COLD_DAMAGE_CONV.get());
        double physToFire = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.PHYS_TO_FIRE_DAMAGE_CONV.get());
        double physToChaos = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.PHYS_TO_CHAOS_DAMAGE_CONV.get());

        double lightningToCold = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.LIGHTNING_TO_COLD_DAMAGE_CONV.get());
        double lightningToFire = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.LIGHTNING_TO_FIRE_DAMAGE_CONV.get());
        double lightningToChaos = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.LIGHTNING_TO_CHAOS_DAMAGE_CONV.get());

        double coldToFire = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.COLD_TO_FIRE_DAMAGE_CONV.get());
        double coldToChaos = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.COLD_TO_CHAOS_DAMAGE_CONV.get());

        double fireToChaos = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.FIRE_TO_CHAOS_DAMAGE_CONV.get());

        /*変換率が100%を超えた時に正規化*/
        double totalConv=physToLightning+physToCold+physToFire+physToChaos;
        if(totalConv>1){
            physToLightning/=totalConv;
            physToCold/=totalConv;
            physToFire/=totalConv;
            physToChaos/=totalConv;
        }
        totalConv=lightningToCold+lightningToFire+lightningToChaos;
        if(totalConv>1){
            lightningToCold/=totalConv;
            lightningToFire/=totalConv;
            lightningToChaos/=totalConv;
        }
        totalConv=coldToFire+coldToChaos;
        if(totalConv>1){
            coldToFire/=totalConv;
            coldToChaos/=totalConv;
        }
        totalConv=fireToChaos;
        if(totalConv>1){
            fireToChaos/=totalConv;
        }

        /*as extra*/
        double physToLightningExtra = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.PHYS_TO_LIGHTNING_DAMAGE_EXTRA.get());
        double physToColdExtra = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.PHYS_TO_COLD_DAMAGE_EXTRA.get());
        double physToFireExtra = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.PHYS_TO_FIRE_DAMAGE_EXTRA.get());
        double physToChaosExtra = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.PHYS_TO_CHAOS_DAMAGE_EXTRA.get());

        double lightningToColdExtra = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.LIGHTNING_TO_COLD_DAMAGE_EXTRA.get());
        double lightningToFireExtra = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.LIGHTNING_TO_FIRE_DAMAGE_EXTRA.get());
        double lightningToChaosExtra = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.LIGHTNING_TO_CHAOS_DAMAGE_EXTRA.get());

        double coldToFireExtra = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.COLD_TO_FIRE_DAMAGE_EXTRA.get());
        double coldToChaosExtra = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.COLD_TO_CHAOS_DAMAGE_EXTRA.get());

        double fireToChaosExtra = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.FIRE_TO_CHAOS_DAMAGE_EXTRA.get());

        if (hitData.components == null || hitData.components.isEmpty()) {
            DamageLog.log(hitData, "[convDamage]ダメージコンポーネントがありません");
            return true;
        }

        List<DamageComponent> snapshot = new ArrayList<>(hitData.components);
        for(DamageComponent dc : snapshot) {
            extra(hitData, dc, ModDamageType.PHYSICAL, ModDamageType.LIGHTNING, physToLightningExtra);
            extra(hitData, dc, ModDamageType.PHYSICAL, ModDamageType.COLD, physToColdExtra);
            extra(hitData, dc, ModDamageType.PHYSICAL, ModDamageType.FIRE, physToFireExtra);
            extra(hitData, dc, ModDamageType.PHYSICAL, ModDamageType.CHAOS, physToChaosExtra);

            conv(hitData, dc, ModDamageType.PHYSICAL, ModDamageType.LIGHTNING, physToLightning);
            conv(hitData, dc, ModDamageType.PHYSICAL, ModDamageType.COLD, physToCold);
            conv(hitData, dc, ModDamageType.PHYSICAL, ModDamageType.FIRE, physToFire);
            conv(hitData, dc, ModDamageType.PHYSICAL, ModDamageType.CHAOS, physToChaos);
            damageFix(hitData,dc,ModDamageType.PHYSICAL);
        }

        snapshot = new ArrayList<>(hitData.components);
        for(DamageComponent dc : snapshot) {
            extra(hitData, dc, ModDamageType.LIGHTNING, ModDamageType.COLD, lightningToColdExtra);
            extra(hitData, dc, ModDamageType.LIGHTNING, ModDamageType.FIRE, lightningToFireExtra);
            extra(hitData, dc, ModDamageType.LIGHTNING, ModDamageType.CHAOS, lightningToChaosExtra);

            conv(hitData, dc, ModDamageType.LIGHTNING, ModDamageType.COLD, lightningToCold);
            conv(hitData, dc, ModDamageType.LIGHTNING, ModDamageType.FIRE, lightningToFire);
            conv(hitData, dc, ModDamageType.LIGHTNING, ModDamageType.CHAOS, lightningToChaos);
            damageFix(hitData,dc,ModDamageType.LIGHTNING);
        }
        snapshot = new ArrayList<>(hitData.components);
        for(DamageComponent dc : snapshot) {
            extra(hitData, dc, ModDamageType.COLD, ModDamageType.FIRE, coldToFireExtra);
            extra(hitData, dc, ModDamageType.COLD, ModDamageType.CHAOS, coldToChaosExtra);

            conv(hitData, dc, ModDamageType.COLD, ModDamageType.FIRE, coldToFire);
            conv(hitData, dc, ModDamageType.COLD, ModDamageType.CHAOS, coldToChaos);
            damageFix(hitData,dc,ModDamageType.COLD);
        }
        snapshot = new ArrayList<>(hitData.components);
        for(DamageComponent dc : snapshot) {
            extra(hitData, dc, ModDamageType.FIRE, ModDamageType.CHAOS, fireToChaosExtra);

            conv(hitData, dc, ModDamageType.FIRE, ModDamageType.CHAOS, fireToChaos);
            damageFix(hitData,dc,ModDamageType.FIRE);
        }

        return true;
    }

    void conv(HitData hitData,DamageComponent dc, ModDamageType before, ModDamageType after, double percent){
        if(dc.finalType!=before||percent==0) return;

        double convertedDmg=dc.base*percent;
        dc.convertedDamageTotal+=convertedDmg;
        DamageComponent newDc = dc.clone();
        newDc.base=convertedDmg;
        newDc.affectedTypes.add(after);
        newDc.finalType=after;
        hitData.components.add(newDc);
        DamageLog.log(hitData,"[convDamage]ダメージ変換:"+before.name()+dc.base+"-("+percent*100+"%)->"+before.name()+(dc.base-convertedDmg)+","+after.name()+newDc.base);
    }

    void extra(HitData hitData,DamageComponent dc, ModDamageType before, ModDamageType after, double percent){
        if(dc.finalType!=before||percent==0) return;

        double extraDmg=dc.base*percent;
        DamageComponent newDc = dc.clone();
        newDc.base=extraDmg;
        newDc.affectedTypes.add(after);
        newDc.finalType=after;
        hitData.components.add(newDc);
        DamageLog.log(hitData,"[convDamage]追加ダメージ"+before.name()+dc.base+"-("+percent*100+"%)->"+before.name()+dc.base+","+after.name()+newDc.base);
    }

    void damageFix(HitData hitData, DamageComponent dc, ModDamageType before){
        if(dc.finalType!=before) return;
        dc.base -= dc.convertedDamageTotal;
        dc.convertedDamageTotal=0;
    }
}
