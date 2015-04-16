package com.marcelotozzi.address.api.integration.jdbc;

import com.marcelotozzi.address.api.business.domain.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by marcelotozzi on 14/04/15.
 */
@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {
}