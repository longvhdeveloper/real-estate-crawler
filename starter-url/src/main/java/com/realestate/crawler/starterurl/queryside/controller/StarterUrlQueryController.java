package com.realestate.crawler.starterurl.queryside.controller;

import com.realestate.crawler.proto.*;
import com.realestate.crawler.starterurl.entity.StarterUrl;
import com.realestate.crawler.starterurl.queryside.handler.GetStarterUrlByUrlQueryHandler;
import com.realestate.crawler.starterurl.queryside.handler.GetStarterUrlQueryHandler;
import com.realestate.crawler.starterurl.queryside.handler.GetStarterUrlsQueryHandler;
import com.realestate.crawler.starterurl.queryside.query.GetStarterUrlByUrlQuery;
import com.realestate.crawler.starterurl.queryside.query.GetStarterUrlsQuery;
import com.realestate.crawler.starterurl.queryside.query.GetStaterUrlQuery;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@GRpcService
@RequestMapping("/")
@Slf4j
public class StarterUrlQueryController extends StarterUrlQueryControllerGrpc.StarterUrlQueryControllerImplBase {

    private final GetStarterUrlQueryHandler getStarterUrlQueryHandler;
    private final GetStarterUrlsQueryHandler getStarterUrlsQueryHandler;
    private final GetStarterUrlByUrlQueryHandler getStarterUrlByUrlQueryHandler;

    @Autowired
    public StarterUrlQueryController(GetStarterUrlQueryHandler getStarterUrlQueryHandler,
                                     GetStarterUrlsQueryHandler getStarterUrlsQueryHandler,
                                     GetStarterUrlByUrlQueryHandler getStarterUrlByUrlQueryHandler) {
        this.getStarterUrlQueryHandler = getStarterUrlQueryHandler;
        this.getStarterUrlsQueryHandler = getStarterUrlsQueryHandler;
        this.getStarterUrlByUrlQueryHandler = getStarterUrlByUrlQueryHandler;
    }

    @Override
    public void get(GetStaterUrl request, StreamObserver<StarterUrlResponse> responseObserver) {

        log.info("stater url request get {}", request);

        Optional<StarterUrl> optional = getStarterUrlQueryHandler.handler(GetStaterUrlQuery.builder().id(request.getId()).build());

        if (optional.isEmpty()) {
            StarterUrlResponse response = StarterUrlResponse.newBuilder().build();
            log.info("server responded {}", response);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        StarterUrl starterUrl = optional.get();
        StarterUrlResponse response = StarterUrlResponse.newBuilder().setStarterUrl(Starterurl.newBuilder()
                .setId(starterUrl.getId())
                .setDataSourceId(starterUrl.getDataSourceId())
                .setUrl(Objects.toString(starterUrl.getUrl(), ""))
                .setCheckSumUrl(Objects.toString(starterUrl.getCheckSumUrl(), ""))
                .setHtmlContent(Objects.toString(starterUrl.getHtmlContent(), ""))
                .setCheckSumHtmlContent(Objects.toString(starterUrl.getCheckSumHtmlContent(), ""))
                .setStatus(starterUrl.getStatus().getStatus())
                .build()).build();
        log.info("server responded {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getList(GetStaterUrls request, StreamObserver<StarterUrlListResponse> responseObserver) {
        log.info("stater url request get {}", request);

        List<StarterUrl> starterUrls = getStarterUrlsQueryHandler.getStarterUrls(GetStarterUrlsQuery.builder().
                dataSourceId(request.getDataSourceId())
                .status(request.getStatus())
                .build());
        StarterUrlListResponse response = StarterUrlListResponse.newBuilder()
                .addAllStarterUrl(starterUrls.stream().map(starterUrl -> Starterurl.newBuilder().setId(starterUrl.getId())
                        .setDataSourceId(starterUrl.getDataSourceId())
                        .setUrl(Objects.toString(starterUrl.getUrl(), ""))
                        .setCheckSumUrl(Objects.toString(starterUrl.getCheckSumUrl(), ""))
                        .setHtmlContent(Objects.toString(starterUrl.getHtmlContent(), ""))
                        .setCheckSumHtmlContent(Objects.toString(starterUrl.getCheckSumHtmlContent(), ""))
                        .setStatus(starterUrl.getStatus().getStatus()).build()).collect(Collectors.toList()))
                .build();

        log.info("server responded {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getByUrl(GetStarterUrlByUrl request, StreamObserver<StarterUrlResponse> responseObserver) {
        log.info("stater url request get {}", request);

        StarterUrl starterUrl = getStarterUrlByUrlQueryHandler.getStarterUrlByUrl(GetStarterUrlByUrlQuery.builder()
                .dataSourceId(request.getDataSourceId())
                .url(request.getUrl())
                .build());

        if (Objects.isNull(starterUrl)) {
            StarterUrlResponse response = StarterUrlResponse.newBuilder().build();
            log.info("server responded {}", response);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        StarterUrlResponse response = StarterUrlResponse.newBuilder().setStarterUrl(Starterurl.newBuilder()
                .setId(starterUrl.getId())
                .setDataSourceId(starterUrl.getDataSourceId())
                .setUrl(Objects.toString(starterUrl.getUrl(), ""))
                .setCheckSumUrl(Objects.toString(starterUrl.getCheckSumUrl(), ""))
                .setHtmlContent(Objects.toString(starterUrl.getHtmlContent(), ""))
                .setCheckSumHtmlContent(Objects.toString(starterUrl.getCheckSumHtmlContent(), ""))
                .setStatus(starterUrl.getStatus().getStatus())
                .build()).build();
        log.info("server responded {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
