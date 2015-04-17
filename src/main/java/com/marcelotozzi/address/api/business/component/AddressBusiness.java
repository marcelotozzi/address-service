package com.marcelotozzi.address.api.business.component;

import com.marcelotozzi.address.api.business.domain.Address;
import com.marcelotozzi.address.api.exception.InvalidZipCodeException;
import com.marcelotozzi.address.api.exception.NotFoundAddressException;
import com.marcelotozzi.address.api.integration.jdbc.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcelotozzi on 14/04/15.
 */
@Service
public class AddressBusiness {

    @Autowired
    private AddressRepository addressRepository;

    public Address searchBy(String zipCode) {
        Address address = searchByAModifyZipCode(zipCode, 0);

        return address;
    }

    private Address searchByAModifyZipCode(String zipCode, int counter) {
        Address address = addressRepository.findByZipCode(zipCode);

        if (address == null && !zipCode.matches("\\d{4}0000")) {
            counter += 1;
            address = searchByAModifyZipCode(mountZipCode(zipCode, counter), counter);
        } else if (zipCode.matches("\\d{4}0000")) {
            throw new InvalidZipCodeException();
        }

        return address;
    }

    private String mountZipCode(String zipCode, int counter) {
        StringBuilder sb = new StringBuilder(zipCode.substring(0, zipCode.length() - counter));
        for (int i = 0; i < counter; i++) {
            sb.append("0");
        }

        return sb.toString();
    }

    public Long create(Address address) {
        try {
            Address createdAddress = addressRepository.save(address);
            return createdAddress.getId();

        } catch (Exception e) {
            throw e;
        }
    }

    public Address load(Long addressId) {
        try {
            Address address = addressRepository.findOne(addressId);

            if (address != null) {
                return address;
            }

            throw new NotFoundAddressException();
        } catch (Exception e) {
            throw e;
        }
    }

    public void edit(Address address) {
        try {
            addressRepository.save(address);
        } catch (Exception e) {
            throw e;
        }
    }

    public void remove(Long addressId) {
        try {
            addressRepository.delete(addressId);
        } catch (Exception e) {
            throw e;
        }
    }
}
