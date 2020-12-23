package com.realestate.crawler.admin.commandside.handler;

import com.realestate.crawler.admin.commandside.command.CreateDataSourceCommand;
import com.realestate.crawler.admin.commandside.command.ICommand;
import com.realestate.crawler.admin.commandside.repository.IDataSourceRepository;
import com.realestate.crawler.proto.CreateDatasource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateDataSourceCommandHandler implements ICommandHandler {
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
