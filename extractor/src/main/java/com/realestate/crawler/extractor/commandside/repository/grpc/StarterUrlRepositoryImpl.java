package com.realestate.crawler.extractor.commandside.repository.grpc;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.realestate.crawler.extractor.commandside.repository.IStarterUrlRepository;
import com.realestate.crawler.proto.GetStaterUrl;
import com.realestate.crawler.proto.StarterUrlQueryControllerGrpc;
import com.realestate.crawler.proto.StarterUrlResponse;
import com.realestate.crawler.proto.Starterurl;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
public class StarterUrlRepositoryImpl implements IStarterUrlRepository {

    private final EurekaClient client;

    @Autowired
    public StarterUrlRepositoryImpl(EurekaClient client) {
        this.client = client;
    }

    @Override
    public Optional<Starterurl> findById(long id) {
        log.info("starter url id: {}", id);

        ManagedChannel channel = getManagedChannel();

        StarterUrlQueryControllerGrpc.StarterUrlQueryControllerBlockingStub
                stub = StarterUrlQueryControllerGrpc.newBlockingStub(channel);

        StarterUrlResponse response = stub.get(GetStaterUrl.newBuilder().setId(id).build());

        channel.shutdown();

        return Optional.of(response.getStarterUrl());
    }

    private ManagedChannel getManagedChannel() {
        final InstanceInfo instanceInfo = client.getNextServerFromEureka("starter-url-service", false);
        return ManagedChannelBuilder.forAddress(instanceInfo.getIPAddr(), instanceInfo.getPort())
                .usePlaintext()
                .build();
    }
}
