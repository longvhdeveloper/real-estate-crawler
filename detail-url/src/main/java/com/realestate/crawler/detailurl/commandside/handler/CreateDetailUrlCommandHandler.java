package com.realestate.crawler.detailurl.commandside.handler;

import com.realestate.crawler.detailurl.commandside.command.CreateDetailUrlCommand;
import com.realestate.crawler.detailurl.commandside.command.ICommand;
import com.realestate.crawler.detailurl.commandside.repository.DetailUrlCommandRepository;
import com.realestate.crawler.detailurl.commandside.validator.CreateDetailUrlCommandValidator;
import com.realestate.crawler.detailurl.entity.DetailUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateDetailUrlCommandHandler implements ICommandHandler {

    private final DetailUrlCommandRepository detailUrlCommandRepository;
    private final CreateDetailUrlCommandValidator validator;

    @Autowired
    public CreateDetailUrlCommandHandler(DetailUrlCommandRepository detailUrlCommandRepository,
                                         CreateDetailUrlCommandValidator validator) {
        this.detailUrlCommandRepository = detailUrlCommandRepository;
        this.validator = validator;
    }

    @Override
    public boolean handler(ICommand command) {
        if (!validator.isValid(command)) {
            return false;
        }

        CreateDetailUrlCommand createDetailUrlCommand = (CreateDetailUrlCommand) command;
        detailUrlCommandRepository.save(new DetailUrl(createDetailUrlCommand.getUrl(),
                createDetailUrlCommand.getDataSourceId()));
        return true;
    }
}
