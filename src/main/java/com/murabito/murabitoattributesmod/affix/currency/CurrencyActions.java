package com.murabito.murabitoattributesmod.affix.currency;

import com.murabito.murabitoattributesmod.affix.*;
import com.murabito.murabitoattributesmod.affix.records.AffixDefinition;
import com.murabito.murabitoattributesmod.affix.records.AffixTier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;

import java.util.*;
import java.util.stream.Collectors;

import static com.murabito.murabitoattributesmod.affix.AffixNbt.*;

public final class CurrencyActions {
    private CurrencyActions() {}

    // rare
    public static final int RARE_MIN_AFFIX = 4;
    public static final int RARE_MAX_AFFIX = 6;

    private static final Set<String> NO_TAGS = Collections.emptySet();

    // ======= Public APIs =======

    /** Alteration: 全リロール（magic: 1~2個）。ロックを尊重する */
    //マジック以下のアイテムをランダムなマジックにリロール
    public static RollResult alteration(ItemStack stack, int ilvl, RandomSource rng) {
        if(getRarity(stack)==Rarity.RARE||getRarity(stack)==Rarity.UNIQUE){
            return new RollResult("レアリティがマジック以下である必要があります");
        }
        clearRespectingLocks(stack);

        addUpTo(stack,ilvl,rng,1,2,1,1,NO_TAGS,NO_TAGS);

        setRarity(stack, Rarity.MAGIC);
        AffixNbt.applyAffixAttributes(stack);
        return new RollResult("alteration");
    }

    public static RollResult alteration(ItemStack stack, RandomSource rng) {
        int ilvl = AffixNbt.getIlvl(stack);
        if(ilvl<=0) {
            AffixNbt.setIlvl(stack,1);
            ilvl=1;
        }
        return alteration(stack, ilvl, rng);
    }

    private static void addUpTo(ItemStack stack, int ilvl, RandomSource rng,int minAffix, int maxAffix, int maxPrefix, int maxSuffix, Set<String> requireTags, Set<String> forbidTags){
        int need = minAffix + rng.nextInt(maxAffix - minAffix + 1);

        for (int i = 0; i < need; i++) {
            if(count(stack, AffixType.PREFIX) + count(stack, AffixType.SUFFIX)>=need) break;

            AffixType type = pickTypeForAdd(stack, rng,maxPrefix,maxSuffix);
            if (type == null) break;
            boolean ok = addOneRandom(stack, type, ilvl, rng, requireTags, forbidTags);
            if (!ok) {
                AffixType other = (type == AffixType.PREFIX) ? AffixType.SUFFIX : AffixType.PREFIX;
                if (canAdd(stack, other, maxPrefix, maxSuffix)) {
                    ok = addOneRandom(stack, other, ilvl, rng, requireTags, forbidTags);
                }
            }
            if (!ok) break;
        }
    }

    private static boolean canAdd(ItemStack stack, AffixType type, int maxPrefix, int maxSuffix) {
        boolean lockP = AffixNbt.isPrefixLocked(stack);
        boolean lockS = AffixNbt.isSuffixLocked(stack);

        int p = count(stack, AffixType.PREFIX);
        int s = count(stack, AffixType.SUFFIX);

        if (type == AffixType.PREFIX) return !lockP && p < maxPrefix;
        else return !lockS && s < maxSuffix;
    }

    /** Chaos: 全リロール（既存削除→4~6個付与。prefix/suffix上限あり） */
    //レア以下のアイテムをランダムなレアにリロール
    public static RollResult chaos(ItemStack stack, int ilvl, RandomSource rng) {
        if(getRarity(stack)==Rarity.UNIQUE){
            return new RollResult("レアリティがレア以下である必要があります");
        }
        clearRespectingLocks(stack);
        addUpTo(stack,ilvl,rng,3,6,3,3,NO_TAGS,NO_TAGS);
        setRarity(stack,Rarity.RARE);

        AffixNbt.applyAffixAttributes(stack);
        return new RollResult("chaos");
    }
    public static RollResult chaos(ItemStack stack, RandomSource rng) {
        int ilvl = AffixNbt.getIlvl(stack);
        if(ilvl<=0) {
            AffixNbt.setIlvl(stack,1);
            ilvl=1;
        }
        return chaos(stack, ilvl, rng);
    }

    /** Exalt: 空きがあれば1個追加（ロック尊重） */
    public static RollResult exalt(ItemStack stack, int ilvl, RandomSource rng) {
        if(getRarity(stack)!=Rarity.RARE){
            return new RollResult("レアリティがレアである必要があります");
        }
        addUpTo(stack,ilvl,rng,3,6,3,3,NO_TAGS,NO_TAGS);
        AffixNbt.applyAffixAttributes(stack);
        return new RollResult("exalt");
    }

    /** Annul: どれか1個削除（ロック尊重） */
    public static RollResult annul(ItemStack stack, RandomSource rng) {
        if(getRarity(stack)==Rarity.NORMAL){
            return new RollResult("レアリティがマジック以上である必要があります");
        }
        boolean lockP = AffixNbt.isPrefixLocked(stack);
        boolean lockS = AffixNbt.isSuffixLocked(stack);

        if (lockP && lockS) return new RollResult("annul (both locked)");

        boolean ok;
        if (lockP) {
            // prefixは消せない → suffixから消す
            ok = AffixNbt.removeRandomByType(stack, AffixType.SUFFIX, rng);
        } else if (lockS) {
            // suffixは消せない → prefixから消す
            ok = AffixNbt.removeRandomByType(stack, AffixType.PREFIX, rng);
        } else {
            // どっちもOK → 全体から
            ok = AffixNbt.removeRandom(stack, rng);
        }
        AffixNbt.applyAffixAttributes(stack);
        return new RollResult(ok ? "annul" : "annul (no affix)");
    }

    /** Scour: 全消し */
    public static RollResult scour(ItemStack stack) {
        if(getRarity(stack)==Rarity.NORMAL){
            return new RollResult("ノーマルレアリティのアイテムには実行できません");
        }
        AffixNbt.resetAll(stack);
        clearRarity(stack);
        AffixNbt.applyAffixAttributes(stack);
        return new RollResult("scour");
    }

    /** Augment(Prefix): prefix枠が空いていればprefixを1つ追加 */
    public static RollResult augmentPrefix(ItemStack stack, int ilvl, RandomSource rng) {
        if (count(stack, AffixType.PREFIX) >= 3) return new RollResult("augment_prefix (full)");
        boolean ok = addOneRandom(stack, AffixType.PREFIX, ilvl, rng, NO_TAGS, NO_TAGS);
        return new RollResult(ok ? "augment_prefix" : "augment_prefix (no candidate)");
    }

    /** Augment(Suffix): suffix枠が空いていればsuffixを1つ追加 */
    public static RollResult augmentSuffix(ItemStack stack, int ilvl, RandomSource rng) {
        if (count(stack, AffixType.SUFFIX) >= 3) return new RollResult("augment_suffix (full)");
        boolean ok = addOneRandom(stack, AffixType.SUFFIX, ilvl, rng, NO_TAGS, NO_TAGS);
        return new RollResult(ok ? "augment_suffix" : "augment_suffix (no candidate)");
    }

    public static RollResult reforge(ItemStack stack, int ilvl, RandomSource rng, String tag) {
        // lock対応：ロックされてる側は維持、反対側だけ作り直す
        boolean lockP = AffixNbt.isPrefixLocked(stack);
        boolean lockS = AffixNbt.isSuffixLocked(stack);

        // 現在のprefix/suffixを退避（ロックされてる側だけ）
        var keepPrefixes = lockP ? AffixNbt.readByType(stack, AffixType.PREFIX) : List.<AffixNbt.AffixInstance>of();
        var keepSuffixes = lockS ? AffixNbt.readByType(stack, AffixType.SUFFIX) : List.<AffixNbt.AffixInstance>of();

        // 一旦全消し
        AffixNbt.resetAll(stack);

        // ロック側を戻す
        for (var inst : keepPrefixes) AffixNbt.add(stack, AffixType.PREFIX, inst.id(), inst.tier(), inst.roll());
        for (var inst : keepSuffixes) AffixNbt.add(stack, AffixType.SUFFIX, inst.id(), inst.tier(), inst.roll());

        // 付け直し数（ロックしてると枠が減る）
        int existing = count(stack, AffixType.PREFIX) + count(stack, AffixType.SUFFIX);
        int target = RARE_MIN_AFFIX + rng.nextInt(RARE_MAX_AFFIX - RARE_MIN_AFFIX + 1);

        int need = Math.max(0, target - existing);
        int added = 0;

        Set<String> require = Set.of(tag);
        for (int i = 0; i < need; i++) {
            AffixType type = pickTypeForAdd(stack, rng,3,3);
            if (type == null) break;

            // まず require(tag) で追加を試す。ダメなら通常追加へフォールバック
            boolean ok = addOneRandom(stack, type, ilvl, rng, require, NO_TAGS);
            if (!ok) {
                AffixType other = (type == AffixType.PREFIX) ? AffixType.SUFFIX : AffixType.PREFIX;
                ok = addOneRandom(stack, other, ilvl, rng, require, NO_TAGS);
            }
            if (!ok) {
                // フォールバック：tag無しで追加（「より多く出る」相当の落とし所）
                type = pickTypeForAdd(stack, rng,3,3);
                if (type == null) break;
                ok = addOneRandom(stack, type, ilvl, rng, NO_TAGS, NO_TAGS);
            }

            if (ok) added++;
            else break;
        }

        return new RollResult("reforge(" + tag + ")");
    }

    public static RollResult exaltCannotRoll(ItemStack stack, int ilvl, RandomSource rng, String forbiddenTag) {
        AffixType type = pickTypeForAdd(stack, rng,3,3);
        if (type == null) return new RollResult("exalt_cannot_roll(" + forbiddenTag + ") (no space)");

        Set<String> forbid = Set.of(forbiddenTag);
        boolean ok = addOneRandom(stack, type, ilvl, rng, NO_TAGS, forbid);
        if (!ok) {
            AffixType other = (type == AffixType.PREFIX) ? AffixType.SUFFIX : AffixType.PREFIX;
            ok = addOneRandom(stack, other, ilvl, rng, NO_TAGS, forbid);
        }
        return new RollResult(ok ? "exalt_cannot_roll(" + forbiddenTag + ")" : "exalt_cannot_roll(no candidate)");
    }

    // ======= Core Logic =======

    private static AffixType pickTypeForAdd(ItemStack stack, RandomSource rng, int maxPrefix, int maxSuffix) {
        boolean lockP = AffixNbt.isPrefixLocked(stack);
        boolean lockS = AffixNbt.isSuffixLocked(stack);

        int p = count(stack, AffixType.PREFIX);
        int s = count(stack, AffixType.SUFFIX);

        // ロックされてる側には追加しない（＝リロール対象外）
        boolean pOk = !lockP && p < maxPrefix;
        boolean sOk = !lockS && s < maxSuffix;

        if (!pOk && !sOk) return null;
        if (pOk && !sOk) return AffixType.PREFIX;
        if (!pOk) return AffixType.SUFFIX;
        return rng.nextBoolean() ? AffixType.PREFIX : AffixType.SUFFIX;
    }

    /** 既存グループ重複禁止 + アイテムタグ適合 + ilvl適合 の候補から1つ選んで追加 */
    private static boolean addOneRandom(ItemStack stack,
                                        AffixType type,
                                        int ilvl,
                                        RandomSource rng,
                                        Set<String> requireTags,
                                        Set<String> forbidTags) {
        Set<String> itemTags = inferItemTags(stack);//weapon/armor/otherとか
        Set<String> usedGroups = collectUsedGroups(stack);

        List<AffixDefinition> candidates = AffixRegistry.byType(type).stream()
                .filter(def -> fitsItem(def, itemTags))
                .filter(def -> !conflictsGroup(def, usedGroups))
                .filter(def -> hasAnyTierAvailable(def, ilvl))
                .filter(def -> hasAllTags(def, requireTags))
                .filter(def -> hasNoTags(def, forbidTags))
                .toList();

        if (candidates.isEmpty()) return false;

        AffixDefinition picked = weightedPick(candidates, def -> sumTierWeight(def, ilvl), rng);
        if (picked == null) return false;

        AffixTier tier = weightedPick(availableTiers(picked, ilvl), AffixTier::weight, rng);
        if (tier == null) return false;

        float roll = rng.nextFloat();
        AffixNbt.add(stack, type, picked.id(), tier.tier(), roll);
        return true;
    }

    private static KeptAffixes clearRespectingLocks(ItemStack stack) {
        boolean lockP = AffixNbt.isPrefixLocked(stack);
        boolean lockS = AffixNbt.isSuffixLocked(stack);

        var keepP = lockP ? AffixNbt.readByType(stack, AffixType.PREFIX) : List.<AffixNbt.AffixInstance>of();
        var keepS = lockS ? AffixNbt.readByType(stack, AffixType.SUFFIX) : List.<AffixNbt.AffixInstance>of();

        AffixNbt.resetAll(stack); // prefix/suffixリストを空にする（ロックフラグは残してOK）

        // ロック側を復元
        for (var inst : keepP) AffixNbt.add(stack, AffixType.PREFIX, inst.id(), inst.tier(), inst.roll());
        for (var inst : keepS) AffixNbt.add(stack, AffixType.SUFFIX, inst.id(), inst.tier(), inst.roll());

        return new KeptAffixes(lockP, lockS, keepP.size(), keepS.size());
    }

    private record KeptAffixes(boolean lockPrefix, boolean lockSuffix, int keptPrefix, int keptSuffix) {}

    // ======= Filtering / Weight helpers =======

    private static boolean hasAnyTierAvailable(AffixDefinition def, int ilvl) {
        return def.tiers().stream().anyMatch(t -> ilvl >= t.minIlvl());
    }

    private static List<AffixTier> availableTiers(AffixDefinition def, int ilvl) {
        return def.tiers().stream().filter(t -> ilvl >= t.minIlvl()).toList();
    }

    private static int sumTierWeight(AffixDefinition def, int ilvl) {
        return def.tiers().stream().filter(t -> ilvl >= t.minIlvl()).mapToInt(AffixTier::weight).sum();
    }

    /** itemTags に対して affixのtagsをチェック（weapon/armor/accessory/mapなどは必須一致扱い） */
    private static boolean fitsItem(AffixDefinition def, Set<String> itemTags) {
        // カテゴリタグは “要求” として扱う（def側に書いてあれば満たす必要あり）
        Set<String> category = Set.of("weapon", "armor", "accessory", "map");

        for (String t : def.tags()) {
            if (category.contains(t) && !itemTags.contains(t)) return false;
        }
        return true;
    }

    private static boolean conflictsGroup(AffixDefinition def, Set<String> usedGroups) {
        for (String g : def.groups()) {
            if (usedGroups.contains(g)) return true;
        }
        return false;
    }

    private static Set<String> collectUsedGroups(ItemStack stack) {
        // 付いてる affix id から定義を引いて groups を集める
        return AffixNbt.read(stack).stream()
                .map(inst -> AffixRegistry.get(inst.id()))
                .filter(Objects::nonNull)
                .flatMap(def -> def.groups().stream())
                .collect(Collectors.toSet());
    }

    /** バニラ装備向け：ざっくり itemTags を推定（いまは weapon/armor/other） */
    private static Set<String> inferItemTags(ItemStack stack) {
        Item item = stack.getItem();
        Set<String> tags = new HashSet<>();

        if (item instanceof ArmorItem) tags.add("armor");

        // TieredItem = 剣/斧/ツルハシ/シャベル/クワ などの道具系
        if (item instanceof TieredItem) tags.add("weapon");

        // まだCuriosはテストしないとのことなのでここは後回し
        // tags.add("accessory"); は Curios導入時に付与判定を足す

        // 何も付かなかったら “other” 扱い（def.tagsにカテゴリが無いものだけが候補になる）
        if (tags.isEmpty()) tags.add("other");
        return tags;
    }

    private interface WeightFunc<T> { int weight(T t); }

    private static <T> T weightedPick(List<T> list, WeightFunc<T> wf, RandomSource rng) {
        long total = 0;
        for (T t : list) {
            int w = wf.weight(t);
            if (w > 0) total += w;
        }
        if (total <= 0) return null;

        long r = Math.floorMod(rng.nextLong(), total); // 0~total-1
        long acc = 0;
        for (T t : list) {
            int w = wf.weight(t);
            if (w <= 0) continue;
            acc += w;
            if (r < acc) return t;
        }
        return null;
    }

    private static boolean hasAllTags(AffixDefinition def, Set<String> required) {
        if (required == null || required.isEmpty()) return true;
        // required は def.tags に全て含まれる必要
        for (String t : required) if (!def.tags().contains(t)) return false;
        return true;
    }

    private static boolean hasNoTags(AffixDefinition def, Set<String> forbidden) {
        if (forbidden == null || forbidden.isEmpty()) return true;
        for (String t : forbidden) if (def.tags().contains(t)) return false;
        return true;
    }

    // ======= Result =======
    public record RollResult(String message) {}
}
