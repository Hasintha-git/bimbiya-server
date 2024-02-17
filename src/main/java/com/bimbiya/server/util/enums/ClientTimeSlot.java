package com.bimbiya.server.util.enums;

public enum ClientTimeSlot {
    MIDNIGHT("00:00:00", "12:AM"),
    ONE_AM("01:00:00", "01:AM"),
    TWO_AM("02:00:00", "02:AM"),
    THREE_AM("03:00:00", "03:AM"),
    FOUR_AM("04:00:00", "04:AM"),
    FIVE_AM("05:00:00", "05:AM"),
    SIX_AM("06:00:00", "06:AM"),
    SEVEN_AM("07:00:00", "07:AM"),
    EIGHT_AM("08:00:00", "08:AM"),
    NINE_AM("09:00:00", "09:AM"),
    TEN_AM("10:00:00", "10:AM"),
    ELEVEN_AM("11:00:00", "11:AM"),
    NOON("12:00:00", "12:PM"),
    ONE_PM("13:00:00", "01:PM"),
    TWO_PM("14:00:00", "02:PM"),
    THREE_PM("15:00:00", "03:PM"),
    FOUR_PM("16:00:00", "04:PM"),
    FIVE_PM("17:00:00", "05:PM"),
    SIX_PM("18:00:00", "06:PM"),
    SEVEN_PM("19:00:00", "07:PM"),
    EIGHT_PM("20:00:00", "08:PM"),
    NINE_PM("21:00:00", "09:PM"),
    TEN_PM("22:00:00", "10:PM"),
    ELEVEN_PM("23:00:00", "11:PM");

    private final String timeValue;
    private final String stringValue;

    ClientTimeSlot(String timeValue, String stringValue) {
        this.timeValue = timeValue;
        this.stringValue = stringValue;
    }

    public static ClientTimeSlot getEnum(String code) {
        switch (code) {
            case "00:00:00":
                return MIDNIGHT;
            case "01:00:00":
                return ONE_AM;
            case "02:00:00":
                return TWO_AM;
            case "03:00:00":
                return THREE_AM;
            case "04:00:00":
                return FOUR_AM;
            case "05:00:00":
                return FIVE_AM;
            case "06:00:00":
                return SIX_AM;
            case "07:00:00":
                return SEVEN_AM;
            case "08:00:00":
                return EIGHT_AM;
            case "09:00:00":
                return NINE_AM;
            case "10:00:00":
                return TEN_AM;
            case "11:00:00":
                return ELEVEN_AM;
            case "12:00:00":
                return NOON;
            case "13:00:00":
                return ONE_PM;
            case "14:00:00":
                return TWO_PM;
            case "15:00:00":
                return THREE_PM;
            case "16:00:00":
                return FOUR_PM;
            case "17:00:00":
                return FIVE_PM;
            case "18:00:00":
                return SIX_PM;
            case "19:00:00":
                return SEVEN_PM;
            case "20:00:00":
                return EIGHT_PM;
            case "21:00:00":
                return NINE_PM;
            case "22:00:00":
                return TEN_PM;
            case "23:00:00":
                return ELEVEN_PM;
            default:
                throw new IllegalArgumentException("Invalid code: " + code);
        }
    }

    public String getTimeValue() {
        return timeValue;
    }

    public String getStringValue() {
        return stringValue;
    }

}
