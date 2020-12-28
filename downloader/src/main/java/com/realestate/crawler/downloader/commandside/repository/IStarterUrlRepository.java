package com.realestate.crawler.downloader.commandside.repository;

import com.realestate.crawler.proto.Starterurl;
import com.realestate.crawler.proto.UpdateHtmlContentStarterUrl;

import java.util.Optional;

public interface IStarterUrlRepository {

    boolean updateHtmlContent(UpdateHtmlContentStarterUrl updateHtmlContentStarterUrl);

    Optional<Starterurl> getStarterUrlByUrl(long dataSourceId, String checkSumUrl);
}
