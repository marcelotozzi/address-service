package com.marcelotozzi.address.api.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcelotozzi.address.api.business.domain.Address;
import com.marcelotozzi.address.api.business.domain.User;

/**
 * Created by marcelotozzi on 14/04/15.
 */
public class AddressHelper {

    public static String toJSON(Address address) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(address);
    }

    public static Address address(Long id, String street, String number, String complement, String zipcode,
                                  String district, String city, String state, User user) {
        Address address = new Address(street, number, zipcode, city, state, user);
        address.setId(id);
        address.setComplement(complement);
        address.setDistrict(district);

        return address;
    }
}
