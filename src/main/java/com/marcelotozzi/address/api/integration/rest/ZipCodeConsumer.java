package com.marcelotozzi.address.api.integration.rest;

import com.marcelotozzi.address.api.business.domain.Address;
import com.marcelotozzi.address.api.integration.rest.entity.ZipcodeResponseAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by marcelotozzi on 18/04/15.
 */
@Service
public class ZipCodeConsumer {
    private static final String API_URL = "http://api.postmon.com.br/v1/cep/";
    private static final Logger LOGGER = LoggerFactory.getLogger(ZipCodeConsumer.class);

    public Address findByZipCode(String zipCode) {
        ZipcodeResponseAPI zipcodeResponseAPI = null;

        RestTemplate restTemplate = new RestTemplate();
        try {
            zipcodeResponseAPI = restTemplate.getForObject(API_URL + zipCode, ZipcodeResponseAPI.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return convertToAddress(zipcodeResponseAPI);
    }

//    public Address(String street, String number, String zipCode, String city, String state, User user) {

    private Address convertToAddress(ZipcodeResponseAPI zipcodeResponseAPI) {
        if (zipcodeResponseAPI != null) {
            Address address = new Address(zipcodeResponseAPI.getLogradouro(), null, zipcodeResponseAPI.getCep(),
                    zipcodeResponseAPI.getCidade(), zipcodeResponseAPI.getEstado(), null);

            address.setDistrict(zipcodeResponseAPI.getBairro());

            return address;
        }
        return null;
    }
}
