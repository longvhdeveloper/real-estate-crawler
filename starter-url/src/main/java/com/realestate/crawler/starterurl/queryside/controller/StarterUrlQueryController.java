package com.realestate.crawler.starterurl.queryside.controller;

import com.realestate.crawler.proto.*;
import com.realestate.crawler.starterurl.entity.StarterUrl;
import com.realestate.crawler.starterurl.queryside.handler.GetStarterUrlQueryHandler;
import com.realestate.crawler.starterurl.queryside.handler.GetStarterUrlsQueryHandler;
import com.realestate.crawler.starterurl.queryside.query.GetStarterUrlsQuery;
import com.realestate.crawler.starterurl.queryside.query.GetStaterUrlQuery;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@GRpcService
@RequestMapping("/")
public class StarterUrlQueryController extends StarterUrlQueryControllerGrpc.StarterUrlQueryControllerImplBase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GetStarterUrlQueryHandler getStarterUrlQueryHandler;
    private final GetStarterUrlsQueryHandler getStarterUrlsQueryHandler;

    @Autowired
    public StarterUrlQueryController(GetStarterUrlQueryHandler getStarterUrlQueryHandler,
                                     GetStarterUrlsQueryHandler getStarterUrlsQueryHandler) {
        this.getStarterUrlQueryHandler = getStarterUrlQueryHandler;
        this.getStarterUrlsQueryHandler = getStarterUrlsQueryHandler;
    }

    @Override
    public void get(GetStaterUrl request, StreamObserver<StarterUrlResponse> responseObserver) {

        logger.info("stater url request get {}", request);

        Optional<StarterUrl> optional = getStarterUrlQueryHandler.handler(GetStaterUrlQuery.builder().id(request.getId()).build());

        if (optional.isEmpty()) {
            StarterUrlResponse response = StarterUrlResponse.newBuilder().build();
            logger.info("server responded {}", response);
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
        logger.info("server responded {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getList(GetStaterUrls request, StreamObserver<StarterUrlListResponse> responseObserver) {
        logger.info("stater url request get {}", request);

        List<StarterUrl> starterUrls = getStarterUrlsQueryHandler.getStarterUrls(GetStarterUrlsQuery.builder().build());
        StarterUrlListResponse response = StarterUrlListResponse.newBuilder()
                .addAllStarterUrl(starterUrls.stream().map(starterUrl -> Starterurl.newBuilder().setId(starterUrl.getId())
                        .setDataSourceId(starterUrl.getDataSourceId())
                        .setUrl(Objects.toString(starterUrl.getUrl(), ""))
                        .setCheckSumUrl(Objects.toString(starterUrl.getCheckSumUrl(), ""))
                        .setHtmlContent(Objects.toString(starterUrl.getHtmlContent(), ""))
                        .setCheckSumHtmlContent(Objects.toString(starterUrl.getCheckSumHtmlContent(), ""))
                        .setStatus(starterUrl.getStatus().getStatus()).build()).collect(Collectors.toList()))
                .build();

        logger.info("server responded {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
