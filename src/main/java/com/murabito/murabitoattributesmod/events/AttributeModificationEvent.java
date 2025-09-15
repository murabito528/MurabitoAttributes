package com.murabito.murabitoattributesmod.events;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import com.murabito.murabitoattributesmod.attributes.CustomAttributes;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MurabitoAttributesMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AttributeModificationEvent {
    @SubscribeEvent
    public static void onAttributeModification(EntityAttributeModificationEvent event) {
        // すべてのEntityType（LivingEntity系）に追加
        event.getTypes().forEach(type -> {
            event.add(type, CustomAttributes.FIRE_DAMAGE_BASE.get());
            event.add(type, CustomAttributes.PHYS_TO_FIRE_DAMAGE_CONV.get());
            //event.add(type, CustomAttributes.FIRE_DAMAGE_MUL.get());
            event.add(type, CustomAttributes.FIRE_RESISTANCE.get());
            event.add(type, CustomAttributes.ICE_DAMAGE_BASE.get());
            event.add(type, CustomAttributes.PHYS_TO_ICE_DAMAGE_CONV.get());
            //event.add(type, CustomAttributes.ICE_DAMAGE_MUL.get());
            event.add(type, CustomAttributes.ICE_RESISTANCE.get());
            event.add(type, CustomAttributes.LIGHTNING_DAMAGE_BASE.get());
            event.add(type, CustomAttributes.PHYS_TO_LIGHTNING_DAMAGE_CONV.get());
            //event.add(type, CustomAttributes.LIGHTNING_DAMAGE_MUL.get());
            event.add(type, CustomAttributes.LIGHTNING_RESISTANCE.get());

            event.add(type,CustomAttributes.PHYS_REFLECTION.get());
            event.add(type,CustomAttributes.FIRE_REFLECTION.get());
            event.add(type,CustomAttributes.ICE_REFLECTION.get());
            event.add(type,CustomAttributes.LIGHTNING_REFLECTION.get());
        });
    }
}
