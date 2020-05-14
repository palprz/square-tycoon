package io.github.palprz.types;

import java.util.StringJoiner;

public enum ProductEnum {

    WATER("Water"),
    HAY("Hay"),
    MILK("Milk"),
    BUTTER("Butter"),
    MEAT("Meat"),
    COAL("Coal"),
    IRON("Iron"),
    IRON_BAR("Iron bar"),
    SKIN("Skin"),
    WOOD("Wood"),
    FURNITURE("Furniture"),
    NONE("None");

    private String displayName;

    ProductEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getName() {
        return this.displayName.toLowerCase();
    }

    @Override
    public String toString() {
        return displayName;
    }
}
