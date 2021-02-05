package model;

public enum Categories {

    DIGITAL(1),
    CLOTHES(2),
    HEALTH(3),
    OTHER(4);

    final long categoryId;

    Categories(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getCategoryId() {
        return categoryId;
    }
}
