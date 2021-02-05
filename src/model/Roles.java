package model;

public enum Roles {

    UNKNOWN(0),
    CUSTOMER(1),
    ADMIN(2);


    private final long id;
    String name;

    Roles(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName(int code) {
        return name;
    }
}
