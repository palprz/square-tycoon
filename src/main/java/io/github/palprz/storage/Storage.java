package io.github.palprz.storage;

import io.github.palprz.storage.config.Config;
import io.github.palprz.types.Field;
import io.github.palprz.types.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Storage {

    private static final Logger LOG = LoggerFactory.getLogger(Storage.class);

    private static Storage storage = null;
    private Config config = null;
    private Map<Position, Field> board = null;
    private int availableMoney = 0;

    public static Storage get() {
        if (storage == null) {
            LOG.info("Init storage");
            storage = new Storage();
        }
        return storage;
    }

    public Config getConfig() {
        return config;
    }

    public Map<Position, Field> getBoard() {
        return board;
    }

    public void setConfig(Config c) {
        if (config == null) {
            LOG.info("Init configuration: {}", c);
            this.config = c;
        } else {
            LOG.warn("Config already initialised");
        }
    }

    public void setBoard(Map<Position, Field> m) {
        this.board = m;
    }

    public int getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(int availableMoney) {
        this.availableMoney = availableMoney;
    }

}
