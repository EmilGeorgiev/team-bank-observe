package com.clouway.core;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * Created by clouway on 14-9-26.
 */
public class SessionIdGenerator implements IdGenerator{
    @Override
    public String generateFor(User user) {
        HashFunction hashFunction = Hashing.sha1();
        HashCode hashCode = hashFunction.hashString(user.getName()+user.getPassword()+System.currentTimeMillis());
        return hashCode.toString();
    }
}