package com.murabito.murabitoattributesmod.events;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import com.murabito.murabitoattributesmod.damage.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = MurabitoAttributesMod.MODID)
public class DamageEvent {


    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        Entity attackerEntity = source.getEntity();

        if(!source.is(ModDamageTypeTags.IS_TRACKING)) return;

        HitData hitData = HitDataFactory.create(target,attackerEntity,source,event.getAmount(),true);
        DamagePipeLine.get().process(hitData);
        event.setAmount((float)hitData.getTotalDamagePostMitigation(ModDamageType.PHYSICAL));
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {

    }
}