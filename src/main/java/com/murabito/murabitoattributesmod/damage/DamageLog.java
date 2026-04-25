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
        if (hitData.attacker == null || hitData.attacker.level().isClientSide) return;
        if (!(hitData.attacker.level() instanceof ServerLevel level)) return;
        if (!level.getGameRules().getBoolean(CustomGameRules.SHOW_DAMAGE_LOG)) return;
        if(level.isClientSide) return;

        Component message = Component.literal(msg);
        // サーバーに接続している全プレイヤーに送信
        level.getServer().getPlayerList().broadcastSystemMessage(message, false);

    }
}
