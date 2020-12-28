package com.realestate.crawler.extractor.commandside.repository;

import com.realestate.crawler.proto.CreateDetailUrl;
import com.realestate.crawler.proto.Detailurl;
import com.realestate.crawler.proto.UpdateHtmlContentDetailUrl;

import java.util.Optional;

public interface IDetailUrlRepository {
    boolean create(CreateDetailUrl createDetailUrl);

    boolean updateHtmlContent(UpdateHtmlContentDetailUrl updateHtmlContentDetailUrl);

    Optional<Detailurl> findById(long id);

    Optional<Detailurl> findByUrl(String url);
}
