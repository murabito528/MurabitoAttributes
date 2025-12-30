package com.murabito.murabitoattributesmod.util;

import com.murabito.murabitoattributesmod.attributes.CustomAttributes;
import com.murabito.murabitoattributesmod.damagesource.ModDamageTypeTags_old;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

import static com.murabito.murabitoattributesmod.util.Util.getAttributeValueOrZero;

/**
 * murabitoattributesmod.damageのほうで一括処理するように変更
 * 特にこのクラス長すぎ!
 * エラーを起こさないためだけに残してる
 * 後で諸々削除
 */
@Deprecated
public class DamageData {
    public float originalDamage;
    public ResourceKey<DamageType> damageType;

    double physExtra;
    double physMulti;

    double convFire;
    double flatFire;
    double convExtraFire;
    double convFire2Chaos;

    double convIce;
    double flatIce;
    double convExtraIce;
    double convIce2Fire;

    double convLightning;
    double flatLightning;
    double convExtraLightning;
    double convLightning2Ice;

    double convChaos;
    double flatChaos;
    double convExtraChaos;

    double fireScale;
    double iceScale;
    double lightningScale;
    double chaosScale;

    double criticalChance;
    double spellCriticalChance;
    double criticalMulti;
    double spellCriticalMulti;

    // 最終的なダメージ
    public float reducedPhysical;
    public float finalFireDamage;
    public float finalIceDamage;
    public float finalLightningDamage;
    public float finalChaosDamage;

    public double effCritical;

    double inputPhysDamage;
    double inputFireDamage;
    double inputIceDamage;
    double inputLightningDamage;
    double inputChaosDamage;

    public DamageData(float damage, DamageSource source, LivingEntity attacker, LivingEntity target){
        /*オリジナルのダメージ*/
        originalDamage = damage;

        inputPhysDamage = (source.is(ModDamageTypeTags_old.IS_PHYS_DAMAGE))?damage:0;
        inputFireDamage = (source.is(ModDamageTypeTags_old.IS_FIRE_DAMAGE))?damage:0;
        inputIceDamage = (source.is(ModDamageTypeTags_old.IS_COLD_DAMAGE))?damage:0;
        inputLightningDamage = (source.is(ModDamageTypeTags_old.IS_LIGHTNING_DAMAGE))?damage:0;
        inputChaosDamage = (source.is(ModDamageTypeTags_old.IS_CHAOS_DAMAGE))?damage:0;

        // 攻撃者属性
        physExtra = getAttributeValueOrZero(attacker, CustomAttributes.PHYS_DAMAGE_EXTRA.get());
        physMulti = getAttributeValueOrZero(attacker, CustomAttributes.PHYS_DAMAGE_MULTI.get());

        convFire = getAttributeValueOrZero(attacker, CustomAttributes.PHYS_TO_FIRE_DAMAGE_CONV.get());
        flatFire = getAttributeValueOrZero(attacker, CustomAttributes.FIRE_DAMAGE_BASE.get());
        convExtraFire = getAttributeValueOrZero(attacker, CustomAttributes.PHYS_TO_FIRE_DAMAGE_EXTRA.get());

        convIce = getAttributeValueOrZero(attacker, CustomAttributes.PHYS_TO_COLD_DAMAGE_CONV.get());
        flatIce = getAttributeValueOrZero(attacker, CustomAttributes.COLD_DAMAGE_BASE.get());
        convExtraIce = getAttributeValueOrZero(attacker, CustomAttributes.PHYS_TO_COLD_DAMAGE_EXTRA.get());

        convLightning = getAttributeValueOrZero(attacker, CustomAttributes.PHYS_TO_LIGHTNING_DAMAGE_CONV.get());
        flatLightning = getAttributeValueOrZero(attacker, CustomAttributes.LIGHTNING_DAMAGE_BASE.get());
        convExtraLightning = getAttributeValueOrZero(attacker, CustomAttributes.PHYS_TO_LIGHTNING_DAMAGE_EXTRA.get());

        convChaos = getAttributeValueOrZero(attacker, CustomAttributes.PHYS_TO_CHAOS_DAMAGE_CONV.get());
        flatChaos = getAttributeValueOrZero(attacker, CustomAttributes.CHAOS_DAMAGE_BASE.get());
        convExtraChaos = getAttributeValueOrZero(attacker, CustomAttributes.PHYS_TO_CHAOS_DAMAGE_EXTRA.get());

        convLightning2Ice = getAttributeValueOrZero(attacker, CustomAttributes.LIGHTNING_TO_COLD_DAMAGE_CONV.get());
        convIce2Fire = getAttributeValueOrZero(attacker, CustomAttributes.COLD_TO_FIRE_DAMAGE_CONV.get());
        convFire2Chaos = getAttributeValueOrZero(attacker, CustomAttributes.FIRE_TO_CHAOS_DAMAGE_CONV.get());

        fireScale = getAttributeValueOrZero(attacker, CustomAttributes.FIRE_DAMAGE_SCALE.get());
        iceScale = getAttributeValueOrZero(attacker, CustomAttributes.COLD_DAMAGE_SCALE.get());
        lightningScale = getAttributeValueOrZero(attacker, CustomAttributes.LIGHTNING_DAMAGE_SCALE.get());
        chaosScale = getAttributeValueOrZero(attacker, CustomAttributes.CHAOS_DAMAGE_SCALE.get());

        criticalChance = getAttributeValueOrZero(attacker, CustomAttributes.CRITICAL_CHANCE.get());
        criticalMulti = getAttributeValueOrZero(attacker, CustomAttributes.CRITICAL_MULTI.get());
        spellCriticalChance = getAttributeValueOrZero(attacker, CustomAttributes.SPELL_CRITICAL_CHANCE.get());
        spellCriticalMulti = getAttributeValueOrZero(attacker, CustomAttributes.SPELL_CRITICAL_MULTI.get());
    }
    
    public void calc_damage(){
        float physDamage = (float)((inputPhysDamage + physExtra)*physMulti);

        // 合計変換率を計算
        double totalConv = convFire + convIce + convLightning + convChaos;

        // スケーリング係数（100%を超える場合は縮小）
        double scale = totalConv > 1.0 ? 1.0 / totalConv : 1.0;

        // 物理 → 属性変換
        reducedPhysical = (float) (physDamage * (1.0 - Math.min(1.0, totalConv)));
        double convertedFireDamage = (physDamage * convFire * scale);
        double convertedIceDamage = (physDamage * convIce * scale);
        double convertedLightningDamage = (physDamage * convLightning * scale);
        double convertedChaosDamage = (physDamage * convChaos * scale);

        //属性->属性変換数値調整
        convLightning2Ice = Math.min(1,convLightning2Ice);
        convIce2Fire = Math.min(1,convIce2Fire);
        convFire2Chaos = Math.min(1,convFire2Chaos);

        // 属性ダメージ計算
        //Lightning
        double lightningBase = inputLightningDamage + convertedLightningDamage + flatLightning + physDamage*convExtraLightning;
        double lightningDamage = lightningBase * lightningScale;

        // Lightning → Ice
        double toIceFromLightning = lightningDamage * convLightning2Ice;
        lightningDamage *= (1 - convLightning2Ice);

        // Ice
        double iceBase = inputIceDamage + toIceFromLightning + convertedIceDamage + flatIce + physDamage*convExtraIce;
        double iceDamage = iceBase * iceScale;

        // Ice → Fire
        double toFireFromIce = iceDamage * convIce2Fire;
        iceDamage *= (1 - convIce2Fire);

        // Fire
        double fireBase = inputFireDamage + toFireFromIce + convertedFireDamage + flatFire + physDamage*convExtraFire;
        double fireDamage = fireBase * fireScale;

        // Fire → Chaos
        double toChaosFromFire = fireDamage * convFire2Chaos;
        fireDamage *= (1 - convFire2Chaos);

        // Chaos
        double chaosBase = inputChaosDamage + toChaosFromFire + convertedChaosDamage + flatChaos + physDamage*convExtraChaos;
        double chaosDamage = chaosBase * chaosScale;


        //クリティカル判定
        effCritical = (1%Math.random()<criticalChance)?criticalMulti:1.0;

        /*最終的なダメージ*/
        reducedPhysical = (float)(reducedPhysical*effCritical);
        finalLightningDamage = (float)(lightningDamage*effCritical);
        finalIceDamage = (float)(iceDamage*effCritical);
        finalFireDamage = (float)(fireDamage*effCritical);
        finalChaosDamage = (float)(chaosDamage*effCritical);
    }
}
