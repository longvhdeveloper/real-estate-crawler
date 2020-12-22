package com.realestate.crawler.starterurl.queryside.handler;

import com.realestate.crawler.starterurl.entity.StarterUrl;
import com.realestate.crawler.starterurl.queryside.query.GetStarterUrlsQuery;
import com.realestate.crawler.starterurl.queryside.repository.StarterUrlQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class GetStarterUrlsQueryHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StarterUrlQueryRepository starterUrlQueryRepository;

    @Autowired
    public GetStarterUrlsQueryHandler(StarterUrlQueryRepository starterUrlQueryRepository) {
        this.starterUrlQueryRepository = starterUrlQueryRepository;
    }

    public List<StarterUrl> getStarterUrls(GetStarterUrlsQuery query) {
        List<StarterUrl> starterUrls = new LinkedList<>();

        starterUrlQueryRepository.findAll().forEach(starterUrls::add);

        return starterUrls;
    }
}
