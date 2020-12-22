package com.realestate.crawler.datasource.commandside.repository;

import com.realestate.crawler.datasource.entity.DataSource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourceCommandRepository extends CrudRepository<DataSource, Long> {
}
