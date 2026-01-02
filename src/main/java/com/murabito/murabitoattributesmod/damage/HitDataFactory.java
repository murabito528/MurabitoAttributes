package com.murabito.murabitoattributesmod.damage;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;

import java.util.EnumMap;

public class HitDataFactory {
    public static HitData create(
            LivingEntity target,
            Entity attackerEntity,
            DamageSource damageSource,
            double baseDamage,
            boolean canOnHit
    ) {
        LivingEntity attacker = null;
        boolean isProjectile = false;

        if (attackerEntity instanceof Projectile p) {
            isProjectile = true;
            if (p.getOwner() instanceof LivingEntity o) {
                attacker = o;
            }
        } else if (attackerEntity instanceof LivingEntity l) {
            attacker = l;
        }

        DamageComponent dc = null;

        for (ModDamageType type : ModDamageType.values()) {
            if (type.matches(damageSource)) {
                dc = new DamageComponent(baseDamage,type);
            }
        }

        HitFlags flags = new HitFlags();
        flags.canOnHit=canOnHit;

        return new HitData(target, attacker, flags, dc);
    }
}
