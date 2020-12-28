package com.realestate.crawler.downloader.commandside.repository.grpc;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.realestate.crawler.downloader.commandside.repository.StarterUrlCommandRepository;
import com.realestate.crawler.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
public class StarterUrlCommandRepositoryImpl implements StarterUrlCommandRepository {

    private final EurekaClient client;

    @Autowired
    public StarterUrlCommandRepositoryImpl(EurekaClient client) {
        this.client = client;
    }

    @Override
    public boolean updateHtmlContent(UpdateHtmlContentStarterUrl updateHtmlContentStarterUrl) {
        log.info("update html content starter url: {}", updateHtmlContentStarterUrl.getId());

        ManagedChannel channel = getManagedChannel();

        StarterUrlCommandControllerGrpc.StarterUrlCommandControllerBlockingStub stub
                = StarterUrlCommandControllerGrpc.newBlockingStub(channel);

        Response response = stub.updateHtmlContent(updateHtmlContentStarterUrl);

        channel.shutdown();

        return response.getStatus() == 1;
    }

    @Override
    public Optional<Starterurl> getStarterUrlByUrl(long dataSourceId, String url) {
        log.info("starter url: {}", url);

        ManagedChannel channel = getManagedChannel();

        StarterUrlQueryControllerGrpc.StarterUrlQueryControllerBlockingStub
                stub = StarterUrlQueryControllerGrpc.newBlockingStub(channel);
        StarterUrlResponse starterUrlResponse = stub.getByUrl(GetStarterUrlByUrl.newBuilder()
                .setUrl(url)
                .setDataSourceId(dataSourceId)
                .build());

        channel.shutdown();

        return Optional.of(starterUrlResponse.getStarterUrl());
    }

    private ManagedChannel getManagedChannel() {
        final InstanceInfo instanceInfo = client.getNextServerFromEureka("starter-url-service", false);
        return ManagedChannelBuilder.forAddress(instanceInfo.getIPAddr(), instanceInfo.getPort())
                .usePlaintext()
                .build();
    }
}
