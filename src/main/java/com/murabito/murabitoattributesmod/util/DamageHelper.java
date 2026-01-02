package com.murabito.murabitoattributesmod.util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
        if (level.isClientSide()) return; // サーバー側でのみ実行
        // 例: "minecraft:magic" や "yourmod:custom_damage"

        Holder<DamageType> damageTypeHolder = level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolder(damageTypeKey)
                .orElseThrow(() -> new IllegalArgumentException("Unknown damage type: " + damageTypeKey.location()));

        DamageSource source = new DamageSource(damageTypeHolder, attacker);
        target.hurt(source, damage);
    }
}
