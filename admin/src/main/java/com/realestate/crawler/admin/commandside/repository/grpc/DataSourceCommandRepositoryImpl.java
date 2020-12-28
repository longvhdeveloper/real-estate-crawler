package com.realestate.crawler.admin.commandside.repository.grpc;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.realestate.crawler.admin.commandside.repository.DataSourceCommandRepository;
import com.realestate.crawler.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
public class DataSourceCommandRepositoryImpl implements DataSourceCommandRepository {
    private final EurekaClient client;

    @Autowired
    public DataSourceCommandRepositoryImpl(EurekaClient client) {
        this.client = client;
    }

    @Override
    public boolean create(CreateDatasource createDatasource) {
        log.info("data source: {}", createDatasource);
        final ManagedChannel channel = getManagedChannel();

        DataSourceCommandControllerGrpc.DataSourceCommandControllerBlockingStub stub
                = DataSourceCommandControllerGrpc.newBlockingStub(channel);
        Response response = stub.create(createDatasource);

        channel.shutdown();

        return response.getStatus() == 1;
    }

    @Override
    public Optional<Datasource> get(long id) {
        log.info("data source id: {}", id);
        final ManagedChannel channel = getManagedChannel();

        DataSourceQueryControllerGrpc.DataSourceQueryControllerBlockingStub stub
                = DataSourceQueryControllerGrpc.newBlockingStub(channel);

        DatasourceResponse datasourceResponse = stub.get(GetDatasource.newBuilder().setId(id).build());
        channel.shutdown();

        return Optional.of(datasourceResponse.getDatasource());
    }

    private ManagedChannel getManagedChannel() {
        final InstanceInfo instanceInfo = client.getNextServerFromEureka("data-source-service", false);

        return ManagedChannelBuilder.forAddress(instanceInfo.getIPAddr(), instanceInfo.getPort())
                .usePlaintext()
                .build();
    }
}
