package com.realestate.crawler.datasource.queryside.handler;

import com.realestate.crawler.datasource.entity.DataSource;
import com.realestate.crawler.datasource.queryside.query.GetDataSourceQuery;
import com.realestate.crawler.datasource.queryside.repository.DataSourceQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetDataSourceQueryHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DataSourceQueryRepository dataSourceQueryRepository;

    @Autowired
    public GetDataSourceQueryHandler(DataSourceQueryRepository dataSourceQueryRepository) {
        this.dataSourceQueryRepository = dataSourceQueryRepository;
    }

    public Optional<DataSource> handler(GetDataSourceQuery query) {
        return dataSourceQueryRepository.findById(query.getId());
    }
}
