package model;

import java.sql.Date;

public class Factor {

    private long id;
    private long basketId;
    private long userId;
    private long price;
    private Date date;
    private int delivery;

    public Factor(long id, long basketId, long userId, long price, Date date, int delivery) {
        this.id = id;
        this.basketId = basketId;
        this.userId = userId;
        this.price = price;
        this.date = date;
        this.delivery = delivery;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBasketId() {
        return basketId;
    }

    public void setBasketId(long basketId) {
        this.basketId = basketId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }
}
