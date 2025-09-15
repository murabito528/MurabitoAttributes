package com.murabito.murabitoattributesmod.gamerule;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = MurabitoAttributesMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GameRuleRegister {
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        CustomGameRules.register();
    }
}
