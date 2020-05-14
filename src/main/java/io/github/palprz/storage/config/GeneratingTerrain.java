package io.github.palprz.storage.config;

import io.github.palprz.types.TerrainEnum;

import java.util.StringJoiner;

public class GeneratingTerrain {

    private TerrainEnum terrain;
    private int percent;

    public GeneratingTerrain() {
        // make Jackson happy
    }

    public GeneratingTerrain(TerrainEnum terrain, int percent) {
        this.terrain = terrain;
        this.percent = percent;
    }

    public TerrainEnum getTerrain() {
        return terrain;
    }

    public int getPercent() {
        return percent;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GeneratingTerrain.class.getSimpleName() + "[", "]")
                .add("terrain='" + terrain + "'")
                .add("percent=" + percent)
                .toString();
    }
}
