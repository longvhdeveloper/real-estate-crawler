package com.realestate.crawler.extractor.commandside.service;

import com.realestate.crawler.extractor.entity.PropertyParseItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class ExtractDetailUrlService {

    private Document document;

    public PropertyParseItem parse(String html) {
        document = parseHtml(html);

        return PropertyParseItem.builder()
                .name(getName())
                .price(getPrice())
                .area(getArea())
                .address(getAddress())
                .description(getDescription())
                .build();
    }

    private Document parseHtml(String html) {
        return Jsoup.parse(html);
    }

    private String getName() {
        Element titleElement = document.getElementsByClass("tile-product").first();
        return titleElement.text();
    }

    private String getPrice() {
        Element priceElement = document.getElementsByClass("sp2").first();

        return priceElement.text().trim();
    }

    private String getArea() {
        Elements elements = document.getElementsByClass("sp2");
        if (elements.size() <= 1) {
            return "";
        }
        Element areaElement = elements.get(1);

        return areaElement.text().trim();
    }

    private String getAddress() {
        Element addressElement = document.getElementsByClass("short-detail").first();
        return addressElement.text();
    }

    private String getDescription() {
        Element descriptionElement = document.getElementsByClass("des-product").first();
        return descriptionElement.text();
    }
}
