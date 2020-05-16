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

    /**
     * Basic FieldMetadata populated at the beginning of creating board.
     *
     * @param row     The current row of the position.
     * @param column  The current column of the position.
     * @param terrain Terrain type of the field.
     * @return FieldMetadata
     */
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

    /**
     * Generate random terrain based on provided configuration (in percent).
     *
     * @return Terrain type of the field
     */
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

    /**
     * Generate buy price of the field based on the current row, column and configuration for increment increasing
     * price.
     *
     * @param boardRowSize    The configurated number of rows for the board
     * @param boardColumnSize The configurated number of columns for the board
     * @param row             Current row of the field
     * @param column          Current column of the field
     * @return price of the field
     */
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

    /**
     * Check and return number of fields from the middle part of the board.
     *
     * @param boardSize configurated size of the board (column/row)
     * @param position  current position (column/row)
     * @return number of fields to the middle part of the board
     */
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
