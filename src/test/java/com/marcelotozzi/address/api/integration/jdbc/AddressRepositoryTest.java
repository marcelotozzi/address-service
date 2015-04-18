package com.marcelotozzi.address.api.integration.jdbc;

import com.marcelotozzi.address.api.AddressServiceApplication;
import com.marcelotozzi.address.api.business.domain.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.marcelotozzi.address.api.helper.AddressHelper.address;
import static com.marcelotozzi.address.api.helper.UserHelper.user;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AddressServiceApplication.class)
@WebAppConfiguration
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void itShouldCreateAnAddress() {
        Address address = address(null, "Av. Mutinga", "22222", null, "05110000", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));

        Address savedAddress = addressRepository.save(address);

        assertNotNull(savedAddress);
        assertEquals(Long.valueOf(3), savedAddress.getId());
    }

    @Test
    public void itShouldGetAnAddress() {
        Address address = addressRepository.findOne(1l);
        assertNotNull(address);
        assertEquals(Long.valueOf(1l), address.getId());
        assertEquals("05110000", address.getZipCode());
        assertEquals("Sao Paulo", address.getCity());
        assertEquals(null, address.getComplement());
        assertEquals("Jd Santo Elias", address.getDistrict());
        assertEquals("22222", address.getNumber());
        assertEquals("SP", address.getState());
        assertEquals("Av. Mutinga", address.getStreet());
    }

    @Test
    public void itShouldUpdateAnAddress() {
        Address oldAddress = address(1l, "Av. Mutinga", "22222", null, "05110000", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));

        Address address = addressRepository.save(oldAddress);

        assertNotNull(address);
        assertEquals(Long.valueOf(1l), address.getId());
        assertEquals("05110000", address.getZipCode());
        assertEquals("Sao Paulo", address.getCity());
        assertEquals(null, address.getComplement());
        assertEquals("Jd Santo Elias", address.getDistrict());
        assertEquals("22222", address.getNumber());
        assertEquals("SP", address.getState());
        assertEquals("Av. Mutinga", address.getStreet());
    }

    @Test
    public void itShouldDeleteAnAddress() {
        addressRepository.delete(2l);

        Address address = addressRepository.findOne(2l);
        assertNull(address);
    }
}