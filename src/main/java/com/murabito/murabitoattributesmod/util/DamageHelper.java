package com.murabito.murabitoattributesmod.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class DamageHelper {
    /**
     * 攻撃者付きで任意のダメージタイプのダメージを与える汎用メソッド
     * @param target ダメージを受けるエンティティ
     * @param attacker ダメージの元となるエンティティ（null可）
     * @param level レベル（DamageType取得用）
     * @param damageTypeKey ダメージタイプのResourceKey
     * @param damage 与えるダメージ量
     */
    public static void dealDamageWithType(LivingEntity target, @Nullable Entity attacker, ServerLevel level, ResourceKey<DamageType> damageTypeKey, float damage) {
        if (damage <= 0) return;
        DamageSource source;
        if (attacker != null) {
            source = new DamageSource(
                    level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypeKey),
                    attacker, attacker
            );
        } else {
            source = new DamageSource(
                    level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypeKey)
            );
        }
        target.hurt(source, damage);
    }
}
