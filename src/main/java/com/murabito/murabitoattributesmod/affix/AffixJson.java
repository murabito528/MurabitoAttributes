package com.murabito.murabitoattributesmod.affix;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.murabito.murabitoattributesmod.affix.records.AffixDefinition;
import com.murabito.murabitoattributesmod.affix.records.AffixStat;
import com.murabito.murabitoattributesmod.affix.records.AffixTier;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class AffixJson {
    private AffixJson() {}

    public static AffixDefinition parse(ResourceLocation fileKey, JsonObject obj, String requiredNamespace) {
        if (!obj.has("id")) {
            throw new IllegalArgumentException("Affix json missing 'id': " + fileKey);
        }
        ResourceLocation id = ResourceLocation.parse(obj.get("id").getAsString());
        if (!id.getNamespace().equals(requiredNamespace)) {
            throw new IllegalArgumentException("Affix id must be in namespace '" + requiredNamespace +
                    "': " + id + " (file=" + fileKey + ")");
        }

        if (!obj.has("type")) {
            throw new IllegalArgumentException("Affix json missing 'type': " + fileKey);
        }
        AffixType type = AffixType.fromString(obj.get("type").getAsString());

        String name = obj.has("name") ? obj.get("name").getAsString() : id.toString();

        List<String> groups = readStringList(obj, "groups");
        List<String> tags = readStringList(obj, "tags");
        List<String> targets = readStringList(obj, "targets");
        List<AffixTier> tiers = readTiers(fileKey, obj, "tiers");

        // tiers が空は危険なので弾く
        if (tiers.isEmpty()) {
            throw new IllegalArgumentException("Affix json has empty 'tiers': " + fileKey);
        }

        return new AffixDefinition(id, type, name, groups, tags, tiers, targets, fileKey);
    }

    private static List<String> readStringList(JsonObject obj, String key) {
        List<String> out = new ArrayList<>();
        if (!obj.has(key)) return out;
        JsonArray arr = obj.getAsJsonArray(key);
        for (JsonElement e : arr) out.add(e.getAsString());
        return out;
    }

    private static List<AffixTier> readTiers(ResourceLocation fileKey, JsonObject obj, String key) {
        List<AffixTier> out = new ArrayList<>();
        if (!obj.has(key)) return out;

        JsonElement el = obj.get(key);
        if (!el.isJsonArray()) {
            throw new IllegalArgumentException("Expected array for '" + key + "': " + fileKey);
        }

        JsonArray arr = el.getAsJsonArray();
        for (JsonElement e : arr) {
            if (!e.isJsonObject()) {
                throw new IllegalArgumentException("Tier entry must be object: " + fileKey);
            }
            JsonObject t = e.getAsJsonObject();

            int tier = t.has("tier") ? t.get("tier").getAsInt() : 1;
            int minIlvl = t.has("min_ilvl") ? t.get("min_ilvl").getAsInt() : 1;
            int weight = t.has("weight") ? t.get("weight").getAsInt() : 1000;

            List<AffixStat> stats = readStats(fileKey, t, "stats");

            // 新形式では stats は必須にしておくのがおすすめ（事故るより早期に落とす）
            if (stats.isEmpty()) {
                throw new IllegalArgumentException("Tier has empty/missing 'stats': tier=" + tier + " file=" + fileKey);
            }

            out.add(new AffixTier(tier, minIlvl, weight, stats));
        }
        return out;
    }

    private static List<AffixStat> readStats(ResourceLocation fileKey, JsonObject tierObj, String key) {
        List<AffixStat> out = new ArrayList<>();
        if (!tierObj.has(key)) return out;

        JsonElement el = tierObj.get(key);
        if (!el.isJsonArray()) {
            throw new IllegalArgumentException("Expected array for '" + key + "': " + fileKey);
        }

        for (JsonElement e : el.getAsJsonArray()) {
            if (!e.isJsonObject()) {
                throw new IllegalArgumentException("Stat entry must be object: " + fileKey);
            }
            JsonObject s = e.getAsJsonObject();

            if (!s.has("attribute")) {
                throw new IllegalArgumentException("Stat missing 'attribute': " + fileKey);
            }
            ResourceLocation attr = ResourceLocation.parse(s.get("attribute").getAsString());

            // op は enum 化してもOK。とりあえず String のままでも動く
            int op = s.has("op") ? s.get("op").getAsInt() : 0;

            // 小数が来るので getAsDouble が必須
            if (!s.has("min") || !s.has("max")) {
                throw new IllegalArgumentException("Stat missing 'min'/'max': " + fileKey);
            }
            double min = s.get("min").getAsDouble();
            double max = s.get("max").getAsDouble();

            if (max < min) {
                throw new IllegalArgumentException("Stat has max < min (" + min + "..." + max + "): " + fileKey);
            }

            out.add(new AffixStat(attr, op, min, max));
        }

        return out;
    }
}

