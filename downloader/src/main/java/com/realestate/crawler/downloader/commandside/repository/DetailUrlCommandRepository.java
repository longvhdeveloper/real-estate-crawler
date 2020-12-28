package com.realestate.crawler.downloader.commandside.repository;

import com.realestate.crawler.proto.Detailurl;
import com.realestate.crawler.proto.UpdateHtmlContentDetailUrl;

import java.util.Optional;

public interface DetailUrlCommandRepository {

    boolean updateHtmlContent(UpdateHtmlContentDetailUrl updateHtmlContentDetailUrl);

    Optional<Detailurl> findByUrl(String url);
}
