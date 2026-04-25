package com.murabito.murabitoattributesmod.items.currency;

import com.murabito.murabitoattributesmod.affix.currency.CurrencyActions;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ExaltedOrbItem extends CurrencyItem{
    public ExaltedOrbItem(Properties properties) {

        super(properties);
    }

    @Override
    protected boolean canApplyTo(ItemStack target) {
        return true;
    }

    @Override
    public boolean applyEffect(ItemStack target, ItemStack currency, Player player) {
        CurrencyActions.exalt(target, RandomSource.create());
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.literal("マジック、レアのアイテムにランダムなモッドを1つ追加する").withStyle(ChatFormatting.DARK_GRAY));

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
