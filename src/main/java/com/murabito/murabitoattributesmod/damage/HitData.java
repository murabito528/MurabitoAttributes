package com.murabito.murabitoattributesmod.damage;

import com.murabito.murabitoattributesmod.damagesource.DamageSourceType;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

/**
 * ヒットのデータ、ヒットごとに作成
 */

public class HitData {
    public LivingEntity target;
    public LivingEntity attacker;
    public List<DamageComponent> components;
    public HitFlags flags;

    public EnumMap<ModDamageType, Double> preMitigationTotalDamages;
    public EnumMap<ModDamageType, Double> postMitigationTotalDamages;

    public HitData(LivingEntity target, LivingEntity attacker, HitFlags flags, DamageComponent dc) {
        this.target = target;
        this.attacker = attacker;
        this.flags = flags;
        this.components = new ArrayList<>();
        this.components.add(dc);
        this.preMitigationTotalDamages=new EnumMap<>(ModDamageType.class);
        this.postMitigationTotalDamages=new EnumMap<>(ModDamageType.class);
    }

    public double getTotalDamagePostMitigation(ModDamageType type) {
        if (postMitigationTotalDamages == null || postMitigationTotalDamages.isEmpty()) return 0.0;
        return postMitigationTotalDamages.get(type);
    }
    public double getTotalDamagePreMitigation(ModDamageType type) {
        if (preMitigationTotalDamages == null || preMitigationTotalDamages.isEmpty()) return 0.0;
        return preMitigationTotalDamages.get(type);
    }

    public void rebuildPreMitigationTotals() {
        preMitigationTotalDamages.clear();
        for (ModDamageType t : ModDamageType.values()) {
            preMitigationTotalDamages.putIfAbsent(t, 0.0);
        }
        for (DamageComponent dc : components) {
            preMitigationTotalDamages.merge(dc.finalType, dc.base, Double::sum);
        }
    }
}
