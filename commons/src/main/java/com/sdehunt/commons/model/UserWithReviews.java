package com.sdehunt.commons.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserWithReviews extends User {

    private List<Review> reviews;

    public UserWithReviews(User u) {
        super(u);
        this.reviews = new ArrayList<>();
    }

    public UserWithReviews(User u, List<Review> reviews) {
        super(u);
        this.reviews = reviews;
    }

}
