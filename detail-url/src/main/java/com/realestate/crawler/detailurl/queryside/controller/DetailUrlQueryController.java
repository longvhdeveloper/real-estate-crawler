package com.realestate.crawler.detailurl.queryside.controller;

import com.realestate.crawler.detailurl.entity.DetailUrl;
import com.realestate.crawler.detailurl.queryside.handler.GetDetailUrlByUrlQueryHandler;
import com.realestate.crawler.detailurl.queryside.handler.GetDetailUrlQueryHandler;
import com.realestate.crawler.detailurl.queryside.query.GetDetailUrlByUrlQuery;
import com.realestate.crawler.detailurl.queryside.query.GetDetailUrlQuery;
import com.realestate.crawler.proto.*;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;
import java.util.Optional;

@GRpcService
@RequestMapping("/")
@Slf4j
public class DetailUrlQueryController extends DetailurlQueryControllerGrpc.DetailurlQueryControllerImplBase {

    private final GetDetailUrlQueryHandler getDetailUrlQueryHandler;
    private final GetDetailUrlByUrlQueryHandler getDetailUrlByUrlQueryHandler;

    @Autowired
    public DetailUrlQueryController(GetDetailUrlQueryHandler getDetailUrlQueryHandler,
                                    GetDetailUrlByUrlQueryHandler getDetailUrlByUrlQueryHandler) {
        this.getDetailUrlQueryHandler = getDetailUrlQueryHandler;
        this.getDetailUrlByUrlQueryHandler = getDetailUrlByUrlQueryHandler;
    }

    @Override
    public void get(GetDetailUrl request, StreamObserver<DetailurlResponse> responseObserver) {
        log.info("get detail url request get {}", request);
        Optional<DetailUrl> optional = getDetailUrlQueryHandler.handler(GetDetailUrlQuery.builder().id(request.getId()).build());

        if (optional.isEmpty()) {
            DetailurlResponse response = DetailurlResponse.newBuilder().build();
            log.info("server responded {}", response);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        DetailUrl detailUrl = optional.get();
        DetailurlResponse response = DetailurlResponse.newBuilder().setDetailUrl(Detailurl.newBuilder()
                .setId(detailUrl.getId())
                .setUrl(detailUrl.getUrl())
                .setCheckSumUrl(detailUrl.getCheckSumUrl())
                .setCheckSumUrl(Objects.toString(detailUrl.getCheckSumUrl(), ""))
                .setHtmlContent(Objects.toString(detailUrl.getHtmlContent(), ""))
                .setCheckSumHtmlContent(Objects.toString(detailUrl.getCheckSumHtmlContent(), ""))
                .setStatus(detailUrl.getStatus().getStatus())
                .build()).build();
        log.info("server responded {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getByUrl(GetDetailUrlByUrl request, StreamObserver<DetailurlResponse> responseObserver) {
        log.info("get detail url request get {}", request);

        DetailUrl detailUrl = getDetailUrlByUrlQueryHandler.handle(GetDetailUrlByUrlQuery.builder()
                .url(request.getUrl())
                .build());

        if (Objects.isNull(detailUrl)) {
            DetailurlResponse response = DetailurlResponse.newBuilder().build();
            log.info("server responded {}", response);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        DetailurlResponse response = DetailurlResponse.newBuilder().setDetailUrl(Detailurl.newBuilder()
                .setId(detailUrl.getId())
                .setDataSourceId(detailUrl.getDataSourceId())
                .setUrl(Objects.toString(detailUrl.getUrl(), ""))
                .setCheckSumUrl(Objects.toString(detailUrl.getCheckSumUrl(), ""))
                .setHtmlContent(Objects.toString(detailUrl.getHtmlContent(), ""))
                .setCheckSumHtmlContent(Objects.toString(detailUrl.getCheckSumHtmlContent(), ""))
                .setStatus(detailUrl.getStatus().getStatus())
                .build()).build();

        log.info("server responded {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
