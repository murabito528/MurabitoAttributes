package com.murabito.murabitoattributesmod.damage;

import com.murabito.murabitoattributesmod.damagesource.DamageSourceType;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * ヒットのデータ、ヒットごとに作成
 */

public class HitData {
    public LivingEntity target;
    public LivingEntity attacker;
    public List<DamageComponent> components;
    public HitFlags flags;

    public HitData(LivingEntity target, LivingEntity attacker, HitFlags flags, DamageComponent dc) {
        this.target = target;
        this.attacker = attacker;
        this.flags = flags;
        this.components = new ArrayList<>();
        this.components.add(dc);
    }
}
