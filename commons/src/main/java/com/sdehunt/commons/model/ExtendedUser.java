package com.sdehunt.commons.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExtendedUser extends User {

    private List<Review> reviews;
    private List<String> repos;

    public ExtendedUser(User u) {
        super(u);
        this.reviews = new ArrayList<>();
    }

    public ExtendedUser(User u, List<Review> reviews) {
        super(u);
        this.reviews = reviews != null ? reviews : new ArrayList<>();
    }

}
