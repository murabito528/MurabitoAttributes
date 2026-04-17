package com.murabito.murabitoattributesmod.items.currency;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import com.murabito.murabitoattributesmod.network.CurrencyClickPacket;
import com.murabito.murabitoattributesmod.network.ModMessages;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class CurrencyItem extends Item {

    public CurrencyItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack currencyStack, Slot slot, ClickAction clickAction, Player player) {
        // 右クリック以外は無視
        if (clickAction != ClickAction.SECONDARY) return false;
        ItemStack targetStack = slot.getItem();
        if (targetStack.isEmpty()) return false;

        // 対象アイテムが加工可能かチェック (例: 装備品のみ、など)
        if (!canApplyTo(targetStack)) return false;

        // サーバー側でのみメインの処理を実行 (パケット同期ミス防止)
        if (player.level().isClientSide) {
            if (applyEffect(targetStack, currencyStack, player)) {
                //パケットを送る
                ModMessages.sendToServer(new CurrencyClickPacket(slot.index));
                // 成功音を鳴らす
                player.playNotifySound(SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                return true;
            }
        }
        return true;
    }

    /**
     * このカレンシーがそのアイテムに使用できるか (条件判定)
     */
    abstract boolean canApplyTo(ItemStack target);

    /**
     * 実際の加工ロジック (NBTの書き換えなど)
     * @return 成功した場合はtrue
     */
    public abstract boolean applyEffect(ItemStack target, ItemStack currency, Player player);
}
