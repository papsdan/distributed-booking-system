package com.dpapie01.distributed_booking_system.enums;

/**
 * Enums represents Gender of a User recorded in their Profile.
 * Used to check eligibility against a game's GenderOption through Gender.isEligibleFor(GameGenderOption) method..
 */
public enum Gender {
    MALE,
    FEMALE,
    NON_BINARY,
    PREFER_NOT_TO_SAY;

    /**
     * Checks whether a player with this gender is allowed to join a game with the given
     * gender option:
     * NON_BINARY is eligible for any option
     * PREFER_NOT_TO_SAY is only eligible for MIXED games
     * MALE is only eligible for MEN games
     * FEMALE is only eligible for WOMEN games
     *
     * @param gameGenderOption the gender eligibility rule of the game being joined
     * @return true if a player with this gender is eligible, else false.
     */
    public boolean isEligibleFor(GameGenderOption gameGenderOption) {
        return switch (this) {
            case MALE -> gameGenderOption != GameGenderOption.WOMEN;
            case FEMALE -> gameGenderOption != GameGenderOption.MEN;
            case NON_BINARY -> true;
            case PREFER_NOT_TO_SAY -> gameGenderOption == GameGenderOption.MIXED;
        };
    }
}