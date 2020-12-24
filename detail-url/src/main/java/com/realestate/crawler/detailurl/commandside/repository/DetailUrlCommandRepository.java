package com.realestate.crawler.detailurl.commandside.repository;

import com.realestate.crawler.detailurl.entity.DetailUrl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailUrlCommandRepository extends CrudRepository<DetailUrl, Long> {
}
