package model;

public enum Colors {

    BROWN(1),
    RED(2),
    GREEN(3),
    Blue(4),
    White(5),
    Black(6),
    Yellow(7);

    private final int code;
    String name;

    Colors(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getName(int code) {

        return name;
    }
}
