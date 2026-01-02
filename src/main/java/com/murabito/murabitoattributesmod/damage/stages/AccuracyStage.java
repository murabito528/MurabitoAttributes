package com.murabito.murabitoattributesmod.damage.stages;

import com.murabito.murabitoattributesmod.attributes.CustomAttributes;
import com.murabito.murabitoattributesmod.damage.DamageLog;
import com.murabito.murabitoattributesmod.damage.DamageStage;
import com.murabito.murabitoattributesmod.damage.HitData;
import com.murabito.murabitoattributesmod.gamerule.CustomGameRules;
import com.murabito.murabitoattributesmod.util.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class AccuracyStage implements DamageStage {
    @Override
    public boolean apply(HitData hitData) {
        DamageLog.log(hitData,"----------------(ダメージログ)------------------");
        //attackerが不在の場合は必中(環境ダメージとか?)
        if (hitData.attacker == null) {
            DamageLog.log(hitData,"[accuracy]アタッカーが不在");
            return true;
        }

        double acc = Util.getAttributeValueOrZero(hitData.attacker, CustomAttributes.ACCURACY.get());
        double eva = Util.getAttributeValueOrZero(hitData.target , CustomAttributes.EVASION_CHANCE.get());
        double evaMax = Util.getAttributeValueOrZero(hitData.target , CustomAttributes.EVASION_CHANCE_MAX.get());

        double evaChance = Math.min(eva, evaMax);
        double hitChance = acc - evaChance;
        hitChance = Math.max(0.05, hitChance);        // 最低5%
        boolean hit = Math.random() < hitChance;

        if(hit){
            DamageLog.log(hitData,"[accuracy]命中(命中率:"+hitChance*100+"%)");
            return true;
        }

        if(hitData.target instanceof Player){
            //プレイヤーであれば完全に回避して処理を中断
            DamageLog.log(hitData,"[accuracy]命中判定に失敗(命中率:"+hitChance*100+"%)");
            return false;
        }else{
            //プレイヤーでなければ回避しても一部ダメージを受ける
            DamageLog.log(hitData,"[accuracy]命中判定に失敗、一部命中(命中率:"+hitChance*100+"%)");
            hitData.flags.evaded=true;
            return true;
        }
    }
}
