package com.realestate.crawler.starterurl.queryside.handler;

import com.realestate.crawler.starterurl.entity.StarterUrl;
import com.realestate.crawler.starterurl.queryside.query.GetStaterUrlQuery;
import com.realestate.crawler.starterurl.queryside.repository.StarterUrlQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetStarterUrlQueryHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StarterUrlQueryRepository starterUrlQueryRepository;

    @Autowired
    public GetStarterUrlQueryHandler(StarterUrlQueryRepository starterUrlQueryRepository) {
        this.starterUrlQueryRepository = starterUrlQueryRepository;
    }

    public Optional<StarterUrl> handler(GetStaterUrlQuery query) {
        return starterUrlQueryRepository.findById(query.getId());
    }
}
