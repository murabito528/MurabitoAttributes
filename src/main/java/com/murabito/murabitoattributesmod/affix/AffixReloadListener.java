package com.murabito.murabitoattributesmod.affix;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class AffixReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().create();
    private final String modid;

    public AffixReloadListener(String modid) {
        super(GSON, "affix"); // data/<namespace>/affix/**/*.json
        this.modid = modid;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        AffixRegistry.clear();

        for (var entry : map.entrySet()) {
            ResourceLocation fileKey = entry.getKey(); // murabitoattributes:affix/prefix/addphys_t1 など
            JsonObject obj = entry.getValue().getAsJsonObject();

            AffixDefinition def = AffixJson.parse(fileKey, obj, modid);

            // 重複IDは即エラー（バグを早期発見）
            if (AffixRegistry.contains(def.id())) {
                throw new IllegalStateException("Duplicate affix id: " + def.id() + " (file=" + fileKey + ")");
            }

            //（任意）prefixフォルダにsuffixが入ってる事故チェック
            String p = fileKey.getPath();
            if (p.startsWith("affix/prefix/") && def.type() != AffixType.PREFIX)
                throw new IllegalArgumentException("File is in affix/prefix but type is " + def.type() + ": " + fileKey);
            if (p.startsWith("affix/suffix/") && def.type() != AffixType.SUFFIX)
                throw new IllegalArgumentException("File is in affix/suffix but type is " + def.type() + ": " + fileKey);

            AffixRegistry.register(def);
        }
    }
}
