package com.sdehunt.commons.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExtendedUser extends User {

    private List<Review> reviews = new ArrayList<>();
    private List<String> repos = new ArrayList<>();

    public ExtendedUser(User u) {
        super(u);
    }

    public ExtendedUser(User u, List<Review> reviews) {
        super(u);
        this.reviews = reviews != null ? reviews : new ArrayList<>();
    }

}
