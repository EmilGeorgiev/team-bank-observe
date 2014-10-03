package com.clouway.core;

/**
 * Created by emil on 14-9-27.
 */
public interface UserRepository {

    boolean isAuthorised(User user);

    boolean exists(String username);

    void add(User user);
}