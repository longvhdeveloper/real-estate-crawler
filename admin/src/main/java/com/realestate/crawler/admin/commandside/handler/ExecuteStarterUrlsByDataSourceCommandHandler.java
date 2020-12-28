package com.realestate.crawler.admin.commandside.handler;

import com.realestate.crawler.admin.commandside.command.ExecuteStarterUrlsByDataSourceCommand;
import com.realestate.crawler.admin.commandside.command.ICommand;
import com.realestate.crawler.admin.commandside.repository.DataSourceCommandRepository;
import com.realestate.crawler.admin.commandside.repository.StarterUrlCommandRepository;
import com.realestate.crawler.admin.message.DownloadStarterUrlMessage;
import com.realestate.crawler.admin.producer.IProducer;
import com.realestate.crawler.proto.Datasource;
import com.realestate.crawler.proto.GetStaterUrls;
import com.realestate.crawler.proto.Starterurl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class ExecuteStarterUrlsByDataSourceCommandHandler implements ICommandHandler {
    private final DataSourceCommandRepository dataSourceRepository;
    private final StarterUrlCommandRepository starterUrlRepository;

    private final IProducer producer;

    @Value("${spring.kafka.topic.downloadStarter}")
    private String downloadStarterTopic;

    @Autowired
    public ExecuteStarterUrlsByDataSourceCommandHandler(DataSourceCommandRepository dataSourceRepository,
                                                        StarterUrlCommandRepository starterUrlRepository, IProducer producer) {
        this.dataSourceRepository = dataSourceRepository;
        this.starterUrlRepository = starterUrlRepository;
        this.producer = producer;
    }

    @Override
    public boolean handler(ICommand command) {
        ExecuteStarterUrlsByDataSourceCommand executeStarterUrlsByDataSourceCommand
                = (ExecuteStarterUrlsByDataSourceCommand) command;

        Datasource datasource = getDataSource(executeStarterUrlsByDataSourceCommand.getId());
        List<Starterurl> starterUrls = getListStarterUrls(datasource);

        if (starterUrls.isEmpty()) {
            log.warn("Data source with id {} is not have starter url", datasource.getId());
            return false;
        }

        starterUrls.stream().filter(this::isStarterUrlEnabled).forEach(starterUrl -> sendToDownloadStarterUrl(datasource, starterUrl));

        return true;
    }

    private Datasource getDataSource(long id) {
        Optional<Datasource> optional = dataSourceRepository.get(id);
        if (optional.isEmpty() || optional.get().getId() == 0) {
            throw new IllegalArgumentException("Data source with id " + id + " is not exist");
        }

        Datasource datasource = optional.get();

        if (!isDataSourceEnabled(datasource)) {
            throw new IllegalArgumentException("Data source with id " + id + " is disabled");
        }
        return datasource;
    }

    private boolean isDataSourceEnabled(Datasource datasource) {
        return datasource.getStatus() == 1;
    }

    private List<Starterurl> getListStarterUrls(Datasource datasource) {
        return starterUrlRepository.getStarterUrls(GetStaterUrls.newBuilder()
                .setDataSourceId(datasource.getId())
                .build());
    }

    private boolean isStarterUrlEnabled(Starterurl starterurl) {
        return starterurl.getStatus() == 1;
    }

    private void sendToDownloadStarterUrl(Datasource datasource, Starterurl starterurl) {
        producer.send(downloadStarterTopic, new DownloadStarterUrlMessage(starterurl.getDataSourceId(), starterurl.getUrl()));
    }
}
