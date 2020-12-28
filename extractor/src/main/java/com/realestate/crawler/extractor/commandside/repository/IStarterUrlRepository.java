package com.realestate.crawler.extractor.commandside.repository;

import com.realestate.crawler.proto.CreateStaterUrl;
import com.realestate.crawler.proto.Starterurl;

import java.util.Optional;

public interface IStarterUrlRepository {

    boolean create(CreateStaterUrl createStaterUrl);

    Optional<Starterurl> findById(long id);
}
