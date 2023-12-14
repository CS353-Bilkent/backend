package com.backend.artbase.entities;

public enum ArtworkStatus {
    WAITING_APPROVAL("W"),
    AUCTION("A"),
    ON("O"),
    SOLD("S"),
    REMOVED("R");

    private final String code;

    private ArtworkStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ArtworkStatus fromCode(String code) {
        for (ArtworkStatus artworkStatus : values()) {
            if (artworkStatus.code.equals(code)) {
                return artworkStatus;
            }
        }
        throw new IllegalArgumentException("Invalid artwork status code: " + code);
    }
}
