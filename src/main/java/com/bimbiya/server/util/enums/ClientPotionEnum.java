package com.bimbiya.server.util.enums;


public enum ClientPotionEnum {
    SMALL("small", "1 ingredient"),
    MEDIUM("medium", "5 ingredient"),
    LARGE("large", "7 ingredient");


    private String code;
    private String description;

    ClientPotionEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ClientPotionEnum getEnum(String code) {
        switch (code) {
            case "small":
                return SMALL;
            case "medium":
                return MEDIUM;
            case "large":
                return LARGE;
            default:
                return MEDIUM;
        }
    }
}
