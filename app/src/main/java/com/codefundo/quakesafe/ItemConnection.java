package com.codefundo.quakesafe;

public class ItemConnection {
    String name;
    int mob;
    boolean status, is_fav;

    public ItemConnection(String name, int mob, boolean status, boolean is_fav) {
        this.name = name;
        this.mob = mob;
        this.status = status;
        this.is_fav = is_fav;
    }

    public String getName() {
        return name;
    }

    public int getMob() {
        return mob;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isIs_fav() {
        return is_fav;
    }
}
