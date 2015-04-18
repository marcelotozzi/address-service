package com.marcelotozzi.address.api.presentation.web.service.rest;

import com.marcelotozzi.address.api.business.component.AddressBusiness;
import com.marcelotozzi.address.api.business.domain.Address;
import com.marcelotozzi.address.api.exception.InvalidZipCodeException;
import com.marcelotozzi.address.api.exception.NotFoundAddressException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by marcelotozzi on 13/04/15.
 */
@Controller
@RequestMapping(value = "addresses")
public class AddressController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private AddressBusiness addressBusiness;

    @RequestMapping(value = "", method = GET, produces = APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity searchAddressBy(@RequestParam("zipcode") String zipCode) {
        try {
            Address address = addressBusiness.searchBy(zipCode);

            return new ResponseEntity<>(address, OK);
        } catch (InvalidZipCodeException e) {
            LOGGER.error("Nao foi possivel encontrar o CEP {}", zipCode);

            return new ResponseEntity<>("CEP invalido", BAD_REQUEST);
        }
    }

    @RequestMapping(value = "", method = POST, consumes = {APPLICATION_JSON_VALUE})
    public
    @ResponseBody
    ResponseEntity create(@RequestBody @Valid final Address address, UriComponentsBuilder builder) {
        HttpHeaders headers = new HttpHeaders();
        try {
            Long addressId = addressBusiness.create(address);

            headers.setLocation(builder.path("/addresses/{id}").buildAndExpand(addressId).toUri());

            return new ResponseEntity<>(headers, CREATED);
        } catch (Exception e) {
            LOGGER.error("Nao foi possivel criar o endereco {} para o usuario", address);

            return new ResponseEntity<>(headers, BAD_REQUEST);
        }
    }

    @RequestMapping(value = "{addressId}", method = GET, produces = {APPLICATION_JSON_VALUE})
    public
    @ResponseBody
    ResponseEntity get(@PathVariable("addressId") final Long addressId) {
        try {
            Address loadedAddress = addressBusiness.load(addressId);

            return new ResponseEntity<>(loadedAddress, OK);
        } catch (NotFoundAddressException e) {
            LOGGER.error("Nao foi possivel encontrar o endereco com o ID {}", addressId);

            return new ResponseEntity<>(NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error("Nao foi possivel encontrar o endereco com ID {}", addressId);

            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    @RequestMapping(value = "{addressId}", method = PUT, consumes = {APPLICATION_JSON_VALUE})
    public
    @ResponseBody
    ResponseEntity edit(@RequestBody @Valid final Address address, @PathVariable("addressId") final Long addressId) {
        try {
            address.setId(addressId);
            addressBusiness.edit(address);

            return new ResponseEntity<>(OK);
        } catch (NotFoundAddressException e) {
            LOGGER.error("Nao foi possivel encontrar o endereco com o ID {}", address.getId());

            return new ResponseEntity<>(NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error("Não foi possível atualizar o endereco", e);

            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    @RequestMapping(value = "{addressId}", method = DELETE)
    public
    @ResponseBody
    ResponseEntity delete(@PathVariable("addressId") final Long addressId) {
        try {
            addressBusiness.remove(addressId);

            return new ResponseEntity<>(OK);
        } catch (NotFoundAddressException e) {
            LOGGER.error("Nao foi possivel encontrar o endereco com o ID {}", addressId);

            return new ResponseEntity<>(NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error("Nao foi possivel remover o endereco com o ID {}", addressId);

            return new ResponseEntity<>(BAD_REQUEST);
        }
    }
}
