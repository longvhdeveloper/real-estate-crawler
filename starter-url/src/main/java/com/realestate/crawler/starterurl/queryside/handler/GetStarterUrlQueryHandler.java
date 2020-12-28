package com.realestate.crawler.starterurl.queryside.handler;

import com.realestate.crawler.starterurl.entity.StarterUrl;
import com.realestate.crawler.starterurl.queryside.query.GetStaterUrlQuery;
import com.realestate.crawler.starterurl.queryside.repository.StarterUrlQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class GetStarterUrlQueryHandler {
    private final StarterUrlQueryRepository starterUrlQueryRepository;

    @Autowired
    public GetStarterUrlQueryHandler(StarterUrlQueryRepository starterUrlQueryRepository) {
        this.starterUrlQueryRepository = starterUrlQueryRepository;
    }

    public Optional<StarterUrl> handler(GetStaterUrlQuery query) {
        return starterUrlQueryRepository.findById(query.getId());
    }
}
