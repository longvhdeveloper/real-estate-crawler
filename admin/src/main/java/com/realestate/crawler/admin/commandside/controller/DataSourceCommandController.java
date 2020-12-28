package com.realestate.crawler.admin.commandside.controller;

import com.realestate.crawler.admin.commandside.command.CreateDataSourceCommand;
import com.realestate.crawler.admin.commandside.handler.CreateDataSourceCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/v1/admin")
public class DataSourceCommandController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CreateDataSourceCommandHandler createDataSourceCommandHandler;

    @Autowired
    public DataSourceCommandController(CreateDataSourceCommandHandler createDataSourceCommandHandler) {
        this.createDataSourceCommandHandler = createDataSourceCommandHandler;
    }

    @PostMapping("/data-source")
    public ResponseEntity<Void> create(@RequestBody CreateDataSourceCommand command)
            throws ExecutionException, InterruptedException {

        logger.info("create data source received {}", command);
        CompletableFuture<Boolean> future = CompletableFuture.completedFuture(createDataSourceCommandHandler.handle(command));
        if (!future.get()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }
}
