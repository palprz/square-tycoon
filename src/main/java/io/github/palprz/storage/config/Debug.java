package io.github.palprz.storage.config;

import java.util.StringJoiner;

public class Debug {

    private boolean visibleAllBoard;

    public Debug() {
        // make Jackson happy
    }

    public Debug(boolean visibleAllBoard) {
        this.visibleAllBoard = visibleAllBoard;
    }

    public boolean isVisibleAllBoard() {
        return visibleAllBoard;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Debug.class.getSimpleName() + "[", "]")
                .add("visibleAllBoard=" + visibleAllBoard)
                .toString();
    }
}
