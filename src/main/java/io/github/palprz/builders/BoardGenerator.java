package io.github.palprz.builders;

import io.github.palprz.storage.config.Config;
import io.github.palprz.storage.Storage;
import io.github.palprz.storage.config.GeneratingTerrain;
import io.github.palprz.types.TerrainEnum;
import io.github.palprz.types.FieldMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BoardGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(BoardGenerator.class);

    public FieldMetadata createBasicFieldMetadata(Integer row, Integer column, TerrainEnum terrain) {
        FieldMetadata fieldMetadata = new FieldMetadata();
        fieldMetadata.setColumn(column);
        fieldMetadata.setRow(row);
        fieldMetadata.setType(terrain);

        Config config = Storage.get().getConfig();
        int boardRowSize = config.getBoardRowSize();
        int boardColumnSize = config.getBoardColumnSize();
        int fieldPrice = this.generateFieldPrice(boardRowSize, boardColumnSize, row, column);
        fieldMetadata.setFieldPrice(fieldPrice);

        return fieldMetadata;
    }

    public TerrainEnum getRandomTerrain() {
        List<GeneratingTerrain> generatingTerrains = Storage.get().getConfig().getGeneratingTerrains();
        LOG.debug("Generating fields based on ratios: {}", generatingTerrains);
        int rand = (int) (Math.random() * 100) + 1;

        int beginning = 1;
        int end = 0;
        for (GeneratingTerrain terrain : generatingTerrains) {
            end = end + terrain.getPercent();
            LOG.debug("Rand: {} for beginning and end: {}, {} for terrain: {}", rand, beginning, end, terrain);
            if (beginning <= rand && rand <= end) {
                return terrain.getTerrain();
            }
            beginning = end + 1;
        }

        return TerrainEnum.UNKNOWN;
    }

    public int generateFieldPrice(int boardRowSize, int boardColumnSize, int row, int column) {
        LOG.debug("Generate field price: {}, {}, {}, {}", boardRowSize, boardColumnSize, row, column);
        int increasePrice = Storage.get().getConfig().getIncreaseFieldPrice();

        int rowDistance = this.defineDistanceFromMiddleBoard(boardRowSize, row);
        int columnDistance = this.defineDistanceFromMiddleBoard(boardColumnSize, column);

        LOG.debug("Row distance from middle of the board: {} ", rowDistance);
        LOG.debug("Column distance from middle of the board: {} ", columnDistance);
        // Take bigger distance from the middle
        if (rowDistance > columnDistance) {
            return rowDistance * increasePrice;
        } else if (rowDistance < columnDistance) {
            return columnDistance * increasePrice;
        } else {
            // whatever
            return rowDistance * increasePrice;
        }
    }

    private int defineDistanceFromMiddleBoard(int boardSize, int position) {
        if (position > (boardSize / 2)) {
            // remove the first half of the board and pretend that is at the beginning of the board
            // '-1' to include square (2x2) at the beginning. Without this change, it will be
            // always a 1x1 square as middle
            return position - 1 - (boardSize / 2);
        } else {
            // revert the result and ignore '-1' flag for the beginning square
            return Math.abs(position - (boardSize / 2));
        }
    }

}
