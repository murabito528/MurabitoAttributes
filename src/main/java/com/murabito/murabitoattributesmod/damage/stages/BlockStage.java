package com.murabito.murabitoattributesmod.damage.stages;

import com.murabito.murabitoattributesmod.attributes.CustomAttributes;
import com.murabito.murabitoattributesmod.damage.DamageLog;
import com.murabito.murabitoattributesmod.damage.DamageStage;
import com.murabito.murabitoattributesmod.damage.HitData;
import com.murabito.murabitoattributesmod.util.Util;

public class BlockStage implements DamageStage {
    @Override
    public boolean apply(HitData hitData) {
        //attackerが不在の場合は必中(環境ダメージとか?)
        if (hitData.attacker == null) {
            DamageLog.log(hitData,"[block]アタッカーが不在");
            return true;
        }

        double block = Util.getAttributeValueOrZero(hitData.target , CustomAttributes.BLOCK_CHANCE.get());
        double spellBlock = Util.getAttributeValueOrZero(hitData.target , CustomAttributes.SPELL_BLOCK_CHANCE.get());
        double blockMax = Util.getAttributeValueOrZero(hitData.target , CustomAttributes.BLOCK_CHANCE_MAX.get());
        block=Math.min(block,blockMax);
        spellBlock=Math.min(spellBlock,blockMax);


        boolean hit;
        if(hitData.flags.spell){
            hit=Math.random()<spellBlock;
            DamageLog.log(hitData, hit ? "[block]スペル防御成功(防御率:"+spellBlock*100+"%)" : "[block]スペル防御失敗(防御率:"+spellBlock*100+"%)");
        }else{
            hit=Math.random()<block;
            DamageLog.log(hitData, hit ? "[block]防御成功(防御率:"+block*100+"%)" : "[block]防御失敗(防御率:"+block*100+"%)");
        }

        hitData.flags.blocked=hit;

        return true;
    }
}
