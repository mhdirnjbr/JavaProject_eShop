package model;

public enum Discounts {

    GIFT(2000),
    SPECIAL_GIFT(10000),
    OTHER(1000);

    private final int code;

    Discounts(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
