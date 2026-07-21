package com.hub.backend.db.dao;

import com.hub.backend.models.User;

public interface UserRepoInterface {

    User login(User user);
    String register(User user);

}
