package com.fams.syllabus_repository.enums;

import lombok.RequiredArgsConstructor;


public enum PublicStatus {
    ACTIVE("Active"),
    DRAFT("Drafting"),
    INACTIVE("In-active"),
    PLANNING("Planning");
    private final String value;

    PublicStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public static PublicStatus fromValue(String value) {
        for (PublicStatus status : PublicStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid PublicStatus value: " + value);
    }
}
