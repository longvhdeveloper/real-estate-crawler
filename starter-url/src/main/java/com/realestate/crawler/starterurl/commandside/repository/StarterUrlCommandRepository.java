package com.realestate.crawler.starterurl.commandside.repository;

import com.realestate.crawler.starterurl.entity.StarterUrl;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StarterUrlCommandRepository extends CrudRepository<StarterUrl, Long> {

    @Query("SELECT su FROM StarterUrl su WHERE su.checkSumUrl = :checkSumUrl AND su.statusCode=1 AND su.dataSourceId = :dataSourceId")
    Optional<StarterUrl> findByCheckSum(@Param("checkSumUrl") String checkSumUrl, @Param("dataSourceId") long dataSourceId);
}
