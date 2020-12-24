package com.realestate.crawler.datasource.queryside.controller;

import com.realestate.crawler.datasource.entity.DataSource;
import com.realestate.crawler.datasource.queryside.handler.GetDataSourceQueryHandler;
import com.realestate.crawler.datasource.queryside.query.GetDataSourceQuery;
import com.realestate.crawler.proto.DataSourceQueryControllerGrpc;
import com.realestate.crawler.proto.Datasource;
import com.realestate.crawler.proto.DatasourceResponse;
import com.realestate.crawler.proto.GetDatasource;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@GRpcService
@RequestMapping("/")
public class DataSourceQueryController extends DataSourceQueryControllerGrpc.DataSourceQueryControllerImplBase {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GetDataSourceQueryHandler getDataSourceQueryHandler;

    @Autowired
    public DataSourceQueryController(GetDataSourceQueryHandler getDataSourceQueryHandler) {
        this.getDataSourceQueryHandler = getDataSourceQueryHandler;
    }

    @Override
    public void get(GetDatasource request, StreamObserver<DatasourceResponse> responseObserver) {

        logger.info("data source request get {}", request);

        Optional<DataSource> optional = getDataSourceQueryHandler.handler(
                GetDataSourceQuery.builder().id(request.getId()).build());

        if (optional.isEmpty()) {
            DatasourceResponse response = DatasourceResponse.newBuilder().build();
            logger.info("server responded {}", response);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        DataSource dataSource = optional.get();
        DatasourceResponse datasourceResponse = DatasourceResponse.newBuilder().setDatasource(Datasource.newBuilder()
                .setId(dataSource.getId())
                .setName(dataSource.getName())
                .setUrl(dataSource.getUrl())
                .setStatus(dataSource.getStatus().getStatus())
                .build()).build();

        logger.info("server responded {}", datasourceResponse);
        responseObserver.onNext(datasourceResponse);
        responseObserver.onCompleted();
    }
}
