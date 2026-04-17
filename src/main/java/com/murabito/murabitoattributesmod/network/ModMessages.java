package com.murabito.murabitoattributesmod.network;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@SuppressWarnings("removal")
public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() { return packetId++; }

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(MurabitoAttributesMod.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        // パケットの登録
        INSTANCE.messageBuilder(CurrencyClickPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(CurrencyClickPacket::decode)
                .encoder(CurrencyClickPacket::encode)
                .consumerMainThread(CurrencyClickPacket::handle)
                .add();
    }

    // クライアントからサーバーへ送るためのメソッド
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}