package com.realestate.crawler.starterurl.queryside.repository;

import com.realestate.crawler.starterurl.entity.StarterUrl;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StarterUrlQueryRepository extends CrudRepository<StarterUrl, Long>, JpaSpecificationExecutor<StarterUrl> {
}
