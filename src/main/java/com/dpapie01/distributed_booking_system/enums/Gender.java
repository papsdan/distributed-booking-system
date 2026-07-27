package com.dpapie01.distributed_booking_system.enums;

public enum Gender {
    MALE,
    FEMALE,
    NON_BINARY,
    PREFER_NOT_TO_SAY;

    public boolean isEligibleFor(GameGenderOption gameGenderOption) {
        return switch (this) {
            case MALE -> gameGenderOption != GameGenderOption.WOMEN;
            case FEMALE -> gameGenderOption != GameGenderOption.MEN;
            case NON_BINARY -> true;
            case PREFER_NOT_TO_SAY -> gameGenderOption == GameGenderOption.MIXED;
        };
    }
}