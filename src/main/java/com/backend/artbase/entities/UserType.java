package com.backend.artbase.entities;

public enum UserType {
    ADMIN('A'),
    CUSTOMER('C'),
    EMPLOYEE('E'),
    NEW_USER('N'),
    GUEST('G'),
    EXTERNAL('X');

    private final char code;

    UserType(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public static UserType fromCode(char code) {
        for (UserType userType : values()) {
            if (userType.code == code) {
                return userType;
            }
        }
        throw new IllegalArgumentException("Invalid user type code: " + code);
    }
}
