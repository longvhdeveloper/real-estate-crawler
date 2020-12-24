package com.realestate.crawler.detailurl.commandside.controller;

import com.realestate.crawler.detailurl.commandside.command.CreateDetailUrlCommand;
import com.realestate.crawler.detailurl.commandside.handler.CreateDetailUrlCommandHandler;
import com.realestate.crawler.proto.CreateDetailUrl;
import com.realestate.crawler.proto.DetailurlCommandControllerGrpc;
import com.realestate.crawler.proto.Response;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GRpcService
@Slf4j
public class DetailUrlCommandController extends DetailurlCommandControllerGrpc.DetailurlCommandControllerImplBase {

    final CreateDetailUrlCommandHandler createDetailUrlCommandHandler;

    @Autowired
    public DetailUrlCommandController(CreateDetailUrlCommandHandler createDetailUrlCommandHandler) {
        this.createDetailUrlCommandHandler = createDetailUrlCommandHandler;
    }

    @Override
    public void create(CreateDetailUrl request, StreamObserver<Response> responseObserver) {
        log.info("create detail url request received {}", request);

        int status = 0;
        if (createDetailUrlCommandHandler.handler(
                CreateDetailUrlCommand.builder().url(request.getUrl()).dataSourceId(request.getDataSourceId()).build())) {
            status = 1;
        }
        Response response = Response.newBuilder().setStatus(status).build();
        log.info("server responded {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
