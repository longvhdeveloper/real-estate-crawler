package com.realestate.crawler.extractor.commandside.repository;

import com.realestate.crawler.proto.CreateProperty;

public interface PropertyCommandRepository {

    boolean create(CreateProperty createProperty);
}
