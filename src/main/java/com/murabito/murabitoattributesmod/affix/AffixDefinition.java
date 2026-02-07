package com.murabito.murabitoattributesmod.affix;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record AffixDefinition(
        ResourceLocation id,
        AffixType type,
        String name,
        List<String> groups,
        List<String> tags,
        List<AffixTier> tiers,
        ResourceLocation sourceFileKey // デバッグ用（どのjsonから来たか）
) {}
