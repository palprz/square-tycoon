package io.github.palprz.storage.config;

import java.util.List;
import java.util.StringJoiner;

public class Config {

    private List<GeneratingTerrain> generatingTerrains;
    private Debug debug;
    private int boardRowSize;
    private int boardColumnSize;
    private int increaseFieldPrice;
    private int initialAvailableMoney;

    public Config(List<GeneratingTerrain> generatingTerrains, Debug debug, int boardRowSize, int boardColumnSize,
                  int increaseFieldPrice, int initialAvailableMoney) {
        this.generatingTerrains = generatingTerrains;
        this.debug = debug;
        this.boardRowSize = boardRowSize;
        this.boardColumnSize = boardColumnSize;
        this.increaseFieldPrice = increaseFieldPrice;
        this.initialAvailableMoney = initialAvailableMoney;
    }

    public Config() {
        // make Jackson happy
    }

    public List<GeneratingTerrain> getGeneratingTerrains() {
        return generatingTerrains;
    }

    public Debug getDebug() {
        return debug;
    }

    public int getBoardRowSize() {
        return boardRowSize;
    }

    public int getBoardColumnSize() {
        return boardColumnSize;
    }

    public int getIncreaseFieldPrice() {
        return increaseFieldPrice;
    }

    public int getInitialAvailableMoney() {
        return initialAvailableMoney;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Config.class.getSimpleName() + "[", "]")
                .add("generatingTerrains=" + generatingTerrains)
                .add("debug=" + debug)
                .add("boardRowSize=" + boardRowSize)
                .add("boardColumnSize=" + boardColumnSize)
                .add("increaseFieldPrice=" + increaseFieldPrice)
                .add("initialAvailableMoney=" + initialAvailableMoney)
                .toString();
    }
}
