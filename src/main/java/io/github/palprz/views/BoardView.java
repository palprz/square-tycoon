package io.github.palprz.views;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import io.github.palprz.builders.BoardGenerator;
import io.github.palprz.builders.DOMBuilder;
import io.github.palprz.storage.config.Config;
import io.github.palprz.storage.Storage;
import io.github.palprz.types.*;
import io.github.palprz.domain.BoardLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;

@CssImport("./styles/style.css")
public class BoardView {

    private static final Logger LOG = LoggerFactory.getLogger(BoardView.class);

    @Inject
    private DOMBuilder domBuilder;

    @Inject
    private BoardGenerator boardGenerator;

    @Inject
    private BoardLogic boardLogic;

    private Map<Position, Field> board = new HashMap<>();

    public Div init() {
        Storage storage = Storage.get();
        Config config = storage.getConfig();

        Div boardContainer = new Div();
        boardContainer.addClassName("board-container");

        int boardRowSize = config.getBoardRowSize();
        int boardColumnSize = config.getBoardColumnSize();
        for (int row = 1; boardRowSize >= row; row++) {
            for (int column = 1; boardColumnSize >= column; column++) {

                Position position = new Position(row, column);

                // New field, not owned
                TerrainEnum terrain = boardGenerator.getRandomTerrain();
                Div domElement = domBuilder.createVisibleFieldElement(board, position, terrain);
                FieldMetadata fieldMetadata = boardGenerator.createBasicFieldMetadata(row, column, terrain);
                boardLogic.defineNotAvailableFields(boardRowSize, boardColumnSize, row, column, domElement, fieldMetadata);

                Field field = new Field();
                field.setElement(domElement);
                field.setFieldMetadata(fieldMetadata);

                board.put(position, field);
                boardContainer.add(domElement);
            }
        }

        storage.setBoard(board);
        return boardContainer;
    }

}
