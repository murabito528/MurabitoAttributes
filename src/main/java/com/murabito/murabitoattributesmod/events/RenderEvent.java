package com.murabito.murabitoattributesmod.events;


import com.mojang.blaze3d.systems.RenderSystem;
import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import com.murabito.murabitoattributesmod.affix.AffixReloadListener;
import com.murabito.murabitoattributesmod.items.currency.CurrencyItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MurabitoAttributesMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderEvent {
    @SubscribeEvent
    public static void onRenderGuiPost(ScreenEvent.Render.Post event) {
        Minecraft mc = Minecraft.getInstance();

        // 現在インベントリ画面を開いているかチェック
        if (mc.screen instanceof AbstractContainerScreen<?> screen) {
            // マウス座標を取得
            double mouseX = mc.mouseHandler.xpos() * (double)mc.getWindow().getGuiScaledWidth() / (double)mc.getWindow().getWidth();
            double mouseY = mc.mouseHandler.ypos() * (double)mc.getWindow().getGuiScaledHeight() / (double)mc.getWindow().getHeight();

            var carriedItem = screen.getMenu().getCarried();

            // カレンシーを掴んでいる時のみ
            if (!carriedItem.isEmpty()) {
                if(carriedItem.getItem() instanceof CurrencyItem) {
                    for (Slot slot : screen.getMenu().slots) {
                        if (isMouseOverSlot(screen, slot, (int) mouseX, (int) mouseY)) {
                            if (slot.hasItem()) {
                                GuiGraphics guiGraphics = event.getGuiGraphics();
                                //先に描画されているものを確定(いらないかもだけど互換性のため)
                                guiGraphics.flush();

                                //ツールチップの描画を呼ぶ
                                guiGraphics.renderTooltip(
                                        mc.font,
                                        slot.getItem(),
                                        (int) mouseX + 16,
                                        (int) mouseY
                                );

                                //ツールチップの描画を確定
                                guiGraphics.flush();
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private static boolean isMouseOverSlot(AbstractContainerScreen<?> screen, Slot slot, int mouseX, int mouseY) {
        int left = screen.getGuiLeft();
        int top = screen.getGuiTop();
        return mouseX >= left + slot.x && mouseX < left + slot.x + 16 &&
                mouseY >= top + slot.y && mouseY < top + slot.y + 16;
    }
}
