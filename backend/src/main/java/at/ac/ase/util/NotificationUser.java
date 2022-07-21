package at.ac.ase.util;

import java.security.Principal;

public final class NotificationUser implements Principal {

    private String name;

    public NotificationUser(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}