package com.realestate.crawler.downloader.commandside.repository;

import com.realestate.crawler.proto.Datasource;

import java.util.Optional;

public interface IDataSourceRepository {
    Optional<Datasource> get(long id);
}
