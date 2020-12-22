package com.realestate.crawler.datasource.entity;

import java.util.stream.Stream;

public enum Status {
    ENABLED(1), DISABLED(3);

    private final int status;

    private Status(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static Status of(int status) {
        return Stream.of(Status.values())
                .filter(p -> p.getStatus() == status)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
