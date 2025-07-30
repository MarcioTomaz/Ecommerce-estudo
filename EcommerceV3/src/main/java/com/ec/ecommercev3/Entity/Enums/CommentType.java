package com.ec.ecommercev3.Entity.Enums;

import lombok.Getter;

@Getter
public enum CommentType {

    REJECTION_REASON("REJECTION REASON"),
    systemComment("systemComment");

    private final String description;

    CommentType(String description) {
        this.description = description;
    }

}
