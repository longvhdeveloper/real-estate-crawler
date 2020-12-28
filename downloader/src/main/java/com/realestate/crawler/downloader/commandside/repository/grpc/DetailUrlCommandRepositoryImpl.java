package com.realestate.crawler.downloader.commandside.repository.grpc;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.realestate.crawler.downloader.commandside.repository.DetailUrlCommandRepository;
import com.realestate.crawler.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DetailUrlCommandRepositoryImpl implements DetailUrlCommandRepository {

    private final EurekaClient client;

    @Autowired
    public DetailUrlCommandRepositoryImpl(EurekaClient client) {
        this.client = client;
    }

    @Override
    public Optional<Detailurl> findByUrl(String url) {
        ManagedChannel channel = getManagedChannel();

        DetailurlQueryControllerGrpc.DetailurlQueryControllerBlockingStub stub
                = DetailurlQueryControllerGrpc.newBlockingStub(channel);

        DetailurlResponse response = stub.getByUrl(GetDetailUrlByUrl.newBuilder().setUrl(url).build());
        channel.shutdown();

        return Optional.of(response.getDetailUrl());
    }

    @Override
    public boolean updateHtmlContent(UpdateHtmlContentDetailUrl updateHtmlContentDetailUrl) {
        ManagedChannel channel = getManagedChannel();

        DetailurlCommandControllerGrpc.DetailurlCommandControllerBlockingStub stub
                = DetailurlCommandControllerGrpc.newBlockingStub(channel);

        Response response = stub.updateHtmlContent(updateHtmlContentDetailUrl);

        channel.shutdown();
        return response.getStatus() == 1;
    }

    private ManagedChannel getManagedChannel() {
        final InstanceInfo instanceInfo = client.getNextServerFromEureka("detail-url-service", false);
        return ManagedChannelBuilder.forAddress(instanceInfo.getIPAddr(), instanceInfo.getPort())
                .usePlaintext()
                .build();
    }
}
