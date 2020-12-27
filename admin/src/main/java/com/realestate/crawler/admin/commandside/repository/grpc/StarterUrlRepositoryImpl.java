package com.realestate.crawler.admin.commandside.repository.grpc;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.realestate.crawler.admin.commandside.repository.IStarterUrlRepository;
import com.realestate.crawler.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class StarterUrlRepositoryImpl implements IStarterUrlRepository {

    private final EurekaClient client;

    @Autowired
    public StarterUrlRepositoryImpl(EurekaClient client) {
        this.client = client;
    }

    @Override
    public boolean create(CreateStaterUrl createStaterUrl) {
        log.info("create starter url: {}", createStaterUrl);
        ManagedChannel channel = getManagedChannel();

        StarterUrlCommandControllerGrpc.StarterUrlCommandControllerBlockingStub stub
                = StarterUrlCommandControllerGrpc.newBlockingStub(channel);

        Response response = stub.create(createStaterUrl);

        channel.shutdown();

        return response.getStatus() == 1;
    }

    @Override
    public List<Starterurl> getStarterUrls(GetStaterUrls getStaterUrls) {
        log.info("starter url: {}", getStaterUrls);
        ManagedChannel channel = getManagedChannel();

        StarterUrlQueryControllerGrpc.StarterUrlQueryControllerBlockingStub stub
                = StarterUrlQueryControllerGrpc.newBlockingStub(channel);

        List<Starterurl> starterUrlList = stub.getList(getStaterUrls).getStarterUrlList();

        channel.shutdown();

        return starterUrlList;
    }

    private ManagedChannel getManagedChannel() {
        final InstanceInfo instanceInfo = client.getNextServerFromEureka("starter-url-service", false);
        return ManagedChannelBuilder.forAddress(instanceInfo.getIPAddr(), instanceInfo.getPort())
                .usePlaintext()
                .build();
    }
}
