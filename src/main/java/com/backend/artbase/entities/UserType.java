package com.backend.artbase.entities;

public enum UserType {
    ARTIST("A"),
    COLLECTOR("C"),
    ENTHUSIAST("E"),
    NORMAL("N"),
    GALLERY("G"),
    ADMIN("X");

    private final String code;

    private UserType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static UserType fromCode(String code) {
        for (UserType userType : values()) {
            if (userType.code.equals(code)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("Invalid user type code: " + code);
    }
}
