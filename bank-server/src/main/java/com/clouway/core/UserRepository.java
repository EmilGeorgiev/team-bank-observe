package com.clouway.core;

/**
 * Created by emil on 14-9-27.
 */
public interface UserRepository {
    CurrentAmount getAmountBy(String name);
    boolean isAuthorised(User user);
}
