package com.murabito.murabitoattributesmod.affix;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
        List<AffixTier> tiers = readTiers(obj, "tiers");

        // tiers が空は危険なので弾く
        if (tiers.isEmpty()) {
            throw new IllegalArgumentException("Affix json has empty 'tiers': " + fileKey);
        }

        return new AffixDefinition(id, type, name, groups, tags, tiers, fileKey);
    }

    private static List<String> readStringList(JsonObject obj, String key) {
        List<String> out = new ArrayList<>();
        if (!obj.has(key)) return out;
        JsonArray arr = obj.getAsJsonArray(key);
        for (JsonElement e : arr) out.add(e.getAsString());
        return out;
    }

    private static List<AffixTier> readTiers(JsonObject obj, String key) {
        List<AffixTier> out = new ArrayList<>();
        if (!obj.has(key)) return out;

        JsonArray arr = obj.getAsJsonArray(key);
        for (JsonElement e : arr) {
            JsonObject t = e.getAsJsonObject();
            int tier = t.has("tier") ? t.get("tier").getAsInt() : 1;
            int minIlvl = t.has("min_ilvl") ? t.get("min_ilvl").getAsInt() : 1;
            int weight = t.has("weight") ? t.get("weight").getAsInt() : 1000;
            out.add(new AffixTier(tier, minIlvl, weight));
        }
        return out;
    }
}
