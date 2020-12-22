package com.realestate.crawler.starterurl.commandside.controller;

import com.realestate.crawler.proto.CreateStaterUrl;
import com.realestate.crawler.proto.Response;
import com.realestate.crawler.proto.StarterUrlCommandControllerGrpc;
import com.realestate.crawler.proto.Url;
import com.realestate.crawler.starterurl.commandside.command.CreateStaterUrlCommand;
import com.realestate.crawler.starterurl.commandside.handler.CreateStarterUrlCommandHandler;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@GRpcService
@RequestMapping("/")
public class StarterUrlCommandController extends StarterUrlCommandControllerGrpc.StarterUrlCommandControllerImplBase {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CreateStarterUrlCommandHandler createStarterUrlCommandHandler;

    public StarterUrlCommandController(CreateStarterUrlCommandHandler createStarterUrlCommandHandler) {
        this.createStarterUrlCommandHandler = createStarterUrlCommandHandler;
    }

    @Override
    public void create(CreateStaterUrl request, StreamObserver<Response> responseObserver) {
        logger.info("starter url request received {}", request);

        int status = 0;

        CreateStaterUrlCommand createStaterUrlCommand = CreateStaterUrlCommand.builder().urls(request.getUrlsList().stream()
                .map(Url::getUrlString).collect(Collectors.toList())).dataSourceId(request.getDataSourceId()).build();

        if (createStarterUrlCommandHandler.handler(createStaterUrlCommand)) {
            status = 1;
        }

        Response response = Response.newBuilder().setStatus(status).build();
        logger.info("server responded {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
