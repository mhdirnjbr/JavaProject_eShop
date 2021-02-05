package model;


public class Discount {

    private long id;
    private String code;
    private long discountValue;
    private boolean isUsed;

    public Discount(long id, String code, long discountValue, boolean isUsed) {
        this.id = id;
        this.code = code;
        this.discountValue = discountValue;
        this.isUsed = isUsed;
    }

    public Discount() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(long discountValue) {
        this.discountValue = discountValue;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    @Override
    public String toString() {
        return code;
    }
}