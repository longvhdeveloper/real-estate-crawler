package com.realestate.crawler.extractor.commandside.repository;

import com.realestate.crawler.proto.Starterurl;

import java.util.Optional;

public interface IStarterUrlRepository {

    Optional<Starterurl> findById(long id);
}
