package com.murabito.murabitoattributesmod.affix.records;

import com.murabito.murabitoattributesmod.affix.AffixType;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record AffixDefinition(
        ResourceLocation id,
        AffixType type,
        String name,
        List<String> groups,
        List<String> tags,
        List<AffixTier> tiers,
        List<String> targets,
        ResourceLocation sourceFileKey
) {}
