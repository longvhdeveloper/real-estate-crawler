package com.realestate.crawler.admin.commandside.controller;

import com.realestate.crawler.admin.commandside.command.CreateStarterUrlCommand;
import com.realestate.crawler.admin.commandside.handler.CreateStarterUrlCommandHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/admin")
@Slf4j
public class StarterUrlCommandController {

    private final CreateStarterUrlCommandHandler createStarterUrlCommandHandler;

    @Autowired
    public StarterUrlCommandController(CreateStarterUrlCommandHandler createStarterUrlCommandHandler) {
        this.createStarterUrlCommandHandler = createStarterUrlCommandHandler;
    }

    @PostMapping("/starter-url")
    public ResponseEntity<Void> create(@RequestBody CreateStarterUrlCommand command) {
        log.info("create starter url received {}", command);
        if (!createStarterUrlCommandHandler.handler(command)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }
}
