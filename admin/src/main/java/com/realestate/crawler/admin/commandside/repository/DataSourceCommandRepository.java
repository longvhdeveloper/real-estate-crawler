package com.realestate.crawler.admin.commandside.repository;

import com.realestate.crawler.proto.CreateDatasource;
import com.realestate.crawler.proto.Datasource;

import java.util.Optional;

public interface DataSourceCommandRepository {
    boolean create(CreateDatasource createDatasource);

    Optional<Datasource> get(long id);
}
