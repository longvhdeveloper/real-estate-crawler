package com.realestate.crawler.extractor.commandside.service;

import com.realestate.crawler.proto.Starterurl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class ExtractStarterUrlService {
    public List<String> extractStarterUrlGetDetailUrls(Starterurl starterUrl) {
        if (Objects.isNull(starterUrl)) {
            throw new IllegalArgumentException("Starter Url is empty");
        }

        Document document = parseHtml(starterUrl.getHtmlContent());

        List<String> detailUrls = new LinkedList<>();

        Elements elements = document.getElementsByClass("wrap-plink");

        for (final Element element : elements) {
            detailUrls.add(element.attr("href"));
        }

        return detailUrls;
    }

    public int getCurrentPageNumber(Starterurl starterUrl) {

        Document document = parseHtml(starterUrl.getHtmlContent());

        Element paginationElement = document.getElementsByClass("pagination").first();

        if (Objects.isNull(paginationElement)) {
            return 0;
        }
        Element activatedPage = paginationElement.getElementsByClass("actived").first();
        return Integer.parseInt(activatedPage.text());
    }

    public Document parseHtml(String html) {
        if (html.isEmpty()) {
            throw new IllegalArgumentException("HTML string is empty");
        }
        return Jsoup.parse(html);
    }
}
