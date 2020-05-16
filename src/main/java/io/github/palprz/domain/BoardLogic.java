package io.github.palprz.domain;

import com.vaadin.flow.component.html.Div;
import io.github.palprz.storage.Storage;
import io.github.palprz.types.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BoardLogic {

    private static final Logger LOG = LoggerFactory.getLogger(BoardLogic.class);

    /**
     * Get map with directions and fields in the cross (3x3) of the fields where provided position is in the middle of
     * that cross.
     *
     * @param r Row of the position
     * @param c Column of the position
     * @return map with directions and fields with 4 keys
     */
    public Map<DirectionEnum, Field> getFieldsAroundByDirection(int r, int c) {
        Map<Position, Field> map = Storage.get().getBoard();

        Map<DirectionEnum, Field> directionFields = new HashMap<>();
        directionFields.put(DirectionEnum.NORTH, map.get(new Position(r - 1, c)));
        directionFields.put(DirectionEnum.EAST, map.get(new Position(r, c + 1)));
        directionFields.put(DirectionEnum.SOUTH, map.get(new Position(r + 1, c)));
        directionFields.put(DirectionEnum.WEST, map.get(new Position(r, c - 1)));

        return directionFields;
    }

    // square 3x3 and in the middle is the field

    /**
     * Get map with directions and fields in around square (3x3) of the fields where provided position is in the middle
     * of that square.
     *
     * @param r Row of the position
     * @param c Column of the position
     * @return map with directions and fields with 8 keys
     */
    public Map<Position, Field> getFieldsAround(int r, int c) {
        Map<Position, Field> map = Storage.get().getBoard();

        Map<Position, Field> fieldsAround = new HashMap<>();
        // North West
        fieldsAround.put(new Position(r - 1, c - 1), map.get(new Position(r - 1, c - 1)));
        // North
        fieldsAround.put(new Position(r - 1, c), map.get(new Position(r - 1, c)));
        // North East
        fieldsAround.put(new Position(r - 1, c + 1), map.get(new Position(r - 1, c + 1)));
        // West
        fieldsAround.put(new Position(r, c - 1), map.get(new Position(r, c - 1)));
        // East
        fieldsAround.put(new Position(r, c + 1), map.get(new Position(r, c + 1)));
        // South West
        fieldsAround.put(new Position(r + 1, c - 1), map.get(new Position(r + 1, c - 1)));
        // South
        fieldsAround.put(new Position(r + 1, c), map.get(new Position(r + 1, c)));
        // South East
        fieldsAround.put(new Position(r + 1, c + 1), map.get(new Position(r + 1, c + 1)));

        return fieldsAround;
    }

    /**
     * Define if provided field should be availble to set production for end-user
     *
     * @param boardRowSize    Configured rows number of the board
     * @param boardColumnSize Configured columns number of the board
     * @param domElement      Div element to be updated by CSS
     * @param fieldMetadata   Contains current position and price of the field. Can be updated.
     */
    public void defineNotAvailableFields(int boardRowSize, int boardColumnSize, Div domElement, FieldMetadata fieldMetadata) {
        int row = fieldMetadata.getRow();
        int column = fieldMetadata.getColumn();
        if (this.isPositionInMiddleBoard(boardRowSize, boardColumnSize, row, column)) {
            fieldMetadata.setVisible(true);
            fieldMetadata.setDimmed(false);
            return;
        }

        if (this.isPositionNextMiddleBoard(boardRowSize, boardColumnSize, row, column)) {
            fieldMetadata.setVisible(true);

            Div title = (Div) domElement.getChildren().findFirst().get();
            title.setText("Price: $" + fieldMetadata.getFieldPrice());
            title.addClassNames("field-title", "field-price");

            domElement.addClassName("dimmed");
            return;
        }

        boolean availableAllBoardDebug = Storage.get().getConfig().getDebug().isVisibleAllBoard();
        if (!availableAllBoardDebug) {
            domElement.addClassName("not-visible");
        }
    }

    /**
     * Check if provided position is in the middle of the board.
     *
     * @param boardRowSize    Configured rows number of the board.
     * @param boardColumnSize Configured columns number of the board.
     * @param row             Row of the position to be check
     * @param column          Column of the position to be check
     * @return <code>true</code> if it's in the middle of the board
     */
    private boolean isPositionInMiddleBoard(int boardRowSize, int boardColumnSize, int row, int column) {
        int middleRow = boardRowSize / 2;
        int middleColumn = boardColumnSize / 2;
        // shift by 1 - middle is 2x2, not: 1x1
        if ((middleRow == row || middleRow + 1 == row) && (middleColumn == column || middleColumn + 1 == column)) {
            return true;
        }
        return false;
    }

    /**
     * Check if provided position is next (1 field) to the middle of the board.
     *
     * @param boardRowSize    Configured rows number of the board.
     * @param boardColumnSize Configured columns number of the board.
     * @param row             Row of the position to be check
     * @param column          Column of the position to be check
     * @return <code>true</code> when it's next to the middle of the board
     */
    private boolean isPositionNextMiddleBoard(int boardRowSize, int boardColumnSize, int row, int column) {
        int middleRow = boardRowSize / 2;
        int middleColumn = boardColumnSize / 2;
        // shift once again by 1 comparing to the middle - middle is 2x2 and next area is 4x4 start counted from the middle should be 1x1
        if ((middleRow - 1 == row
                || middleRow == row
                || middleRow + 1 == row
                || middleRow + 2 == row)

                && (middleColumn - 1 == column
                || middleColumn == column
                || middleColumn + 1 == column
                || middleColumn + 2 == column)) {
            return true;
        }
        return false;
    }

    /**
     * Check if provided field had got market next to it.
     *
     * @param field Field to be check
     * @return <code>true</> when field contains minimum 1 market next to that field
     */
    public boolean isMarketNextField(Field field) {
        Map<DirectionEnum, Field> fieldsAround = this.getFieldsAroundByDirection(
                field.getFieldMetadata().getRow(),
                field.getFieldMetadata().getColumn());
        for (Map.Entry<DirectionEnum, Field> entry : fieldsAround.entrySet()) {
            Field nextField = entry.getValue();
            if (nextField == null) {
                // edge of the board
                continue;
            }

            if (nextField.getFieldMetadata().getProduction() == ProductionEnum.MARKET) {
                return true;
            }
        }
        return false;
    }
}
