package com.bimbiya.server.util.enums;


public enum ClientDistrictEnum {
    KALUTHARA("kaluthara", "Kaluthara"),
    COLOMBO("colombo", "Colombo"),
    GAMPAHA("gampaha", "Gampaha");


    private String code;
    private String description;

    ClientDistrictEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ClientDistrictEnum getEnum(String code) {
        switch (code) {
            case "kaluthara":
                return KALUTHARA;
            case "colombo":
                return COLOMBO;
            case "gampaha":
                return GAMPAHA;
            default:
                return null;
        }
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
