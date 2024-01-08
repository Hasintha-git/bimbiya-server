package com.bimbiya.server.util.enums;

public enum ClientOrderStatus {
    PENDING("pending", "Pending"),
    COMPLETED("completed", "Completed"),
    PROCESSING("processing", "processing"),
    SHIPPED("shipped", "Shipped"),
    CANCELED("canceled", "Canceled");


    private String code;
    private String description;

    ClientOrderStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ClientOrderStatus getEnum(String code) {
        switch (code) {
            case "pending":
                return PENDING;
            case "completed":
                return COMPLETED;
            case "processing":
                return PROCESSING;
            case "shipped":
                return SHIPPED;
            default:
                return CANCELED;
        }
    }
}
