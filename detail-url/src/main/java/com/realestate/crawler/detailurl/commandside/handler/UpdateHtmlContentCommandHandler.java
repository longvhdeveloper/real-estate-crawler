package com.realestate.crawler.detailurl.commandside.handler;

import com.realestate.crawler.detailurl.commandside.command.ICommand;
import com.realestate.crawler.detailurl.commandside.command.UpdateHtmlContentCommand;
import com.realestate.crawler.detailurl.commandside.repository.DetailUrlCommandRepository;
import com.realestate.crawler.detailurl.commandside.validator.UpdateDetailUrlCommandValidator;
import com.realestate.crawler.detailurl.entity.DetailUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class UpdateHtmlContentCommandHandler implements ICommandHandler {

    private final UpdateDetailUrlCommandValidator validator;
    private final DetailUrlCommandRepository detailUrlCommandRepository;

    @Autowired
    public UpdateHtmlContentCommandHandler(UpdateDetailUrlCommandValidator validator,
                                           DetailUrlCommandRepository detailUrlCommandRepository) {
        this.validator = validator;
        this.detailUrlCommandRepository = detailUrlCommandRepository;
    }

    @Override
    public boolean handler(ICommand command) {

        if (!validator.isValid(command)) {
            return false;
        }

        UpdateHtmlContentCommand updateHtmlContentCommand = (UpdateHtmlContentCommand) command;
        Optional<DetailUrl> optional = detailUrlCommandRepository.findById(updateHtmlContentCommand.getId());

        if (optional.isEmpty()) {
            log.error("detail url with id {} is not exist", updateHtmlContentCommand.getId());
            return false;
        }

        DetailUrl detailUrl = optional.get();
        detailUrl.setHtmlContent(updateHtmlContentCommand.getHtml());

        detailUrlCommandRepository.save(detailUrl);

        return true;
    }
}
