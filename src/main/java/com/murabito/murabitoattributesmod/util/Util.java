package com.murabito.murabitoattributesmod.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class Util {
    /** 属性値を安全に取得（nullなら0） */
    public static double getAttributeValueOrZero(LivingEntity entity, net.minecraft.world.entity.ai.attributes.Attribute attr) {
        AttributeInstance inst = entity.getAttribute(attr);
        return inst != null ? inst.getValue() : 0.0;
    }
}
