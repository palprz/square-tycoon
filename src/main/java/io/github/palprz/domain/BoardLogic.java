package io.github.palprz.domain;

import com.vaadin.flow.component.html.Div;
import io.github.palprz.storage.Storage;
import io.github.palprz.types.DirectionEnum;
import io.github.palprz.types.Field;
import io.github.palprz.types.FieldMetadata;
import io.github.palprz.types.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BoardLogic {

    private static final Logger LOG = LoggerFactory.getLogger(BoardLogic.class);

    // cross 3x3 and in the middle is the field
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

    public void defineNotAvailableFields(int boardRowSize, int boardColumnSize, int row, int column, Div domElement, FieldMetadata fieldMetadata) {
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

    private boolean isPositionInMiddleBoard(int boardRowSize, int boardColumnSize, int row, int column) {
        int middleRow = boardRowSize / 2;
        int middleColumn = boardColumnSize / 2;
        // shift by 1 - middle is 2x2, not: 1x1
        if ((middleRow == row || middleRow + 1 == row) && (middleColumn == column || middleColumn + 1 == column)) {
            return true;
        }
        return false;
    }

    private boolean isPositionNextMiddleBoard(int boardRowSize, int boardColumnSize, int row, int column) {
        int middleRow = boardRowSize / 2;
        int middleColumn = boardColumnSize / 2;
        // shift once again by 1 comparing to the middle - middle is 2x2 and next area is 4x4 start counted from the middle should be 1x1
        if ((middleRow - 1 == row || middleRow + 2 == row) && (middleColumn - 1 == column || middleColumn + 2 == column)) {
            return true;
        }
        return false;
    }

}
