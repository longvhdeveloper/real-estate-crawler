package com.realestate.crawler.downloader.commandside.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class DownloadService {
    public Optional<Document> downloadWebPage(String url) {
        if (url.isEmpty()) {
            throw new IllegalArgumentException("Url to download page is empty");
        }

        Document document = null;

        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla")
                    .timeout(5000)
                    .get();
        } catch (IOException e) {
            log.warn("Can not get content from page {} sometimes. Reason[{}]", url, e.getMessage());
        }

        return Optional.ofNullable(document);
    }
}
