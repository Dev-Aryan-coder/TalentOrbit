package com.example.TalentOrbit.util;

import java.util.regex.Pattern;

public class FormatValidators {
    // AISHE format: e.g. C-12345, U-0123, S-99999
    private static final Pattern AISHE_PATTERN = Pattern.compile("^[A-Z]-[0-9]{4,7}$", Pattern.CASE_INSENSITIVE);
    
    // MCA 21-character CIN format: e.g. U72900MH2018PTC312450
    private static final Pattern CIN_PATTERN = Pattern.compile("^[UL][0-9]{5}[A-Z]{2}[0-9]{4}[A-Z]{3}[0-9]{6}$");

    public static boolean isValidAisheCode(String code) {
        if (code == null || code.trim().isEmpty()) return false;
        return AISHE_PATTERN.matcher(code.trim().toUpperCase()).matches();
    }

    public static boolean isValidCin(String cin) {
        if (cin == null || cin.trim().isEmpty()) return false;
        return CIN_PATTERN.matcher(cin.trim().toUpperCase()).matches();
    }
}
