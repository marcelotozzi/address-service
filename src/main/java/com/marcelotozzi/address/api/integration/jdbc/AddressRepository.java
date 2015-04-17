package com.marcelotozzi.address.api.integration.jdbc;

import com.marcelotozzi.address.api.business.domain.Address;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcelotozzi on 14/04/15.
 */
@Repository
@Transactional
public interface AddressRepository extends CrudRepository<Address, Long> {
    @Query(value = "select a from Address a where a.zipCode = :zipcode")
    public Address findByZipCode(@Param("zipcode") String zipCode);
}