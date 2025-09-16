package com.murabito.murabitoattributesmod.damagesource;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

@SuppressWarnings("removal")
public class ModDamageTypeTags {
    public static final TagKey<DamageType> IS_PHYS_DAMAGE = create("murabitoattributes:is_phys_damage");
    public static final TagKey<DamageType> IS_FIRE_DAMAGE = create("murabitoattributes:is_fire_damage");
    public static final TagKey<DamageType> IS_ICE_DAMAGE = create("murabitoattributes:is_ice_damage");
    public static final TagKey<DamageType> IS_LIGHTNING_DAMAGE = create("murabitoattributes:is_lightning_damage");
    public static final TagKey<DamageType> IS_CHAOS_DAMAGE = create("murabitoattributes:is_chaos_damage");
    public static final TagKey<DamageType> IS_TRACKING = create("murabitoattributes:is_tracking");

    private static TagKey<DamageType> create(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(name));
    }
}
