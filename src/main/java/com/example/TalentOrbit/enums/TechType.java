package com.example.TalentOrbit.enums;

public enum TechType {
    LANGUAGE,
    FRAMEWORK,
    LIBRARY,
    TOOL;

    public static TechType fromString(String val) {
        if (val == null) return LANGUAGE;
        try {
            return TechType.valueOf(val.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return LANGUAGE;
        }
    }
}
