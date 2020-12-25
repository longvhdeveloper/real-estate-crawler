package com.realestate.crawler.property.commandside.repository;

import com.realestate.crawler.property.entity.Property;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyRepository extends CrudRepository<Property, Long> {
    @Query("SELECT p FROM Property p WHERE p.checkSumUrl = :checkSumUrl")
    Optional<Property> findByCheckSum(@Param("checkSumUrl") String checkSumUrl);
}
