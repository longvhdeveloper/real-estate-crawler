package com.realestate.crawler.datasource.queryside.repository;

import com.realestate.crawler.datasource.entity.DataSource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourceQueryRepository extends CrudRepository<DataSource, Long> {
}
