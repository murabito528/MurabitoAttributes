package com.murabito.murabitoattributesmod.affix;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.murabito.murabitoattributesmod.affix.currency.CurrencyActions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Locale;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AffixCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent e) {
        register(e.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("murabitoAffix")
                .requires(src -> src.hasPermission(2));

        root.then(Commands.literal("set_ilvl")
                .then(Commands.argument("ilvl", IntegerArgumentType.integer(0)) // 0以上に制限
                        .executes(ctx -> {
                            var src = ctx.getSource();
                            var stack = src.getPlayerOrException().getMainHandItem();
                            if (stack.isEmpty()) {
                                src.sendFailure(Component.literal("Hold item."));
                                return 0;
                            }

                            int ilvl = IntegerArgumentType.getInteger(ctx, "ilvl");
                            AffixNbt.setIlvl(stack, ilvl);

                            src.sendSuccess(() ->
                                    Component.literal("Set item level to " + ilvl), true);
                            return 1;
                        })));


        root.then(Commands.literal("add_prefix")
                .then(Commands.argument("id", ResourceLocationArgument.id())
                        .suggests(suggestAffixIds(AffixType.PREFIX))
                        .executes(ctx -> {
                            ResourceLocation id = ResourceLocationArgument.getId(ctx, "id");
                            try {
                                return add(ctx.getSource(), AffixType.PREFIX, id);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })));

        root.then(Commands.literal("add_suffix")
                .then(Commands.argument("id", ResourceLocationArgument.id())
                        .suggests(suggestAffixIds(AffixType.SUFFIX))
                        .executes(ctx -> {
                            ResourceLocation id = ResourceLocationArgument.getId(ctx, "id");
                            try {
                                return add(ctx.getSource(), AffixType.SUFFIX, id);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })));

        root.then(Commands.literal("clear")
                .executes(ctx -> {
                    ItemStack stack = ctx.getSource().getPlayerOrException().getMainHandItem();
                    AffixNbt.resetAll(stack);
                    ctx.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal("Cleared affixes"), true);
                    return 1;
                }));

        root.then(Commands.literal("alteration")
                .executes(ctx -> {
                    var src = ctx.getSource();
                    var stack = src.getPlayerOrException().getMainHandItem();
                    if (stack.isEmpty()) { src.sendFailure(Component.literal("Hold item.")); return 0; }

                    var res = CurrencyActions.alteration(stack, RandomSource.create());
                    src.sendSuccess(() -> Component.literal(res.message()), true);
                    return 1;
                }));

        root.then(Commands.literal("chaos")
                .executes(ctx -> {
                    var src = ctx.getSource();
                    var stack = src.getPlayerOrException().getMainHandItem();
                    if (stack.isEmpty()) { src.sendFailure(Component.literal("Hold item.")); return 0; }

                    var res = CurrencyActions.chaos(stack, RandomSource.create());
                    src.sendSuccess(() -> Component.literal(res.message()), true);
                    return 1;
                }));

        root.then(Commands.literal("exalt")
                .executes(ctx -> {
                    var src = ctx.getSource();
                    var stack = src.getPlayerOrException().getMainHandItem();
                    var res = CurrencyActions.exalt(stack, 1, RandomSource.create());
                    src.sendSuccess(() -> Component.literal(res.message()), true);
                    return 1;
                }));

        root.then(Commands.literal("annul")
                .executes(ctx -> {
                    var src = ctx.getSource();
                    var stack = src.getPlayerOrException().getMainHandItem();
                    var res = CurrencyActions.annul(stack, RandomSource.create());
                    src.sendSuccess(() -> Component.literal(res.message()), true);
                    return 1;
                }));

        root.then(Commands.literal("scour")
                .executes(ctx -> {
                    var src = ctx.getSource();
                    var stack = src.getPlayerOrException().getMainHandItem();
                    var res = CurrencyActions.scour(stack);
                    src.sendSuccess(() -> Component.literal(res.message()), true);
                    return 1;
                }));
        root.then(Commands.literal("lock_prefix")
                .then(Commands.argument("v", BoolArgumentType.bool())
                        .executes(ctx -> {
                            var src = ctx.getSource();
                            var stack = src.getPlayerOrException().getMainHandItem();
                            boolean v = BoolArgumentType.getBool(ctx, "v");
                            AffixNbt.setPrefixLocked(stack, v);
                            src.sendSuccess(() -> Component.literal("lock_prefix=" + v), true);
                            return 1;
                        })));
        root.then(Commands.literal("lock_suffix")
                .then(Commands.argument("v", BoolArgumentType.bool())
                        .executes(ctx -> {
                            var src = ctx.getSource();
                            var stack = src.getPlayerOrException().getMainHandItem();
                            boolean v = BoolArgumentType.getBool(ctx, "v");
                            AffixNbt.setSuffixLocked(stack, v);
                            src.sendSuccess(() -> Component.literal("lock_suffix=" + v), true);
                            return 1;
                        })));

        dispatcher.register(root);
    }

    private static int add(CommandSourceStack src, AffixType type, ResourceLocation id) throws Exception {
        ItemStack stack = src.getPlayerOrException().getMainHandItem();
        if (stack.isEmpty()) {
            src.sendFailure(Component.literal("Hold an item in main hand."));
            return 0;
        }

        var def = AffixRegistry.get(id);
        if (def == null) {
            src.sendFailure(Component.literal("Unknown affix id: " + id));
            return 0;
        }
        if (def.type() != type) {
            src.sendFailure(Component.literal("Affix type mismatch. def=" + def.type() + ", cmd=" + type));
            return 0;
        }

        int tier = def.tiers().get(0).tier();
        float roll = new Random().nextFloat();

        AffixNbt.add(stack, type, id, tier, roll);
        src.sendSuccess(() -> Component.literal("Added " + type + " " + id), true);
        return 1;
    }

    // AffixCommand クラス内に追記
    private static SuggestionProvider<CommandSourceStack> suggestAffixIds(AffixType type) {
        return (ctx, builder) -> {
            String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

            // PREFIX/SUFFIX で絞り込む
            var ids = AffixRegistry.byType(type).stream()
                    .map(def -> def.id().toString())
                    .toList();

            // 予測候補を返す（部分一致）
            return SharedSuggestionProvider.suggest(ids, builder);
        };
    }
}
