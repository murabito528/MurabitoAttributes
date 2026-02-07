package com.murabito.murabitoattributesmod.affix;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class AffixNbt {
    private AffixNbt() {}

    private static final String ROOT = "murabito_affixes";
    private static final String PREFIXES = "prefixes";
    private static final String SUFFIXES = "suffixes";
    private static final String RARITY = "rarity";
    private static final String ILVL = "ilvl";

    public record AffixInstance(ResourceLocation id, int tier, float roll, String type) {}

    public static CompoundTag getOrCreateRoot(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(ROOT, Tag.TAG_COMPOUND)) tag.put(ROOT, new CompoundTag());
        return tag.getCompound(ROOT);
    }

    private static ListTag getList(ItemStack stack, String key) {
        CompoundTag root = getOrCreateRoot(stack);
        if (!root.contains(key, Tag.TAG_LIST)) root.put(key, new ListTag());
        return root.getList(key, Tag.TAG_COMPOUND);
    }

    public static List<AffixInstance> read(ItemStack stack) {
        List<AffixInstance> out = new ArrayList<>();
        CompoundTag root = getOrCreateRoot(stack);

        readList(root, PREFIXES, out);
        readList(root, SUFFIXES, out);

        return out;
    }

    private static void readList(CompoundTag root, String key, List<AffixInstance> out) {
        if (!root.contains(key, Tag.TAG_LIST)) return;
        ListTag list = root.getList(key, Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag c = list.getCompound(i);
            if (!c.contains("id", Tag.TAG_STRING)) continue;
            ResourceLocation id = ResourceLocation.parse(c.getString("id"));
            int tier = c.contains("tier", Tag.TAG_INT) ? c.getInt("tier") : 1;
            float roll = c.contains("roll", Tag.TAG_FLOAT) ? c.getFloat("roll") : 0.5f;
            String type = c.contains("type", Tag.TAG_STRING) ? c.getString("type") : "null it's bug";
            out.add(new AffixInstance(id, tier, roll, type));
        }
    }

    public static int getIlvl(ItemStack stack) {
        CompoundTag root = getOrCreateRoot(stack);
        return root.contains(ILVL, Tag.TAG_INT) ? root.getInt(ILVL) : 0;
    }

    public static void setIlvl(ItemStack stack, int ilvl) {
        getOrCreateRoot(stack).putInt(ILVL, Math.max(1, ilvl));
    }

    public static void clearIlvl(ItemStack stack) {
        getOrCreateRoot(stack).remove(ILVL);
    }

    public static void add(ItemStack stack, AffixType type, ResourceLocation affixId, int tier, float roll01) {
        String listKey = (type == AffixType.PREFIX) ? PREFIXES : SUFFIXES;
        ListTag list = getList(stack, listKey);

        CompoundTag c = new CompoundTag();
        c.putString("id", affixId.toString());
        c.putInt("tier", tier);
        c.putFloat("roll", clamp01(roll01));
        c.putString("type", type.toString());
        list.add(c);
    }

    public static void resetAll(ItemStack stack) {
        CompoundTag root = getOrCreateRoot(stack);
        setPrefixLocked(stack,false);
        setSuffixLocked(stack,false);
        root.put(PREFIXES, new ListTag());
        root.put(SUFFIXES, new ListTag());
    }

    private static float clamp01(float f) {
        if (f < 0f) return 0f;
        if (f > 1f) return 1f;
        return f;
    }

    public static List<AffixInstance> readByType(ItemStack stack, AffixType type) {
        List<AffixInstance> out = new ArrayList<>();
        CompoundTag root = getOrCreateRoot(stack);
        readList(root, (type == AffixType.PREFIX) ? PREFIXES : SUFFIXES, out);
        return out;
    }

    public static int count(ItemStack stack, AffixType type) {
        CompoundTag root = getOrCreateRoot(stack);
        String key = (type == AffixType.PREFIX) ? PREFIXES : SUFFIXES;
        if (!root.contains(key, Tag.TAG_LIST)) return 0;
        return root.getList(key, Tag.TAG_COMPOUND).size();
    }

    public static boolean removeRandom(ItemStack stack, RandomSource rng) {
        // prefix/suffix両方からランダムに1個消す
        int p = count(stack, AffixType.PREFIX);
        int s = count(stack, AffixType.SUFFIX);
        int total = p + s;
        if (total <= 0) return false;

        int pick = rng.nextInt(total);
        if (pick < p) return removeAt(stack, AffixType.PREFIX, pick);
        return removeAt(stack, AffixType.SUFFIX, pick - p);
    }

    public static boolean removeAt(ItemStack stack, AffixType type, int index) {
        CompoundTag root = getOrCreateRoot(stack);
        String key = (type == AffixType.PREFIX) ? PREFIXES : SUFFIXES;
        if (!root.contains(key, Tag.TAG_LIST)) return false;

        ListTag list = root.getList(key, Tag.TAG_COMPOUND);
        if (index < 0 || index >= list.size()) return false;

        list.remove(index);
        return true;
    }

    public static boolean removeRandomByType(ItemStack stack, AffixType type, RandomSource rng) {
        int count = count(stack, type);
        if (count <= 0) return false;
        int idx = rng.nextInt(count);
        return removeAt(stack, type, idx);
    }


    private static final String LOCK_PREFIX = "lock_prefix";
    private static final String LOCK_SUFFIX = "lock_suffix";

    public static boolean isPrefixLocked(ItemStack stack) {
        return getOrCreateRoot(stack).getBoolean(LOCK_PREFIX);
    }
    public static boolean isSuffixLocked(ItemStack stack) {
        return getOrCreateRoot(stack).getBoolean(LOCK_SUFFIX);
    }
    public static void setPrefixLocked(ItemStack stack, boolean v) {
        getOrCreateRoot(stack).putBoolean(LOCK_PREFIX, v);
    }
    public static void setSuffixLocked(ItemStack stack, boolean v) {
        getOrCreateRoot(stack).putBoolean(LOCK_SUFFIX, v);
    }

    public static void setRarity(ItemStack stack, Rarity rarity) {
        getOrCreateRoot(stack).putString(RARITY, rarity.id);
        applyNameByRarity(stack);
    }
    public static Rarity getRarity(ItemStack stack) {
        CompoundTag root = getOrCreateRoot(stack);
        if (!root.contains(RARITY, Tag.TAG_STRING)) return Rarity.NORMAL; // 未設定の扱い
        return Rarity.fromId(root.getString(RARITY));
    }
    public static void clearRarity(ItemStack stack) {
        getOrCreateRoot(stack).remove(RARITY);
        applyNameByRarity(stack);
        //バグ:自前で名前を付けたアイテムをクリアすると元の名前が消える
    }

    public static void applyNameByRarity(ItemStack stack) {
        var rarity = AffixNbt.getRarity(stack);
        String color = switch (rarity) {
            case MAGIC -> "aqua";
            case RARE -> "yellow";
            case UNIQUE -> "gold";
            case NORMAL -> null;
        };

        // 表示名は元の翻訳名を使うのが自然
        String baseName = stack.getHoverName().getString();
        setColoredName(stack, baseName, color);
    }

    public static void setColoredName(ItemStack stack, String text, String colorName) {
        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag display = tag.contains("display", Tag.TAG_COMPOUND) ? tag.getCompound("display") : new CompoundTag();

        if(colorName==null){
            display.remove("Name");
            tag.put("display", display);
            return;
        }

        // JSONテキスト（/giveと同じ）
        String json = String.format("{\"text\":\"%s\",\"color\":\"%s\",\"italic\":false}", escapeJson(text), colorName);
        display.putString("Name", json);

        tag.put("display", display);
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
