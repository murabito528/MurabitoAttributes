package com.murabito.murabitoattributesmod.damage.stages;

import com.murabito.murabitoattributesmod.damage.DamageStage;
import com.murabito.murabitoattributesmod.damage.HitData;

/**
 * conv,extraは属性ごとにそれぞれやることにしたからこのステージは廃止
 */
@Deprecated
public class GainDamageStage implements DamageStage {
    @Override
    public boolean apply(HitData hitData) {
        return false;
    }
}
