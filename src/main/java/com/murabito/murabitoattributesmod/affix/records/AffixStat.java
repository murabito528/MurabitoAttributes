package com.murabito.murabitoattributesmod.affix.records;

import net.minecraft.resources.ResourceLocation;

public record AffixStat(
        ResourceLocation attribute,
        int op,
        double min,
        double max
) {}
