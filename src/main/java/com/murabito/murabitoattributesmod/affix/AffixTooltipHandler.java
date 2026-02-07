package com.murabito.murabitoattributesmod.affix;

import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = MurabitoAttributesMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AffixTooltipHandler {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent e) {

        if (FMLEnvironment.dist != Dist.CLIENT) return;

        var stack = e.getItemStack();

        var affixes = AffixNbt.read(stack);
        int ilvl = AffixNbt.getIlvl(stack);

        boolean showDetails = Screen.hasAltDown();

        if(ilvl<=0) return;
        e.getToolTip().add(Component.literal("")); // 空行
        if(showDetails) e.getToolTip().add(Component.literal("itemLevel: " + ilvl).withStyle(ChatFormatting.DARK_GRAY));
        if (affixes.isEmpty()) return;
        e.getToolTip().add(Component.literal("[Affixes]").withStyle(ChatFormatting.DARK_GRAY));

        for (var inst : affixes) {
            var def = AffixRegistry.get(inst.id());

            String name = (def != null) ? def.name() : inst.id().toString();
            // rollの表示はとりあえず%
            int pct = Math.round(inst.roll() * 100f);

            MutableComponent line =
                    Component.literal(" - " + name).withStyle(ChatFormatting.GRAY);

            if(showDetails) {
                line = line.append(
                        Component.literal(" (T" + inst.tier() + ", " + pct + "%) [" + inst.type().charAt(0) + "]").withStyle(ChatFormatting.DARK_GRAY)
                );
            }

            e.getToolTip().add(line);
        }
        if(!showDetails){
            e.getToolTip().add(Component.literal("press alt to show details").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
