package com.realestate.crawler.extractor.commandside.repository.grpc;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.realestate.crawler.extractor.commandside.repository.PropertyCommandRepository;
import com.realestate.crawler.proto.CreateProperty;
import com.realestate.crawler.proto.PropertyCommandControllerGrpc;
import com.realestate.crawler.proto.Response;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class PropertyCommandRepositoryImpl implements PropertyCommandRepository {

    private final EurekaClient client;

    @Autowired
    public PropertyCommandRepositoryImpl(EurekaClient client) {
        this.client = client;
    }

    @Override
    public boolean create(CreateProperty createProperty) {
        log.info("create property: {}", createProperty.getUrl());

        ManagedChannel channel = getManagedChannel();

        PropertyCommandControllerGrpc.PropertyCommandControllerBlockingStub stub
                = PropertyCommandControllerGrpc.newBlockingStub(channel);

        Response response = stub.create(createProperty);

        channel.shutdown();

        return response.getStatus() == 1;
    }

    private ManagedChannel getManagedChannel() {
        final InstanceInfo instanceInfo = client.getNextServerFromEureka("property-service", false);
        return ManagedChannelBuilder.forAddress(instanceInfo.getIPAddr(), instanceInfo.getPort())
                .usePlaintext()
                .build();
    }
}
