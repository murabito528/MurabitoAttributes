package com.murabito.murabitoattributesmod.util;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class Util {
    /** 属性値を安全に取得（nullなら0） */
    public static double getAttributeValueOrZero(LivingEntity entity, net.minecraft.world.entity.ai.attributes.Attribute attr) {
        AttributeInstance inst = entity.getAttribute(attr);
        return inst != null ? inst.getValue() : 0.0;
    }

    /** アイテムの装備スロットを取得 **/
    public static EquipmentSlot getEquipmentSlot(ItemStack stack){
        EquipmentSlot slot;
        if (stack.getItem() instanceof ArmorItem armorItem) {
            slot = armorItem.getEquipmentSlot();
        }else{
            slot = stack.getEquipmentSlot();
        }
        if(slot==null){
            slot = EquipmentSlot.MAINHAND;
        }
        //MurabitoAttributesMod.LOGGER.info(slot.getName());
        return slot;
    }
}
