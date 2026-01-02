package com.murabito.murabitoattributesmod.damage.stages;

import com.murabito.murabitoattributesmod.damage.DamageLog;
import com.murabito.murabitoattributesmod.damage.DamageStage;
import com.murabito.murabitoattributesmod.damage.HitData;

import com.murabito.murabitoattributesmod.damage.ModDamageType;
import com.murabito.murabitoattributesmod.damagesource.CustomDamageSource;
import net.minecraft.server.level.ServerLevel;

import static com.murabito.murabitoattributesmod.util.DamageHelper.dealDamageWithType;

public class ApplyStage implements DamageStage {
    @Override
    public boolean apply(HitData hitData) {
        double physDmg=hitData.getTotalDamagePostMitigation(ModDamageType.PHYSICAL);
        double lightningDmg=hitData.getTotalDamagePostMitigation(ModDamageType.LIGHTNING);
        double coldDmg=hitData.getTotalDamagePostMitigation(ModDamageType.COLD);
        double fireDmg=hitData.getTotalDamagePostMitigation(ModDamageType.FIRE);
        double chaosDmg=hitData.getTotalDamagePostMitigation(ModDamageType.CHAOS);

        DamageLog.log(hitData,"[apply]物理:%.2f 雷:%.2f 冷気:%.2f 火:%.2f 混沌:%.2f".formatted(
                physDmg,lightningDmg,coldDmg,fireDmg,chaosDmg));

        int invTime = hitData.target.invulnerableTime;
        hitData.target.invulnerableTime=0;
        if(lightningDmg>0) dealDamageWithType(hitData.target, hitData.attacker, (ServerLevel)hitData.target.level(), CustomDamageSource.ELEMENTAL_LIGHTNING,(float)lightningDmg);
        hitData.target.invulnerableTime=0;
        if(coldDmg>0) dealDamageWithType(hitData.target, hitData.attacker, (ServerLevel)hitData.target.level(), CustomDamageSource.ELEMENTAL_COLD,(float)coldDmg);
        hitData.target.invulnerableTime=0;
        if(fireDmg>0) dealDamageWithType(hitData.target, hitData.attacker, (ServerLevel)hitData.target.level(), CustomDamageSource.ELEMENTAL_FIRE,(float)fireDmg);
        hitData.target.invulnerableTime=0;
        if(chaosDmg>0) dealDamageWithType(hitData.target, hitData.attacker, (ServerLevel)hitData.target.level(), CustomDamageSource.ELEMENTAL_CHAOS,(float)chaosDmg);
        hitData.target.invulnerableTime=invTime;
        return true;
    }
}
