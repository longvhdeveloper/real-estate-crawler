package com.realestate.crawler.admin.commandside.handler;

import com.realestate.crawler.admin.commandside.command.CreateStarterUrlCommand;
import com.realestate.crawler.admin.commandside.command.ICommand;
import com.realestate.crawler.admin.commandside.repository.StarterUrlCommandRepository;
import com.realestate.crawler.proto.CreateStaterUrl;
import com.realestate.crawler.proto.Url;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Slf4j
public class CreateStarterUrlCommandHandler implements ICommandHandler {

    private final StarterUrlCommandRepository starterUrlRepository;

    @Autowired
    public CreateStarterUrlCommandHandler(StarterUrlCommandRepository starterUrlRepository) {
        this.starterUrlRepository = starterUrlRepository;
    }

    @Override
    public boolean handle(ICommand command) {
        CreateStarterUrlCommand createStarterUrlCommand = (CreateStarterUrlCommand) command;
        return starterUrlRepository.create(CreateStaterUrl.newBuilder()
                .setDataSourceId(createStarterUrlCommand.getDataSourceId())
                .addAllUrls(createStarterUrlCommand.getUrls().stream().map(
                        url -> Url.newBuilder().setUrlString(url).build()).collect(Collectors.toList()))
                .build());
    }
}
