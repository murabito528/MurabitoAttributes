package com.murabito.murabitoattributesmod.damage.stages;

import com.murabito.murabitoattributesmod.attributes.CustomAttributes;
import com.murabito.murabitoattributesmod.damage.DamageLog;
import com.murabito.murabitoattributesmod.damage.DamageStage;
import com.murabito.murabitoattributesmod.damage.HitData;
import com.murabito.murabitoattributesmod.damage.ModDamageType;
import com.murabito.murabitoattributesmod.util.Util;

public class ConversionStage implements DamageStage {
    @Override
    public boolean apply(HitData hitData) {
        //DamageLog.log(hitData,"[conversion]未完成、処理なし");
        double physToLightning = Util.getAttributeValueOrZero(hitData.target , CustomAttributes.LIGHTNING_DAMAGE_TAKEN_AS.get());
        double physToCold = Util.getAttributeValueOrZero(hitData.target , CustomAttributes.COLD_DAMAGE_TAKEN_AS.get());
        double physToFire = Util.getAttributeValueOrZero(hitData.target , CustomAttributes.FIRE_DAMAGE_TAKEN_AS.get());
        double physToChaos = Util.getAttributeValueOrZero(hitData.target , CustomAttributes.CHAOS_DAMAGE_TAKEN_AS.get());

        double total = physToLightning+physToCold+physToFire+physToChaos;
        if(total>1){
            physToLightning/=total;
            physToCold/=total;
            physToFire/=total;
            physToChaos/=total;
        }

        double phys = hitData.preMitigationTotalDamages.get(ModDamageType.PHYSICAL);
        hitData.preMitigationTotalDamages.merge(ModDamageType.LIGHTNING,phys*physToLightning,Double::sum);
        hitData.preMitigationTotalDamages.merge(ModDamageType.COLD,phys*physToCold,Double::sum);
        hitData.preMitigationTotalDamages.merge(ModDamageType.FIRE,phys*physToFire,Double::sum);
        hitData.preMitigationTotalDamages.merge(ModDamageType.CHAOS,phys*physToChaos,Double::sum);

        double takenPercent= Math.min(total, 1.0);
        hitData.preMitigationTotalDamages.put(ModDamageType.PHYSICAL, phys * (1.0 - takenPercent));
        return true;
    }
}
