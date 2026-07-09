package com.dpapie01.distributed_booking_system.enums;

public enum Gender {
    MAN,
    WOMAN,
    NON_BINARY,
    PREFER_NOT_TO_SAY;

    public boolean isEligibleFor(GameGenderOption gameGenderOption) {
        return switch (this) {
            case MAN -> gameGenderOption != GameGenderOption.WOMENS;
            case WOMAN -> gameGenderOption != GameGenderOption.MENS;
            case NON_BINARY -> true;
            case PREFER_NOT_TO_SAY -> gameGenderOption == GameGenderOption.MIXED;
        };
    }
}