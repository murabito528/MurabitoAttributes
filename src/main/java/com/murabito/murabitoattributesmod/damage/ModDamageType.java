package com.murabito.murabitoattributesmod.damage;


import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

/**
 * MA上のダメージ属性
 */
public enum ModDamageType {
    PHYSICAL(ModDamageTypeTags.IS_PHYS_DAMAGE),
    FIRE(ModDamageTypeTags.IS_FIRE_DAMAGE),
    COLD(ModDamageTypeTags.IS_COLD_DAMAGE),
    LIGHTNING(ModDamageTypeTags.IS_LIGHTNING_DAMAGE),
    CHAOS(ModDamageTypeTags.IS_CHAOS_DAMAGE),
    TRUE(ModDamageTypeTags.IS_TRUE_DAMAGE);

    private final TagKey<DamageType> tag;

    ModDamageType(TagKey<DamageType> tag) {
        this.tag = tag;
    }

    public boolean matches(DamageSource source) {
        return source.is(tag);
    }
}
