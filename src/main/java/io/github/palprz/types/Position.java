package io.github.palprz.types;

import java.util.StringJoiner;

public class Position {

    private int row;
    private int column;

    public Position(int r, int c) {
        this.row = r;
        this.column = c;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (row != position.row) return false;
        return column == position.column;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Position.class.getSimpleName() + "[", "]")
                .add("row=" + row)
                .add("column=" + column)
                .toString();
    }

}
