package com.murabito.murabitoattributesmod.affix;

import com.murabito.murabitoattributesmod.affix.records.AffixDefinition;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class AffixRegistry {
    private static final Map<ResourceLocation, AffixDefinition> BY_ID = new ConcurrentHashMap<>();

    private AffixRegistry() {}

    public static void clear() {
        BY_ID.clear();
    }

    public static boolean contains(ResourceLocation id) {
        return BY_ID.containsKey(id);
    }

    public static void register(AffixDefinition def) {
        BY_ID.put(def.id(), def);
    }

    public static AffixDefinition get(ResourceLocation id) {
        return BY_ID.get(id);
    }

    public static Collection<AffixDefinition> all() {
        return Collections.unmodifiableCollection(BY_ID.values());
    }

    public static List<AffixDefinition> byType(AffixType type) {
        return BY_ID.values().stream().filter(d -> d.type() == type).toList();
    }

    public static Set<ResourceLocation> ids() {
        return Collections.unmodifiableSet(BY_ID.keySet());
    }
}