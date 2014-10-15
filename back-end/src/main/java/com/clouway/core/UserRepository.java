package com.clouway.core;

/**
 * Created by emil on 14-9-27.
 */
public interface UserRepository {

    void add(User user);

    User getBy(String username);
}