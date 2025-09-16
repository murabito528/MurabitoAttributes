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
            // CustomAttributes に登録済みの全 Attribute を自動で追加
            CustomAttributes.ATTRIBUTES.getEntries().forEach(regObj -> event.add(type, regObj.get()));
        });
    }
}

