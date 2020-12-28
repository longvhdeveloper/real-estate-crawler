package com.realestate.crawler.detailurl.queryside.repository;

import com.realestate.crawler.detailurl.entity.DetailUrl;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailUrlQueryRepository extends CrudRepository<DetailUrl, Long>, JpaSpecificationExecutor<DetailUrl> {
}
