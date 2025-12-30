package com.murabito.murabitoattributesmod.damagesource;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

import static com.murabito.murabitoattributesmod.MurabitoAttributesMod.MODID;

/**
 * murabitoattributesmod.damageのほうで一括処理するように変更
 * エラーを起こさないためだけに残してる
 * 後で諸々削除
 */
@Deprecated
@SuppressWarnings("removal")
public class CustomDamageSource {
    public static final ResourceKey<DamageType> ELEMENTAL_FIRE =
            ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(MODID, "elemental_fire"));

    public static final ResourceKey<DamageType> ELEMENTAL_ICE =
            ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(MODID, "elemental_ice"));

    public static final ResourceKey<DamageType> ELEMENTAL_LIGHTNING =
            ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(MODID, "elemental_lightning"));

    public static final ResourceKey<DamageType> ELEMENTAL_CHAOS =
            ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(MODID, "elemental_chaos"));

    public static final ResourceKey<DamageType> REFLECT =
            ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(MODID, "reflect"));
}