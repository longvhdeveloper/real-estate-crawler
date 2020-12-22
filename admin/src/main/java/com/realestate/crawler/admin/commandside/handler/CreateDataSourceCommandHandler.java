package com.realestate.crawler.admin.commandside.handler;

import com.realestate.crawler.admin.commandside.command.CreateDataSourceCommand;
import com.realestate.crawler.admin.commandside.command.ICommand;
import com.realestate.crawler.admin.commandside.repository.IDataSourceRepository;
import com.realestate.crawler.proto.CreateDatasource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CreateDataSourceCommandHandler implements ICommandHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final IDataSourceRepository dataSourceRepository;

    public CreateDataSourceCommandHandler(IDataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    @Override
    public boolean handler(ICommand command) {
        CreateDataSourceCommand createDataSourceCommand = (CreateDataSourceCommand) command;
        return dataSourceRepository.create(CreateDatasource.newBuilder()
                .setName(createDataSourceCommand.getName())
                .setUrl(createDataSourceCommand.getUrl())
                .build());
    }
}
