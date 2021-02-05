package model;


public class Address {

    private long id;
    private String address;

    public Address() {
    }

    public Address(long id, String address) {
        this.id = id;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
