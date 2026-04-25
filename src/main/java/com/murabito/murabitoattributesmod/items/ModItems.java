package com.murabito.murabitoattributesmod.items;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import com.murabito.murabitoattributesmod.items.currency.AlterationOrbItem;
import com.murabito.murabitoattributesmod.items.currency.ChaosOrbItem;
import com.murabito.murabitoattributesmod.items.currency.ExaltedOrbItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.murabito.murabitoattributesmod.MurabitoAttributesMod.MODID;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> ORB_CHAOS =
            ITEMS.register("orb_chaos", () -> new ChaosOrbItem(new Item.Properties()));
    public static final RegistryObject<Item> ORB_ALTERATION =
            ITEMS.register("orb_alteration", () -> new AlterationOrbItem(new Item.Properties()));
    public static final RegistryObject<Item> ORB_EXALTED =
            ITEMS.register("orb_exalted", () -> new ExaltedOrbItem(new Item.Properties()));
}