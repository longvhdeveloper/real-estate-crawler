package com.realestate.crawler.admin.commandside.repository;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.realestate.crawler.proto.CreateDatasource;
import com.realestate.crawler.proto.DataSourceCommandControllerGrpc;
import com.realestate.crawler.proto.Datasource;
import com.realestate.crawler.proto.Response;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DataSourceRepositoryImpl implements IDataSourceRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EurekaClient client;

    @Autowired
    public DataSourceRepositoryImpl(EurekaClient client) {
        this.client = client;
    }

    @Override
    public boolean create(CreateDatasource createDatasource) {
        logger.info("data source: {}", createDatasource);
        final InstanceInfo instanceInfo = client.getNextServerFromEureka("data-source-service", false);
        final ManagedChannel channel = ManagedChannelBuilder.forAddress(instanceInfo.getIPAddr(), instanceInfo.getPort())
                .usePlaintext()
                .build();

        DataSourceCommandControllerGrpc.DataSourceCommandControllerBlockingStub stub
                = DataSourceCommandControllerGrpc.newBlockingStub(channel);
        Response response = stub.create(createDatasource);

        channel.shutdown();

        return response.getStatus() == 1;
    }

    @Override
    public Optional<Datasource> get(long id) {
        return Optional.empty();
    }
}
