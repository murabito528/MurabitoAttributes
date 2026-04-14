package com.murabito.murabitoattributesmod.affix;

import com.google.common.collect.Multimap;
import com.murabito.murabitoattributesmod.MurabitoAttributesMod;
import com.murabito.murabitoattributesmod.affix.records.AffixStat;
import com.murabito.murabitoattributesmod.affix.records.AffixTier;
import com.murabito.murabitoattributesmod.attributes.CustomAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = MurabitoAttributesMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AffixTooltipHandler {

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent e) {
        if (FMLEnvironment.dist != Dist.CLIENT) return;
        displayStats(e);
        displayAffixes(e);
    }

    private static void displayAffixes(ItemTooltipEvent e){
        var stack = e.getItemStack();

        var affixes = AffixNbt.read(stack);
        int ilvl = AffixNbt.getIlvl(stack);

        boolean showDetails = Screen.hasAltDown();

        if(ilvl<=0) return;
        e.getToolTip().add(Component.literal("")); // 空行
        if(showDetails) e.getToolTip().add(Component.literal("itemLevel: " + ilvl).withStyle(ChatFormatting.DARK_GRAY));
        if (affixes.isEmpty()) return;
        e.getToolTip().add(Component.literal("[Affixes]").withStyle(ChatFormatting.DARK_GRAY));

        for (var affix : affixes) {
            var affixDef = AffixRegistry.get(affix.id());
            String name = (affixDef != null) ? affixDef.name() : affix.id().toString();

            //#を数値に入れ替える
            List<AffixStat> affixStats = affixDef.tiers().stream()
                    .filter(t -> t.tier() == affix.tier())
                    .findFirst()
                    .map(AffixTier::stats)
                    .orElse(Collections.emptyList());

            for(var stat : affixStats) {
                double rawAmount = stat.min() + (stat.max() - stat.min()) * affix.roll();

                // 表示用の数値計算
                double displayAmount;
                String targetPlaceholder;

                if (name.contains("#%")) {
                    // #% の場合は100倍にして、小数点第2位で丸める
                    displayAmount = Math.floor(rawAmount * 100 * 100) / 100.0;
                    targetPlaceholder = "#%";
                    // 置換後の文字列に % を付け直す（例: "5% increased..."）
                    String replacement = displayAmount + "%";
                    name = name.replaceFirst(targetPlaceholder, replacement);
                } else {
                    // 通常の # の場合はそのまま（小数点第2位で丸める）
                    displayAmount = Math.floor(rawAmount * 100) / 100.0;
                    targetPlaceholder = "#";
                    name = name.replaceFirst(targetPlaceholder, String.valueOf(displayAmount));
                }
            }
            // rollの表示はとりあえず%
            int pct = Math.round(affix.roll() * 100f);

            MutableComponent line =
                    Component.literal(" - " + name).withStyle(ChatFormatting.GRAY);

            if(showDetails) {
                line = line.append(
                        Component.literal(" (T" + affix.tier() + ", " + pct + "%) [" + affix.type().charAt(0) + "]").withStyle(ChatFormatting.DARK_GRAY)
                );
            }

            e.getToolTip().add(line);
        }
        if(!showDetails){
            e.getToolTip().add(Component.literal("press alt to show details").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    private static void displayStats(ItemTooltipEvent e){
        //attributeの設定で消えた攻撃力とかを自前で表示
        var player = e.getEntity();

        boolean showDetails = Screen.hasAltDown();
        var stack = e.getItemStack();
        var affixes = AffixNbt.read(stack);
        int ilvl = AffixNbt.getIlvl(stack);
        if(ilvl<=0 || affixes.isEmpty()) return;

        var modifiers = stack.getAttributeModifiers(EquipmentSlot.MAINHAND);
        //物理ダメージ
        DamageRange physRange = calcItemDamage(modifiers, player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE),
                Attributes.ATTACK_DAMAGE,
                CustomAttributes.PHYS_ADD_MIN.get(),
                CustomAttributes.PHYS_ADD_MAX.get(),
                CustomAttributes.PHYS_DAMAGE_INC.get(),
                CustomAttributes.PHYS_DAMAGE_MORE.get());
        MutableComponent line = Component.literal("物理ダメージ: ").withStyle(ChatFormatting.WHITE);
        if(physRange.min==physRange.max) {
            line.append(Component.literal(String.format("%,.0f", physRange.min)).withStyle(ChatFormatting.BLUE));
        }else{
            line.append(Component.literal(String.format("%,.0f-%,.0f", physRange.min, physRange.max)).withStyle(ChatFormatting.BLUE));
        }
        e.getToolTip().add(line);

        //属性ダメージ
        DamageRange fireRange = calcItemDamage(modifiers, 0,
                CustomAttributes.FIRE_DAMAGE_BASE.get(),
                CustomAttributes.FIRE_ADD_MIN.get(),
                CustomAttributes.FIRE_ADD_MAX.get(),
                CustomAttributes.FIRE_DAMAGE_INC.get(),
                CustomAttributes.FIRE_DAMAGE_MORE.get());
        DamageRange coldRange = calcItemDamage(modifiers, 0,
                CustomAttributes.COLD_DAMAGE_BASE.get(),
                CustomAttributes.COLD_ADD_MIN.get(),
                CustomAttributes.COLD_ADD_MAX.get(),
                CustomAttributes.COLD_DAMAGE_INC.get(),
                CustomAttributes.COLD_DAMAGE_MORE.get());
        DamageRange lightningRange = calcItemDamage(modifiers, 0,
                CustomAttributes.LIGHTNING_DAMAGE_BASE.get(),
                CustomAttributes.LIGHTNING_ADD_MIN.get(),
                CustomAttributes.LIGHTNING_ADD_MAX.get(),
                CustomAttributes.LIGHTNING_DAMAGE_INC.get(),
                CustomAttributes.LIGHTNING_DAMAGE_MORE.get());

        if(fireRange.min>0||coldRange.min>0||lightningRange.min>0) {
            line = Component.literal("元素ダメージ: ").withStyle(ChatFormatting.WHITE);
            if(fireRange.min>0) {
                if (fireRange.min == fireRange.max) {
                    line.append(Component.literal(String.format("%,.0f ", fireRange.min)).withStyle(ChatFormatting.RED));
                } else {
                    line.append(Component.literal(String.format("%,.0f-%,.0f ", fireRange.min, fireRange.max)).withStyle(ChatFormatting.RED));
                }
            }
            if(coldRange.min>0) {
                if (coldRange.min == coldRange.max) {
                    line.append(Component.literal(String.format("%,.0f ", coldRange.min)).withStyle(ChatFormatting.AQUA));
                } else {
                    line.append(Component.literal(String.format("%,.0f-%,.0f ", coldRange.min, coldRange.max)).withStyle(ChatFormatting.AQUA));
                }
            }
            if(lightningRange.min>0) {
                if (lightningRange.min == lightningRange.max) {
                    line.append(Component.literal(String.format("%,.0f ", lightningRange.min)).withStyle(ChatFormatting.YELLOW));
                } else {
                    line.append(Component.literal(String.format("%,.0f-%,.0f ", lightningRange.min, lightningRange.max)).withStyle(ChatFormatting.YELLOW));
                }
            }
            e.getToolTip().add(line);
        }
        //混沌ダメージ
        DamageRange chaosRange = calcItemDamage(modifiers, 0,
                CustomAttributes.CHAOS_DAMAGE_BASE.get(),
                CustomAttributes.CHAOS_ADD_MIN.get(),
                CustomAttributes.CHAOS_ADD_MAX.get(),
                CustomAttributes.CHAOS_DAMAGE_INC.get(),
                CustomAttributes.CHAOS_DAMAGE_MORE.get());

        if(chaosRange.min>0){
            line = Component.literal("混沌ダメージ: ").withStyle(ChatFormatting.WHITE);
            if(chaosRange.min==chaosRange.max) {
                line.append(Component.literal(String.format("%,.0f", chaosRange.min)).withStyle(ChatFormatting.LIGHT_PURPLE));
            }else{
                line.append(Component.literal(String.format("%,.0f-%,.0f", chaosRange.min, chaosRange.max)).withStyle(ChatFormatting.LIGHT_PURPLE));
            }
            e.getToolTip().add(line);
        }

        if(showDetails){
            e.getToolTip().add(Component.literal("表記されているダメージ値は武器による一部補正のみを参照しています").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public record DamageRange(double min, double max) {}

    private static DamageRange calcItemDamage(
            Multimap<Attribute, AttributeModifier> modifiers,
            double playerBase,
            Attribute baseAttr,
            Attribute minAttr,
            Attribute maxAttr,
            Attribute incAttr,
            Attribute moreAttr
    ) {
        double base = playerBase;
        double min = 0;
        double max = 0;
        double inc = 0.0;
        double more = 1.0;

        // 各Modifierを集計
        for (var mod : modifiers.get(baseAttr)) base += mod.getAmount();
        for (var mod : modifiers.get(minAttr))  min  += mod.getAmount();
        for (var mod : modifiers.get(maxAttr))  max  += mod.getAmount();
        for (var mod : modifiers.get(incAttr))  inc  += mod.getAmount();
        for (var mod : modifiers.get(moreAttr)) more *= (1.0 + mod.getAmount());

        double scale = (1+inc) * more;
        return new DamageRange((base + min) * scale, (base + max) * scale);
    }
}
