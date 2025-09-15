package com.murabito.murabitoattributesmod.gamerule;

import net.minecraft.world.level.GameRules;

public class CustomGameRules {
    public static GameRules.Key<GameRules.BooleanValue> SHOW_DAMAGE_LOG;

    public static void register() {
        SHOW_DAMAGE_LOG = GameRules.register(
                "showDamageLog",
                GameRules.Category.MISC,
                GameRules.BooleanValue.create(false) // デフォルト false
        );
    }
}