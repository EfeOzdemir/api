package com.plantapp.api.core.enums;

public enum Gender {
    MALE, FEMALE, NA;

    public static Gender getGender(String gender) {
        return switch (gender.toUpperCase()) {
            case "MALE" -> MALE;
            case "FEMALE" -> FEMALE;
            case "NA" -> NA;
            default -> throw new IllegalArgumentException("Gender constant for given string not available.");
        };
    }
}
