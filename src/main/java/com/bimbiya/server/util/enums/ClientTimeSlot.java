package com.bimbiya.server.util.enums;

public enum ClientTimeSlot {
    MIDNIGHT("00:00", "12.00 AM"),
    MIDNIGHT_HALF("00:30", "12.30 AM"),
    ONE_AM("01:00", "01.00 AM"),
    ONE_AM_HALF("01:30", "01.30 AM"),
    TWO_AM("02:00", "02.00 AM"),
    TWO_AM_HALF("02:30", "02.30 AM"),
    THREE_AM("03:00", "03.00 AM"),
    THREE_AM_HALF("03:30", "03.30 AM"),
    FOUR_AM("04:00", "04.00 AM"),
    FOUR_AM_HALF("04:30", "04.30 AM"),
    FIVE_AM("05:00", "05.00 AM"),
    FIVE_AM_HALF("05:30", "05.30 AM"),
    SIX_AM("06:00", "06.00 AM"),
    SIX_AM_HALF("06:30", "06.30 AM"),
    SEVEN_AM("07:00", "07.00 AM"),
    SEVEN_AM_HALF("07:30", "07.30 AM"),
    EIGHT_AM("08:00", "08.00 AM"),
    EIGHT_AM_HALF("08:30", "08.30 AM"),
    NINE_AM("09:00", "09.00 AM"),
    NINE_AM_HALF("09:30", "09.30 AM"),
    TEN_AM("10:00", "10.00 AM"),
    TEN_AM_HALF("10:30", "10.30 AM"),
    ELEVEN_AM("11:00", "11.00 AM"),
    ELEVEN_AM_HALF("11:30", "11.30 AM"),
    NOON("12:00", "12.00 PM"),
    NOON_HALF("12:30", "12.30 PM"),
    ONE_PM("13:00", "01.00 PM"),
    ONE_PM_HALF("13:30", "01.30 PM"),
    TWO_PM("14:00", "02.00 PM"),
    TWO_PM_HALF("14:30", "02.30 PM"),
    THREE_PM("15:00", "03.00 PM"),
    THREE_PM_HALF("15:30", "03.30 PM"),
    FOUR_PM("16:00", "04.00 PM"),
    FOUR_PM_HALF("16:30", "04.30 PM"),
    FIVE_PM("17:00", "05.00 PM"),
    FIVE_PM_HALF("17:30", "05.30 PM"),
    SIX_PM("18:00", "06.00 PM"),
    SIX_PM_HALF("18:30", "06.30 PM"),
    SEVEN_PM("19:00", "07.00 PM"),
    SEVEN_PM_HALF("19:30", "07.30 PM"),
    EIGHT_PM("20:00", "08.00 PM"),
    EIGHT_PM_HALF("20:30", "08.30 PM"),
    NINE_PM("21:00", "09.00 PM"),
    NINE_PM_HALF("21:30", "09.30 PM"),
    TEN_PM("22:00", "10.00 PM"),
    TEN_PM_HALF("22:30", "10.30 PM"),
    ELEVEN_PM("23:00", "11.00 PM"),
    ELEVEN_PM_HALF("23:30", "11.30 PM");

    private final String timeValue;
    private final String stringValue;

    ClientTimeSlot(String timeValue, String stringValue) {
        this.timeValue = timeValue;
        this.stringValue = stringValue;
    }

    public static ClientTimeSlot getEnum(String code) {
        switch (code) {
            case "00:00":
                return MIDNIGHT;
            case "00:30":
                return MIDNIGHT_HALF;
            case "01:00":
                return ONE_AM;
            case "01:30":
                return ONE_AM_HALF;
            case "02:00":
                return TWO_AM;
            case "02:30":
                return TWO_AM_HALF;
            case "03:00":
                return THREE_AM;
            case "03:30":
                return THREE_AM_HALF;
            case "04:00":
                return FOUR_AM;
            case "04:30":
                return FOUR_AM_HALF;
            case "05:00":
                return FIVE_AM;
            case "05:30":
                return FIVE_AM_HALF;
            case "06:00":
                return SIX_AM;
            case "06:30":
                return SIX_AM_HALF;
            case "07:00":
                return SEVEN_AM;
            case "07:30":
                return SEVEN_AM_HALF;
            case "08:00":
                return EIGHT_AM;
            case "08:30":
                return EIGHT_AM_HALF;
            case "09:00":
                return NINE_AM;
            case "09:30":
                return NINE_AM_HALF;
            case "10:00":
                return TEN_AM;
            case "10:30":
                return TEN_AM_HALF;
            case "11:00":
                return ELEVEN_AM;
            case "11:30":
                return ELEVEN_AM_HALF;
            case "12:00":
                return NOON;
            case "12:30":
                return NOON_HALF;
            case "13:00":
                return ONE_PM;
            case "13:30":
                return ONE_PM_HALF;
            case "14:00":
                return TWO_PM;
            case "14:30":
                return TWO_PM_HALF;
            case "15:00":
                return THREE_PM;
            case "15:30":
                return THREE_PM_HALF;
            case "16:00":
                return FOUR_PM;
            case "16:30":
                return FOUR_PM_HALF;
            case "17:00":
                return FIVE_PM;
            case "17:30":
                return FIVE_PM_HALF;
            case "18:00":
                return SIX_PM;
            case "18:30":
                return SIX_PM_HALF;
            case "19:00":
                return SEVEN_PM;
            case "19:30":
                return SEVEN_PM_HALF;
            case "20:00":
                return EIGHT_PM;
            case "20:30":
                return EIGHT_PM_HALF;
            case "21:00":
                return NINE_PM;
            case "21:30":
                return NINE_PM_HALF;
            case "22:00":
                return TEN_PM;
            case "22:30":
                return TEN_PM_HALF;
            case "23:00":
                return ELEVEN_PM;
            case "23:30":
                return ELEVEN_PM_HALF;
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
