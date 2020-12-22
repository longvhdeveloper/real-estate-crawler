package com.realestate.crawler.datasource.commandside.controller;

import com.realestate.crawler.datasource.commandside.command.CreateDataSourceCommand;
import com.realestate.crawler.datasource.commandside.handler.CreateDataSourceCommandHandler;
import com.realestate.crawler.proto.CreateDatasource;
import com.realestate.crawler.proto.DataSourceCommandControllerGrpc;
import com.realestate.crawler.proto.Response;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@GRpcService
@RequestMapping("/")
public class DataSourceCommandController extends DataSourceCommandControllerGrpc.DataSourceCommandControllerImplBase {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CreateDataSourceCommandHandler createDataSourceCommandHandler;

    @Autowired
    public DataSourceCommandController(CreateDataSourceCommandHandler createDataSourceCommandHandler) {
        this.createDataSourceCommandHandler = createDataSourceCommandHandler;
    }

    @Override
    public void create(CreateDatasource request, StreamObserver<Response> responseObserver) {

        logger.info("data source request received {}", request);

        int status = 0;
        if (createDataSourceCommandHandler.handler(
                CreateDataSourceCommand.builder().name(request.getName()).url(request.getUrl()).build())) {
            status = 1;
        }
        Response response = Response.newBuilder().setStatus(status).build();
        logger.info("server responded {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
