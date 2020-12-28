package com.realestate.crawler.admin.commandside.controller;

import com.realestate.crawler.admin.commandside.command.ExecuteStarterUrlsByDataSourceCommand;
import com.realestate.crawler.admin.commandside.handler.ExecuteStarterUrlsByDataSourceCommandHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/v1/admin")
@Slf4j
public class StarterUrlsExecutorController {

    private final ExecuteStarterUrlsByDataSourceCommandHandler executeStarterUrlsByDataSourceCommandHandler;

    @Autowired
    public StarterUrlsExecutorController(
            ExecuteStarterUrlsByDataSourceCommandHandler executeStarterUrlsByDataSourceCommandHandler) {
        this.executeStarterUrlsByDataSourceCommandHandler = executeStarterUrlsByDataSourceCommandHandler;
    }

    @PostMapping("/executor")
    public ResponseEntity<Void> execute(@RequestBody ExecuteStarterUrlsByDataSourceCommand command)
            throws ExecutionException, InterruptedException {

        log.info("execute starter url by data source received {}", command);

        CompletableFuture.runAsync(() -> executeStarterUrlsByDataSourceCommandHandler.handle(command)).get();

        return ResponseEntity.ok().build();
    }
}
