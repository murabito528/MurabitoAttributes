package com.murabito.murabitoattributesmod.damage.stages;

import com.murabito.murabitoattributesmod.attributes.CustomAttributes;
import com.murabito.murabitoattributesmod.damage.DamageLog;
import com.murabito.murabitoattributesmod.damage.DamageStage;
import com.murabito.murabitoattributesmod.damage.HitData;
import com.murabito.murabitoattributesmod.damage.ModDamageType;
import com.murabito.murabitoattributesmod.potion.ModEffects;
import com.murabito.murabitoattributesmod.util.Util;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class AilmentStage implements DamageStage {
    @Override
    public boolean apply(HitData hitData) {
        DamageLog.log(hitData,"[ailment]未完成、感電、冷却のみ");

        if (hitData.flags == null || hitData.flags.evaded || hitData.flags.blocked) {
            DamageLog.log(hitData, "[ailment]部分回避/ブロックによりスキップ");
            return true;
        }
        if (!hitData.flags.canOnHit) {
            DamageLog.log(hitData, "[ailment]オンヒットが発生しないためスキップ");
            return true;
        }
        if (hitData.attacker == null) {
            DamageLog.log(hitData,"[accuracy]アタッカーが不在");
            return true;
        }

        int MAX_CHILL_LEVEL=30;
        int CHILL_DURATION_BASE=2 * 20;
        int MAX_SHOCK_LEVEL=50;
        int SHOCK_DURATION_BASE=2 * 20;

        double coldDmg = hitData.getTotalDamagePostMitigation(ModDamageType.COLD);
        double chillEff = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.CHILL_EFFECT_INC.get());
        double chillTimeInc = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.CHILL_TIME_INC.get());

        if (coldDmg > 0) {
            //https://www.poewiki.net/wiki/Chill
            int level = (int)Math.min(MAX_CHILL_LEVEL,0.5*Math.pow(coldDmg/hitData.target.getMaxHealth(),0.4)*(1+chillEff)*100);
            if (level >= 5) {
                int duration = (int)(CHILL_DURATION_BASE*(1+chillTimeInc));
                applyOrRefreshEffect(hitData.target, ModEffects.CHILL.get(),duration,level-1);
                DamageLog.log(hitData,"[ailment]冷却%d%% %.2fs".formatted(level,(double)duration/20));
            }
        }

        double lightningDmg = hitData.getTotalDamagePostMitigation(ModDamageType.LIGHTNING);
        double shockChance = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.SHOCK_CHANCE.get());
        double shockEff = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.SHOCK_EFFECT_INC.get());
        double shockTimeInc = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.SHOCK_TIME_INC.get());

        if (lightningDmg > 0) {
            if(Math.random()<shockChance||hitData.flags.critical) {
                //https://www.poewiki.net/wiki/Shock
                int level = (int) Math.min(MAX_SHOCK_LEVEL, 0.5 * Math.pow(lightningDmg / hitData.target.getMaxHealth(), 0.4) * (1 + shockEff) * 100);
                if (level >= 5) {
                    int duration = (int) (SHOCK_DURATION_BASE * (1 + shockTimeInc));
                    applyOrRefreshEffect(hitData.target, ModEffects.SHOCK.get(), duration, level-1);
                    DamageLog.log(hitData, "[ailment]感電%d%% %.2fs".formatted(level, (double) duration / 20));
                }
            }else{
                DamageLog.log(hitData, "[ailment]感電判定に失敗(%.2f)".formatted(shockChance));
            }
        }
        return true;
    }

    private static void applyOrRefreshEffect(LivingEntity target, MobEffect effect, int durationTicks, int amplifier) {
        MobEffectInstance cur = target.getEffect(effect);

        if (cur == null) {
            target.addEffect(new MobEffectInstance(effect, durationTicks, amplifier, true, true, true));
            return;
        }

        int curAmp = cur.getAmplifier();
        int curDur = cur.getDuration();

        int nextAmp = Math.max(curAmp, amplifier);
        int nextDur = Math.max(curDur, durationTicks);

        // 変化がある時だけ更新（無駄な同期を減らす）
        if (nextAmp != curAmp || nextDur != curDur) {
            target.addEffect(new MobEffectInstance(effect, nextDur, nextAmp, true, true, true));
        }
    }
}
