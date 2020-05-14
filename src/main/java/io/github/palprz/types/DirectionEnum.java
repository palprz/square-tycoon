package io.github.palprz.types;

public enum DirectionEnum {
    NORTH("north", "south"),
    EAST("east", "west"),
    SOUTH("south", "north"),
    WEST("west","east");

    private String name;
    private String oppositeDirection;

    private DirectionEnum(String name, String oppositeDirection) {
        this.name = name;
        this.oppositeDirection = oppositeDirection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOppositeDirection() {
        return oppositeDirection;
    }

    public void setOppositeDirection(String oppositeDirection) {
        this.oppositeDirection = oppositeDirection;
    }

}
