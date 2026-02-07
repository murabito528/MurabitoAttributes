package com.murabito.murabitoattributesmod.affix;

public enum Rarity {
    MAGIC("magic"),
    RARE("rare"),
    NORMAL("normal"),
    UNIQUE("unique");

    public final String id;
    Rarity(String id) { this.id = id; }

    public static Rarity fromId(String s) {
        if (s == null) return NORMAL;
        return switch (s) {
            case "magic" -> MAGIC;
            case "rare" -> RARE;
            case "unique" -> UNIQUE;
            default -> NORMAL;
        };
    }
}
