package com.murabito.murabitoattributesmod.network;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import com.murabito.murabitoattributesmod.items.currency.CurrencyItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CurrencyClickPacket {
    private final int slotIndex;

    // コンストラクタ
    public CurrencyClickPacket(int slotIndex) {
        this.slotIndex = slotIndex;
    }

    // ネットワーク経由で送るための書き込み
    public static void encode(CurrencyClickPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.slotIndex);
    }

    // ネットワークから届いたデータを読み取る
    public static CurrencyClickPacket decode(FriendlyByteBuf buffer) {
        return new CurrencyClickPacket(buffer.readInt());
    }

    // サーバー側で実行される処理
    public static void handle(CurrencyClickPacket msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // ここはサーバー側スレッドで実行される
            ServerPlayer player = context.getSender();
            if (player != null) {
                // プレイヤーが開いているメニューから該当スロットを取得
                ItemStack currencyStack = player.containerMenu.getCarried(); // マウスで掴んでいるもの

                //クリエだとここらへんで失敗するらしい

                // 掴んでいるのがカレンシーアイテムなら効果を発動
                Slot slot =player.containerMenu.getSlot(msg.slotIndex);
                if (currencyStack.getItem() instanceof CurrencyItem currencyItem) {
                    //applyEffect をサーバー側で実行
                    if (currencyItem.applyEffect(slot.getItem(), currencyStack, player)) {
                        // 成功したらアイテムを減らす等の処理（サーバーなので同期される）
                        currencyStack.shrink(1);
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
