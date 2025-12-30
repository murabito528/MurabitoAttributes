package com.murabito.murabitoattributesmod.damage.stages;

import com.murabito.murabitoattributesmod.attributes.CustomAttributes;
import com.murabito.murabitoattributesmod.damage.DamageLog;
import com.murabito.murabitoattributesmod.damage.DamageStage;
import com.murabito.murabitoattributesmod.damage.HitData;
import com.murabito.murabitoattributesmod.util.Util;

public class CritStage implements DamageStage {
    @Override
    public boolean apply(HitData hitData) {
        if (hitData.attacker == null) {
            DamageLog.log(hitData,"[accuracy]アタッカーが不在");
            return true;
        }

        double critChance = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.CRITICAL_CHANCE.get());
        double spellCritChance = Util.getAttributeValueOrZero(hitData.attacker , CustomAttributes.SPELL_CRITICAL_CHANCE.get());

        boolean crit;
        if(hitData.flags.spell){
            crit = Math.random() < spellCritChance;
            DamageLog.log(hitData, crit ? "[crit]スペルクリティカル発生(クリティカル率:"+spellCritChance*100+"%)" : "[crit]スペル通常(クリティカル率:"+spellCritChance*100+"%)");
        }else{
            crit = Math.random() < critChance;
            DamageLog.log(hitData, crit ? "[crit]クリティカル発生(クリティカル率:"+critChance*100+"%)" : "[crit]通常(クリティカル率:"+critChance*100+"%)");
        }
        hitData.flags.critical=crit;

        return true;
    }
}
