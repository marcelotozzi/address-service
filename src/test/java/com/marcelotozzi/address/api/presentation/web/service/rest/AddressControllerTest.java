package com.marcelotozzi.address.api.presentation.web.service.rest;

import com.marcelotozzi.address.api.AddressServiceApplication;
import com.marcelotozzi.address.api.business.component.AddressBusiness;
import com.marcelotozzi.address.api.business.domain.Address;
import com.marcelotozzi.address.api.exception.InvalidZipCodeException;
import com.marcelotozzi.address.api.exception.NotFoundAddressException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static com.marcelotozzi.address.api.helper.AddressHelper.address;
import static com.marcelotozzi.address.api.helper.AddressHelper.toJSON;
import static com.marcelotozzi.address.api.helper.UserHelper.user;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AddressServiceApplication.class)
@WebAppConfiguration
public class AddressControllerTest {

    @Autowired
    @InjectMocks
    private AddressController addressController;

    @Mock
    private AddressBusiness addressBusiness;

    private MockMvc mvc;

    @Before
    public void setUp() {
        initMocks(this);
        mvc = standaloneSetup(addressController).build();
    }

    @Test
    public void itShouldReturnACorrespondingAddressFromAValidZipCode() throws Exception {
        String zipCode = "05110000";
        Address address = address(11l, "Av. Mutinga", "5452", null, zipCode, "Jd Santo Elias", "Sao Paulo", "SP", user(1l));
        String expectedAddressJSON = toJSON(address);

        when(addressBusiness.searchBy(zipCode)).thenReturn(address);

        mvc.perform(get("/addresses").param("zipcode", zipCode))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string(expectedAddressJSON));

        verify(addressBusiness, times(1)).searchBy(zipCode);
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldReturnAMessageWrongZipCode() throws Exception {
        String zipCode = "11111111";

        when(addressBusiness.searchBy(zipCode)).thenThrow(new InvalidZipCodeException());

        mvc.perform(get("/addresses").param("zipcode", zipCode))
                .andExpect(content().string("CEP invalido"))
                .andExpect(status().isBadRequest());

        verify(addressBusiness, times(1)).searchBy(zipCode);
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldNotCreateANewAddressToUser() throws Exception {
        Address newAddress = address(null, "Av. Mutinga", "5452", null, "05110000", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));
        String json = toJSON(newAddress);

        when(addressBusiness.create(any(Address.class))).thenThrow(new RuntimeException());

        mvc.perform(post("/addresses").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andExpect(header().doesNotExist("Location"));

        verify(addressBusiness, times(1)).create(any(Address.class));
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldCreateANewAddressToUser() throws Exception {
        Address newAddress = address(null, "Av. Mutinga", "5452", null, "05110000", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));
        String json = toJSON(newAddress);

        when(addressBusiness.create(any(Address.class))).thenReturn(11l);

        mvc.perform(post("/addresses").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(content().string(""))
                .andExpect(header().string("Location", "http://localhost/addresses/11"));

        verify(addressBusiness, times(1)).create(any(Address.class));
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldNotCreateANewAddressToUserBecauseStreetIsNull() throws Exception {
        Address newAddress = address(null, null, "5452", null, "05110000", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));
        String json = toJSON(newAddress);

        mvc.perform(post("/addresses").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andExpect(header().doesNotExist("Location"));

        verify(addressBusiness, times(0)).create(any(Address.class));
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldNotCreateANewAddressToUserBecauseNumberIsNull() throws Exception {
        Address newAddress = address(null, "Av. Mutinga", null, null, "05110000", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));
        String json = toJSON(newAddress);

        mvc.perform(post("/addresses").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andExpect(header().doesNotExist("Location"));

        verify(addressBusiness, times(0)).create(any(Address.class));
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldNotCreateANewAddressToUserBecauseZipCodeIsNull() throws Exception {
        Address newAddress = address(null, "Av. Mutinga", "5452", null, null, "Jd Santo Elias", "Sao Paulo", "SP", user(1l));
        String json = toJSON(newAddress);

        mvc.perform(post("/addresses").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andExpect(header().doesNotExist("Location"));

        verify(addressBusiness, times(0)).create(any(Address.class));
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldNotCreateANewAddressToUserBecauseCityIsNull() throws Exception {
        Address newAddress = address(null, "Av. Mutinga", "5452", null, "05110000", "Jd Santo Elias", null, "SP", user(1l));
        String json = toJSON(newAddress);

        mvc.perform(post("/addresses").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andExpect(header().doesNotExist("Location"));

        verify(addressBusiness, times(0)).create(any(Address.class));
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldNotCreateANewAddressToUserBecauseStateIsNull() throws Exception {
        Address newAddress = address(null, "Av. Mutinga", "5452", null, "05110000", "Jd Santo Elias", "Sao Paulo", null, user(1l));
        String json = toJSON(newAddress);

        mvc.perform(post("/addresses").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""))
                .andExpect(header().doesNotExist("Location"));

        verify(addressBusiness, times(0)).create(any(Address.class));
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldGetAnAddressById() throws Exception {
        Address loadedAddress = address(11l, "Av. Mutinga", "5452", null, "05110000", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));
        String json = toJSON(loadedAddress);

        when(addressBusiness.load(loadedAddress.getId())).thenReturn(loadedAddress);

        mvc.perform(get("/addresses/11"))
                .andExpect(status().isOk())
                .andExpect(content().string(json));

        verify(addressBusiness, times(1)).load(11l);
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldNotFoundAnAddressByIdBecauseNotFoundAddressException() throws Exception {
        when(addressBusiness.load(11l)).thenThrow(new NotFoundAddressException());

        mvc.perform(get("/addresses/11"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

        verify(addressBusiness, times(1)).load(11l);
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldNotFoundAnAddressByIdBecauseException() throws Exception {
        when(addressBusiness.load(11l)).thenThrow(new RuntimeException());

        mvc.perform(get("/addresses/11"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));

        verify(addressBusiness, times(1)).load(11l);
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldUpdateAnAddress() throws Exception {
        Address address = address(12l, "Av. Mutinga", "5452", null, "05110000", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));
        String json = toJSON(address);

        mvc.perform(put("/addresses/12").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(addressBusiness, times(1)).edit(any(Address.class));
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldNotUpdateAnAddressBecauseNotFound() throws Exception {
        Address address = address(12l, "Av. Mutinga", "5452", null, "05110000", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));
        String json = toJSON(address);

        doThrow(new NotFoundAddressException()).when(addressBusiness).edit(any(Address.class));

        mvc.perform(put("/addresses/12").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

        verify(addressBusiness, times(1)).edit(any(Address.class));
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldNotUpdateAnAddress() throws Exception {
        Address address = address(12l, "Av. Mutinga", "5452", null, "05110000", "Jd Santo Elias", "Sao Paulo", "SP", user(1l));
        String json = toJSON(address);

        doThrow(new RuntimeException()).when(addressBusiness).edit(any(Address.class));

        mvc.perform(put("/addresses/12").contentType(APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));

        verify(addressBusiness, times(1)).edit(any(Address.class));
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldDeleteAnAddress() throws Exception {
        doNothing().when(addressBusiness).remove(12l);

        mvc.perform(delete("/addresses/12"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(addressBusiness, times(1)).remove(12l);
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldNotDeleteAnAddressBecauseNotFound() throws Exception {
        doThrow(new NotFoundAddressException()).when(addressBusiness).remove(any(Long.class));

        mvc.perform(delete("/addresses/12"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

        verify(addressBusiness, times(1)).remove(12l);
        verifyZeroInteractions(addressBusiness);
    }

    @Test
    public void itShouldNotDeleteAnAddress() throws Exception {
        doThrow(new RuntimeException()).when(addressBusiness).remove(any(Long.class));

        mvc.perform(delete("/addresses/12"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));

        verify(addressBusiness, times(1)).remove(12l);
        verifyZeroInteractions(addressBusiness);
    }
}