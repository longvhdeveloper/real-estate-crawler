package com.realestate.crawler.property.commandside.handler;

import com.realestate.crawler.property.commandside.command.CreatePropertyCommand;
import com.realestate.crawler.property.commandside.command.ICommand;
import com.realestate.crawler.property.commandside.repository.PropertyRepository;
import com.realestate.crawler.property.commandside.validator.CreatePropertyCommandValidator;
import com.realestate.crawler.property.entity.Property;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreatePropertyCommandHandler implements ICommandHandler {

    private final PropertyRepository propertyRepository;

    private final CreatePropertyCommandValidator validator;

    @Autowired
    public CreatePropertyCommandHandler(PropertyRepository propertyRepository,
                                        CreatePropertyCommandValidator validator) {
        this.propertyRepository = propertyRepository;
        this.validator = validator;
    }

    @Override
    public boolean handler(ICommand command) {

        CreatePropertyCommand createPropertyCommand = (CreatePropertyCommand) command;
        if (!validator.isValid(command)) {
            return false;
        }

        Property property = new Property(createPropertyCommand.getName(), createPropertyCommand.getPrice(), createPropertyCommand.getArea(),
                createPropertyCommand.getAddress(), createPropertyCommand.getDescription(), createPropertyCommand.getUrl());

        if (isPropertyExist(property)) {
            log.warn("Property with url {} is exist", property.getUrl());
            return false;
        }

        propertyRepository.save(property);

        return true;
    }

    private boolean isPropertyExist(Property property) {
        return propertyRepository.findByCheckSum(property.getCheckSumUrl()).isPresent();
    }
}
