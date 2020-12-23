package com.realestate.crawler.starterurl.commandside.handler;

import com.realestate.crawler.starterurl.commandside.command.ICommand;
import com.realestate.crawler.starterurl.commandside.command.UpdateHtmlContentCommand;
import com.realestate.crawler.starterurl.commandside.repository.StarterUrlCommandRepository;
import com.realestate.crawler.starterurl.commandside.validator.UpdateHtmlContentCommandValidator;
import com.realestate.crawler.starterurl.entity.StarterUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UpdateHtmlContentCommandHandler implements ICommandHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UpdateHtmlContentCommandValidator validator;
    private final StarterUrlCommandRepository starterUrlCommandRepository;

    public UpdateHtmlContentCommandHandler(UpdateHtmlContentCommandValidator validator,
                                           StarterUrlCommandRepository starterUrlCommandRepository) {
        this.validator = validator;
        this.starterUrlCommandRepository = starterUrlCommandRepository;
    }

    @Override
    public boolean handler(ICommand command) {
        if (!validator.isValid(command)) {
            return false;
        }

        UpdateHtmlContentCommand updateHtmlContentCommand = (UpdateHtmlContentCommand) command;

        Optional<StarterUrl> optional = starterUrlCommandRepository.findById(updateHtmlContentCommand.getId());

        if (optional.isEmpty()) {
            logger.error("starter url with id {} is not exist", updateHtmlContentCommand.getId());
            return false;
        }

        StarterUrl starterUrl = optional.get();
        starterUrl.setHtmlContent(updateHtmlContentCommand.getHtmlContent());

        starterUrlCommandRepository.save(starterUrl);

        return true;
    }
}
