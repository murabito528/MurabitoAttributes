package com.murabito.murabitoattributesmod.affix;

public enum AffixType {
    PREFIX, SUFFIX, IMPLICIT, UNIQUE;

    public static AffixType fromString(String s) {
        return switch (s.toLowerCase()) {
            case "prefix" -> PREFIX;
            case "suffix" -> SUFFIX;
            case "implicit" -> IMPLICIT;
            case "unique" -> UNIQUE;
            default -> throw new IllegalArgumentException("Unknown affix type: " + s);
        };
    }
}
