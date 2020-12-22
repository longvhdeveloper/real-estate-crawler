package com.realestate.crawler.datasource.commandside.handler;

import com.realestate.crawler.datasource.commandside.command.CreateDataSourceCommand;
import com.realestate.crawler.datasource.commandside.command.ICommand;
import com.realestate.crawler.datasource.entity.DataSource;
import com.realestate.crawler.datasource.commandside.repository.DataSourceCommandRepository;
import com.realestate.crawler.datasource.commandside.validator.CreateDataSourceCommandValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateDataSourceCommandHandler implements ICommandHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DataSourceCommandRepository dataSourceCommandRepository;

    private final CreateDataSourceCommandValidator validator;

    @Autowired
    public CreateDataSourceCommandHandler(DataSourceCommandRepository dataSourceCommandRepository,
                                          CreateDataSourceCommandValidator validator) {
        this.dataSourceCommandRepository = dataSourceCommandRepository;
        this.validator = validator;
    }

    @Override
    public boolean handler(ICommand command) {
        if (!validator.isValid(command))
            return false;

        CreateDataSourceCommand createDataSourceCommand = (CreateDataSourceCommand) command;
        dataSourceCommandRepository.save(new DataSource(createDataSourceCommand.getName(), createDataSourceCommand.getUrl()));

        return true;
    }
}
