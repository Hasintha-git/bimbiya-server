package com.bimbiya.server.util.enums;


public enum ClientPotionEnum
{
    SMALL("small", "Small"),
    MEDIUM("medium", "Medium"),
    LARGE("large", "Large"),
    XLARGE("xlarge", "X-Large"),
    CUSTOM("custom", "Custom");


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
            case "xlarge":
                return XLARGE;
            default:
                return CUSTOM;
        }
    }
}
