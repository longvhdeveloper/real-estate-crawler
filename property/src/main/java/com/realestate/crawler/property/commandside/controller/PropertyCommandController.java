package com.realestate.crawler.property.commandside.controller;

import com.realestate.crawler.property.commandside.command.CreatePropertyCommand;
import com.realestate.crawler.property.commandside.handler.CreatePropertyCommandHandler;
import com.realestate.crawler.proto.CreateProperty;
import com.realestate.crawler.proto.PropertyCommandControllerGrpc;
import com.realestate.crawler.proto.Response;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GRpcService
@Slf4j
public class PropertyCommandController extends PropertyCommandControllerGrpc.PropertyCommandControllerImplBase {
    private final CreatePropertyCommandHandler createPropertyCommandHandler;

    @Autowired
    public PropertyCommandController(CreatePropertyCommandHandler createPropertyCommandHandler) {
        this.createPropertyCommandHandler = createPropertyCommandHandler;
    }

    @Override
    public void create(CreateProperty request, StreamObserver<Response> responseObserver) {
        log.info("starter url request received {}", request.getUrl());

        int status = 0;

        CreatePropertyCommand createPropertyCommand = CreatePropertyCommand.builder()
                .name(request.getName())
                .price(request.getPrice())
                .area(request.getArea())
                .address(request.getAddress())
                .description(request.getDescription())
                .url(request.getUrl())
                .build();

        if (createPropertyCommandHandler.handler(createPropertyCommand)) {
            status = 1;
        }

        Response response = Response.newBuilder().setStatus(status).build();
        log.info("server responded {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
