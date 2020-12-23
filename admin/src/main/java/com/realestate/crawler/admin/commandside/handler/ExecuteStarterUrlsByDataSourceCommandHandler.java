package com.realestate.crawler.admin.commandside.handler;

import com.realestate.crawler.admin.commandside.command.ExecuteStarterUrlsByDataSourceCommand;
import com.realestate.crawler.admin.commandside.command.ICommand;
import com.realestate.crawler.admin.commandside.message.DownloadStarterUrlMessage;
import com.realestate.crawler.admin.commandside.repository.IDataSourceRepository;
import com.realestate.crawler.admin.commandside.repository.IStarterUrlRepository;
import com.realestate.crawler.admin.producer.IProducer;
import com.realestate.crawler.proto.Datasource;
import com.realestate.crawler.proto.GetStaterUrls;
import com.realestate.crawler.proto.Starterurl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ExecuteStarterUrlsByDataSourceCommandHandler implements ICommandHandler {
    private final IDataSourceRepository dataSourceRepository;
    private final IStarterUrlRepository starterUrlRepository;

    private final IProducer producer;

    @Autowired
    public ExecuteStarterUrlsByDataSourceCommandHandler(IDataSourceRepository dataSourceRepository,
                                                        IStarterUrlRepository starterUrlRepository, IProducer producer) {
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

        String topic = getTopic(datasource);
        starterUrls.stream().filter(this::isStarterUrlEnabled).forEach(starterUrl -> {
            producer.send(topic, new DownloadStarterUrlMessage(starterUrl.getDataSourceId(), starterUrl.getUrl()));
        });

        return true;
    }

    private Datasource getDataSource(long id) {
        Datasource datasource = dataSourceRepository.get(id).orElseThrow(
                () -> new IllegalArgumentException("Data source with id " + id + " is not exist"));
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

    private String getTopic(Datasource datasource) {
        return "starter-" + datasource.getId();
    }
}
