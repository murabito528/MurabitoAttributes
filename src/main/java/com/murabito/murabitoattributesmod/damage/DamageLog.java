package com.murabito.murabitoattributesmod.damage;

import com.murabito.murabitoattributesmod.gamerule.CustomGameRules;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

import java.util.logging.Level;

public class DamageLog {
    /**
     * 攻撃者がプレイヤーであればmsgを通知
     * @param hitData
     * @param msg
     */
    public static void log(HitData hitData, String msg){
        if(!(hitData.attacker instanceof Player player)) return;

        ServerLevel level = (ServerLevel) player.level();
        if (!level.getGameRules().getBoolean(CustomGameRules.SHOW_DAMAGE_LOG)) return;
        if(level.isClientSide) return;
        player.sendSystemMessage(Component.literal(msg));

    }
}
