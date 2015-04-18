package com.marcelotozzi.address.api.business.component;

import com.marcelotozzi.address.api.AddressServiceApplication;
import com.marcelotozzi.address.api.business.domain.Address;
import com.marcelotozzi.address.api.exception.InvalidZipCodeException;
import com.marcelotozzi.address.api.exception.NotFoundAddressException;
import com.marcelotozzi.address.api.integration.jdbc.AddressRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.marcelotozzi.address.api.helper.AddressHelper.address;
import static com.marcelotozzi.address.api.helper.UserHelper.user;
import static java.lang.Long.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AddressServiceApplication.class)
@WebAppConfiguration
public class AddressBusinessTest {

    @Autowired
    @InjectMocks
    private AddressBusiness addressBusiness;

    @Mock
    private AddressRepository addressRepository;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void itShouldCreateAnAddress() {
        Address newAddress = address(null, "Av. Mutinga", "5452", null, "05110000", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));

        Address savedAddress = address(11l, "Av. Mutinga", "5452", null, "05110000", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));

        when(addressRepository.save(newAddress)).thenReturn(savedAddress);

        Long generatedId = addressBusiness.create(newAddress);

        assertNotNull(generatedId);
        assertEquals(11, generatedId.longValue());
        verify(addressRepository, times(1)).save(newAddress);
        verifyZeroInteractions(addressRepository);
    }

    @Test
    public void itShouldGetAnAddressById() {
        Address address = address(11l, "Av. Mutinga", "5452", null, "05110000", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));

        when(addressRepository.findOne(11l)).thenReturn(address);

        Address loadedAddress = addressBusiness.load(11l);

        assertNotNull(loadedAddress);
        assertEquals(valueOf(11), loadedAddress.getId());
        verify(addressRepository, times(1)).findOne(11l);
        verifyZeroInteractions(addressRepository);
    }

    @Test
    public void itShouldUpdateAnAddress() {
        Address toUpdateAddress = address(11l, "Av. Mutinga", "22222", null, "05110000", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));
        Address loadedAddress = address(11l, "Av.", "5454", null, "05110000", "Jd Santo", "Sao Paulo", "SP", user(1l));

        when(addressRepository.findOne(11l)).thenReturn(loadedAddress);

        addressBusiness.edit(toUpdateAddress);

        verify(addressRepository, times(1)).findOne(11l);
        verify(addressRepository, times(1)).save(toUpdateAddress);
        verifyZeroInteractions(addressRepository);
    }

    @Test
    public void itShouldDeleteAnAddressById() {
        addressBusiness.remove(12l);

        verify(addressRepository, times(1)).delete(12l);
        verifyZeroInteractions(addressRepository);
    }

    @Test
    public void itShouldSearchByACorrectZipCode() {
        Address address = address(11l, "Av. Mutinga", "5452", null, "22333800", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));

        when(addressRepository.findByZipCode("22333800")).thenReturn(address);

        Address addressByZipCode = addressBusiness.searchBy("22333800");

        assertNotNull(addressByZipCode);
        assertEquals(address.getId(), addressByZipCode.getId());
        assertEquals(address.getCity(), addressByZipCode.getCity());
        assertEquals(address.getComplement(), addressByZipCode.getComplement());
        assertEquals(address.getDistrict(), addressByZipCode.getDistrict());
        assertEquals(address.getNumber(), addressByZipCode.getNumber());
        assertEquals(address.getState(), addressByZipCode.getState());
        assertEquals(address.getStreet(), addressByZipCode.getStreet());
        assertEquals(address.getZipCode(), addressByZipCode.getZipCode());
    }

    @Test
    public void itShouldSearchByZipCodeWithLastCharWrong() {
        Address address = address(11l, "Av. Mutinga", "5452", null, "11222550", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));

        when(addressRepository.findByZipCode("11222555")).thenReturn(null);
        when(addressRepository.findByZipCode("11222550")).thenReturn(address);

        Address addressByZipCode = addressBusiness.searchBy("11222555");

        assertNotNull(addressByZipCode);
        assertEquals(address.getId(), addressByZipCode.getId());
        assertEquals(address.getCity(), addressByZipCode.getCity());
        assertEquals(address.getComplement(), addressByZipCode.getComplement());
        assertEquals(address.getDistrict(), addressByZipCode.getDistrict());
        assertEquals(address.getNumber(), addressByZipCode.getNumber());
        assertEquals(address.getState(), addressByZipCode.getState());
        assertEquals(address.getStreet(), addressByZipCode.getStreet());
        assertEquals(address.getZipCode(), addressByZipCode.getZipCode());

        verify(addressRepository, times(2)).findByZipCode(anyString());
        verifyZeroInteractions(addressRepository);
    }

    @Test
    public void itShouldSearchByZipCodeWithPenultCharWrong() {
        Address address = address(11l, "Av. Mutinga", "5452", null, "11222500", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));

        when(addressRepository.findByZipCode("11222555")).thenReturn(null);
        when(addressRepository.findByZipCode("11222550")).thenReturn(null);
        when(addressRepository.findByZipCode("11222500")).thenReturn(address);

        Address addressByZipCode = addressBusiness.searchBy("11222555");

        assertNotNull(addressByZipCode);
        assertEquals(address.getId(), addressByZipCode.getId());
        assertEquals(address.getCity(), addressByZipCode.getCity());
        assertEquals(address.getComplement(), addressByZipCode.getComplement());
        assertEquals(address.getDistrict(), addressByZipCode.getDistrict());
        assertEquals(address.getNumber(), addressByZipCode.getNumber());
        assertEquals(address.getState(), addressByZipCode.getState());
        assertEquals(address.getStreet(), addressByZipCode.getStreet());
        assertEquals(address.getZipCode(), addressByZipCode.getZipCode());

        verify(addressRepository, times(3)).findByZipCode(anyString());
        verifyZeroInteractions(addressRepository);
    }

    @Test
    public void itShouldSearchByZipCodeWithAntePenultCharWrong() {
        Address address = address(11l, "Av. Mutinga", "5452", null, "11222000", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));

        when(addressRepository.findByZipCode("11222555")).thenReturn(null);
        when(addressRepository.findByZipCode("11222550")).thenReturn(null);
        when(addressRepository.findByZipCode("11222500")).thenReturn(null);
        when(addressRepository.findByZipCode("11222000")).thenReturn(address);

        Address addressByZipCode = addressBusiness.searchBy("11222555");

        assertNotNull(addressByZipCode);
        assertEquals(address.getId(), addressByZipCode.getId());
        assertEquals(address.getCity(), addressByZipCode.getCity());
        assertEquals(address.getComplement(), addressByZipCode.getComplement());
        assertEquals(address.getDistrict(), addressByZipCode.getDistrict());
        assertEquals(address.getNumber(), addressByZipCode.getNumber());
        assertEquals(address.getState(), addressByZipCode.getState());
        assertEquals(address.getStreet(), addressByZipCode.getStreet());
        assertEquals(address.getZipCode(), addressByZipCode.getZipCode());

        verify(addressRepository, times(4)).findByZipCode(anyString());
        verifyZeroInteractions(addressRepository);
    }

    @Test(expected = InvalidZipCodeException.class)
    public void itShouldNotFindAZipCode() {
        when(addressRepository.findByZipCode("55222666")).thenReturn(null);
        when(addressRepository.findByZipCode("55222660")).thenReturn(null);
        when(addressRepository.findByZipCode("55222600")).thenReturn(null);
        when(addressRepository.findByZipCode("55222000")).thenReturn(null);

        addressBusiness.searchBy("55222666");
    }
}