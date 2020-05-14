package io.github.palprz.types;

public enum TerrainEnum {

    WATER("Water"),
    PLAIN("Plain"),
    FOREST("Forest"),
    MOUNTAIN("Mountain"),
    UNKNOWN("Unknown");

    private String displayName;

    TerrainEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getName() {
        return this.displayName.toLowerCase();
    }
}
