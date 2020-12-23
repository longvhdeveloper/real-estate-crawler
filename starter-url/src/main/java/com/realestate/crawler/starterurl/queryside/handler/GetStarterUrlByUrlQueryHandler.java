package com.realestate.crawler.starterurl.queryside.handler;

import com.realestate.crawler.starterurl.entity.StarterUrl;
import com.realestate.crawler.starterurl.queryside.query.GetStarterUrlByCheckSumQuery;
import com.realestate.crawler.starterurl.queryside.query.GetStarterUrlsQuery;
import com.realestate.crawler.starterurl.queryside.repository.StarterUrlQueryRepository;
import com.realestate.crawler.starterurl.queryside.specification.StarterUrlSpecifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@Slf4j
public class GetStarterUrlByCheckSumQueryHandler {
    private final StarterUrlQueryRepository starterUrlQueryRepository;

    @Autowired
    public GetStarterUrlByCheckSumQueryHandler(StarterUrlQueryRepository starterUrlQueryRepository) {
        this.starterUrlQueryRepository = starterUrlQueryRepository;
    }

    public StarterUrl getStarterUrlByCheckSum(GetStarterUrlByCheckSumQuery query) {


        return null;
    }

    private List<Specification<StarterUrl>> getSpecifications(GetStarterUrlsQuery query) {
        List<Specification<StarterUrl>> specifications = new LinkedList<>();

        if (query.getDataSourceId() > 0) {
            specifications.add(StarterUrlSpecifications.hasDataSource(query.getDataSourceId()));
        }

        if (query.getStatus() > 0) {
            specifications.add(StarterUrlSpecifications.hasStatus(query.getStatus()));
        }

        return specifications;
    }
}
