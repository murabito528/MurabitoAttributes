package com.murabito.murabitoattributesmod.damage.stages;

import com.murabito.murabitoattributesmod.damage.DamageLog;
import com.murabito.murabitoattributesmod.damage.DamageStage;
import com.murabito.murabitoattributesmod.damage.HitData;

public class DefenseStage implements DamageStage {
    @Override
    public boolean apply(HitData hitData) {
        DamageLog.log(hitData,"[defence]未完成、処理なし");
        return true;
    }
}
