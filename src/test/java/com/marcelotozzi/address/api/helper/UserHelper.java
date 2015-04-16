package com.marcelotozzi.address.api.helper;

import com.marcelotozzi.address.api.business.domain.Address;
import com.marcelotozzi.address.api.business.domain.User;

import java.util.List;

/**
 * Created by marcelotozzi on 14/04/15.
 */
public class UserHelper {
    public static User user(Long id, String email, String username, String name, List<Address> addresses) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setUsername(username);
        user.setName(name);
        user.setAddresses(addresses);
        return user;
    }

    public static User user(Long id) {
        return user(id, null, null, null, null);
    }
}
