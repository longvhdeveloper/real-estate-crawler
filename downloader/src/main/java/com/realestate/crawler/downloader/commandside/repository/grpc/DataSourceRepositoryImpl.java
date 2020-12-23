package com.realestate.crawler.downloader.commandside.repository.grpc;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.realestate.crawler.downloader.commandside.repository.IDataSourceRepository;
import com.realestate.crawler.proto.DataSourceQueryControllerGrpc;
import com.realestate.crawler.proto.Datasource;
import com.realestate.crawler.proto.DatasourceResponse;
import com.realestate.crawler.proto.GetDatasource;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
public class DataSourceRepositoryImpl implements IDataSourceRepository {
    private final EurekaClient client;

    @Autowired
    public DataSourceRepositoryImpl(EurekaClient client) {
        this.client = client;
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
