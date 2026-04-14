package com.murabito.murabitoattributesmod.affix.records;

import java.util.List;

public record AffixTier(
        int tier,
        int minIlvl,
        int weight,
        List<AffixStat> stats
) {}
