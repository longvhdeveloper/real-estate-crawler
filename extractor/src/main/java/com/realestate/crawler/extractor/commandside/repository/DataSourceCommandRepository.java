package com.realestate.crawler.extractor.commandside.repository;

import com.realestate.crawler.proto.Datasource;

import java.util.Optional;

public interface DataSourceCommandRepository {
    Optional<Datasource> get(long id);
}
