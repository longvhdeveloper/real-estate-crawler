package com.realestate.crawler.extractor.commandside.repository.grpc;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.realestate.crawler.extractor.commandside.repository.IDetailUrlRepository;
import com.realestate.crawler.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DetailUrlRepositoryImpl implements IDetailUrlRepository {

    private final EurekaClient client;

    @Autowired
    public DetailUrlRepositoryImpl(EurekaClient client) {
        this.client = client;
    }

    @Override
    public boolean create(CreateDetailUrl createDetailUrl) {
        ManagedChannel channel = getManagedChannel();

        DetailurlCommandControllerGrpc.DetailurlCommandControllerBlockingStub stub
                = DetailurlCommandControllerGrpc.newBlockingStub(channel);

        Response response = stub.create(createDetailUrl);

        channel.shutdown();
        return response.getStatus() == 1;
    }

    @Override
    public boolean updateHtmlContent(UpdateHtmlContentDetailUrl updateHtmlContentDetailUrl) {
        return false;
    }

    @Override
    public Optional<Detailurl> findById(long id) {
        ManagedChannel channel = getManagedChannel();

        DetailurlQueryControllerGrpc.DetailurlQueryControllerBlockingStub stub
                = DetailurlQueryControllerGrpc.newBlockingStub(channel);


        channel.shutdown();

        return Optional.empty();
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

    private ManagedChannel getManagedChannel() {
        final InstanceInfo instanceInfo = client.getNextServerFromEureka("detail-url-service", false);
        return ManagedChannelBuilder.forAddress(instanceInfo.getIPAddr(), instanceInfo.getPort())
                .usePlaintext()
                .build();
    }
}
