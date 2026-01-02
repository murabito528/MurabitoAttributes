package com.murabito.murabitoattributesmod.attributes;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = MurabitoAttributesMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CustomAttributes {


    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MurabitoAttributesMod.MODID);

    /*物理*/
    //public static final RegistryObject<Attribute> PHYS_DAMAGE_EXTRA =
    //        register("phys_damage_extra", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> PHYS_DAMAGE_MORE =
            register("phys_damage_more", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> PHYS_DAMAGE_INC =
            register("phys_damage_increased", 0.0D, 0.0D, 25565.0D);

    /*物理軽減*/
    public static final RegistryObject<Attribute> PHYS_REDUCTION =
            register("phys_reduction", 0.0D, 0.0D, 25565.0D);

    /*非物理属性基礎値*/
    public static final RegistryObject<Attribute> FIRE_DAMAGE_BASE =
            register("fire_damage_base", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> COLD_DAMAGE_BASE =
            register("cold_damage_base", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> LIGHTNING_DAMAGE_BASE =
            register("lightning_damage_base", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> CHAOS_DAMAGE_BASE =
            register("chaos_damage_base", 0.0D, 0.0D, 25565.0D);

    /*非物理属性more*/
    public static final RegistryObject<Attribute> FIRE_DAMAGE_MORE =
            register("fire_damage_more", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> COLD_DAMAGE_MORE =
            register("cold_damage_more", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> LIGHTNING_DAMAGE_MORE =
            register("lightning_damage_more", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> CHAOS_DAMAGE_MORE =
            register("chaos_damage_more", 0.0D, 0.0D, 25565.0D);

    /*非物理属性inc*/
    public static final RegistryObject<Attribute> FIRE_DAMAGE_INC =
            register("fire_damage_inc", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> COLD_DAMAGE_INC =
            register("cold_damage_inc", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> LIGHTNING_DAMAGE_INC =
            register("lightning_damage_inc", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> CHAOS_DAMAGE_INC =
            register("chaos_damage_inc", 0.0D, 0.0D, 25565.0D);

    /*非物理属性耐性*/
    public static final RegistryObject<Attribute> FIRE_RESISTANCE =
            register("fire_resistance", 0.0D, -1.0D, 25565.0D);
    public static final RegistryObject<Attribute> COLD_RESISTANCE =
            register("cold_resistance", 0.0D, -1.0D, 25565.0D);
    public static final RegistryObject<Attribute> LIGHTNING_RESISTANCE =
            register("lightning_resistance", 0.0D, -1.0D, 25565.0D);
    public static final RegistryObject<Attribute> CHAOS_RESISTANCE =
            register("chaos_resistance", 0.0D, -1.0D, 25565.0D);

    /*非物理属性耐性最大値*/
    public static final RegistryObject<Attribute> FIRE_RESISTANCE_MAX =
            register("fire_resistance_max", 0.75D, -25565.0D, 25565.0D);
    public static final RegistryObject<Attribute> COLD_RESISTANCE_MAX =
            register("cold_resistance_max", 0.75D, -25565.0D, 25565.0D);
    public static final RegistryObject<Attribute> LIGHTNING_RESISTANCE_MAX =
            register("lightning_resistance_max", 0.75D, -25565.0D, 25565.0D);
    public static final RegistryObject<Attribute> CHAOS_RESISTANCE_MAX =
            register("chaos_resistance_max", 0.75D, -25565.0D, 25565.0D);

    /*耐性貫通*/
    public static final RegistryObject<Attribute> FIRE_PENETRATION =
            register("fire_penetration", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> COLD_PENETRATION =
            register("cold_penetration", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> LIGHTNING_PENETRATION =
            register("lightning_penetration", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> CHAOS_PENETRATION =
            register("chaos_penetration", 0.0D, 0.0D, 25565.0D);

    /*追加ダメージ*/
    public static final RegistryObject<Attribute> PHYS_ADD =
            register("phys_add", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> FIRE_ADD =
            register("fire_add", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> COLD_ADD =
            register("cold_add", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> LIGHTNING_ADD =
            register("lightning_add", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> CHAOS_ADD =
            register("chaos_add", 0.0D, 0.0D, 25565.0D);

    /*ダメージ変換関係*/
    /*ダメージ変換は物理→雷→冷気→火→混沌の順に限定される*/
    /*A属性のB%をC属性として与える(Damage conversion)*/
    public static final RegistryObject<Attribute> PHYS_TO_FIRE_DAMAGE_CONV =
            register("phys_to_fire_damage_conv", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> PHYS_TO_COLD_DAMAGE_CONV =
            register("phys_to_cold_damage_conv", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> PHYS_TO_LIGHTNING_DAMAGE_CONV =
            register("phys_to_lightning_damage_conv", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> PHYS_TO_CHAOS_DAMAGE_CONV =
            register("phys_to_chaos_damage_conv", 0.0D, 0.0D, 25565.0D);

    public static final RegistryObject<Attribute> LIGHTNING_TO_COLD_DAMAGE_CONV =
            register("lightning_to_cold_damage_conv", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> LIGHTNING_TO_FIRE_DAMAGE_CONV =
            register("lightning_to_fire_damage_conv", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> LIGHTNING_TO_CHAOS_DAMAGE_CONV =
            register("lightning_to_chaos_damage_conv", 0.0D, 0.0D, 25565.0D);

    public static final RegistryObject<Attribute> COLD_TO_FIRE_DAMAGE_CONV =
            register("cold_to_fire_damage_conv", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> COLD_TO_CHAOS_DAMAGE_CONV =
            register("cold_to_chaos_damage_conv", 0.0D, 0.0D, 25565.0D);

    public static final RegistryObject<Attribute> FIRE_TO_CHAOS_DAMAGE_CONV =
            register("fire_to_chaos_damage_conv", 0.0D, 0.0D, 25565.0D);

    /*属性変換加算*/
    public static final RegistryObject<Attribute> PHYS_TO_FIRE_DAMAGE_EXTRA =
            register("phys_to_fire_damage_extra", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> PHYS_TO_COLD_DAMAGE_EXTRA =
            register("phys_to_cold_damage_extra", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> PHYS_TO_LIGHTNING_DAMAGE_EXTRA =
            register("phys_to_lightning_damage_extra", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> PHYS_TO_CHAOS_DAMAGE_EXTRA =
            register("phys_to_chaos_damage_extra", 0.0D, 0.0D, 25565.0D);

    public static final RegistryObject<Attribute> LIGHTNING_TO_COLD_DAMAGE_EXTRA =
            register("lightning_to_cold_damage_extra", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> LIGHTNING_TO_FIRE_DAMAGE_EXTRA =
            register("lightning_to_fire_damage_extra", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> LIGHTNING_TO_CHAOS_DAMAGE_EXTRA =
            register("lightning_to_chaos_damage_extra", 0.0D, 0.0D, 25565.0D);

    public static final RegistryObject<Attribute> COLD_TO_FIRE_DAMAGE_EXTRA =
            register("cold_to_fire_damage_extra", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> COLD_TO_CHAOS_DAMAGE_EXTRA =
            register("cold_to_chaos_damage_extra", 0.0D, 0.0D, 25565.0D);

    public static final RegistryObject<Attribute> FIRE_TO_CHAOS_DAMAGE_EXTRA =
            register("fire_to_chaos_damage_extra", 0.0D, 0.0D, 25565.0D);

    /*被ダメージ変換関係*/
    /*物理からの変換のみ*/
    /*A属性のB%をC属性として受ける(Damage Taken as)*/
    public static final RegistryObject<Attribute> FIRE_DAMAGE_TAKEN_AS =
            register("fire_damage_taken_as", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> COLD_DAMAGE_TAKEN_AS =
            register("cold_damage_taken_as", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> LIGHTNING_DAMAGE_TAKEN_AS =
            register("lightning_damage_taken_as", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> CHAOS_DAMAGE_TAKEN_AS =
            register("chaos_damage_taken_as", 0.0D, 0.0D, 25565.0D);


    /*反射関係*/
    public static final RegistryObject<Attribute> PHYS_REFLECTION =
            register("physics_reflection", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> FIRE_REFLECTION =
            register("fire_reflection", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> COLD_REFLECTION =
            register("cold_reflection", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> LIGHTNING_REFLECTION =
            register("lightning_reflection", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> CHAOS_REFLECTION =
            register("chaos_reflection", 0.0D, 0.0D, 25565.0D);

    /*ブロック関係*/
    public static final RegistryObject<Attribute> BLOCK_CHANCE =
            register("block_chance", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> SPELL_BLOCK_CHANCE =
            register("spell_block_chance", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> BLOCK_CHANCE_MAX =
            register("block_chance_max", 0.75D, 0.0D, 0.9D);

    /*回避関係*/
    /*PoEとは違い%*/
    public static final RegistryObject<Attribute> EVASION_CHANCE =
            register("evasion_chance", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> EVASION_CHANCE_MAX =
            register("evasion_chance_max", 0.75D, 0.0D, 0.9D);

    /*命中率*/
    public static final RegistryObject<Attribute> ACCURACY =
            register("accuracy", 1.0D, 0.0D, 25565.0D);

    /*クリティカル*/
    public static final RegistryObject<Attribute> CRITICAL_CHANCE =
            register("critical_chance", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> CRITICAL_CHANCE_SPELL =
            register("critical_chance_spell", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> CRITICAL_MULTI_GLOBAL =
            register("critical_multi_global", 1.5D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> CRITICAL_MULTI_SPELL =
            register("critical_multi_spell", 1.5D, 0.0D, 25565.0D);

    //クリティカルダメージ軽減
    public static final RegistryObject<Attribute> CRITICAL_DAMAGE_LESS =
            register("critical_damage_less", 0.0D, 0.0D, 25565.0D);

    /*状態異常確率*/
    public static final RegistryObject<Attribute> IGNITE_CHANCE =
            register("ignite_chance", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> FREEZE_CHANCE =
            register("freeze_chance", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> SHOCK_CHANCE =
            register("shock_chance", 0.0D, 0.0D, 25565.0D);

    /*状態異常時間倍率*/
    public static final RegistryObject<Attribute> IGNITE_TIME_INC =
            register("ignite_time_inc", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> FREEZE_TIME_INC =
            register("freeze_time_inc", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> CHILL_TIME_INC =
            register("chill_time_inc", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> SHOCK_TIME_INC =
            register("shock_time_inc", 0.0D, 0.0D, 25565.0D);

    /*状態異常効果倍率*/
    public static final RegistryObject<Attribute> IGNITE_EFFECT_INC =
            register("ignite_effect_inc", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> FREEZE_EFFECT_INC =
            register("freeze_effect_inc", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> CHILL_EFFECT_INC =
            register("chill_effect_inc", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> SHOCK_EFFECT_INC =
            register("shock_effect_inc", 0.0D, 0.0D, 25565.0D);

    /*リーチ関係*/
    public static final RegistryObject<Attribute> LIFE_LEECH =
            register("life_leech", 0.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> LIFE_LEECH_CAP_PER_LEECH =
            register("life_leech_cap_per_leech", 0.1D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> LIFE_LEECH_CAP_PER_SEC =
            register("life_leech_cap_per_sec", 0.2D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> LIFE_LEECH_INSTANT =
            register("life_leech_instant", 0.0D, 0.0D, 1.0D);

    /*ライフゲイン系*/
    public static final RegistryObject<Attribute> LIFE_GAIN_ON_HIT =
            register("life_gain_on_hit", 0.0D, 0.0D, 25565.0D);

    /*チャージ系*/
    public static final RegistryObject<Attribute> ENDURANCE_CHARGE_MAX =
            register("endurance_charge_max", 3.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> FRENZY_CHARGE_MAX =
            register("frenzy_charge_max", 3.0D, 0.0D, 25565.0D);
    public static final RegistryObject<Attribute> POWER_CHARGE_MAX =
            register("power_charge_max", 3.0D, 0.0D, 25565.0D);




    // ---------- 登録用ヘルパー ----------
    private static RegistryObject<Attribute> register(String name, double defaultValue, double min, double max) {
        return ATTRIBUTES.register(name,
                () -> new RangedAttribute("Attribute.name." + MurabitoAttributesMod.MODID + "." + name,
                        defaultValue, min, max)
                        .setSyncable(true)
        );
    }

    // ---------- 呼び出し用 ----------
    @SuppressWarnings("removal")
    public static void register() {
        ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}

