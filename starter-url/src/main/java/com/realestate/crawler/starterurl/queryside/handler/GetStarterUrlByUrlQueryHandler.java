package com.realestate.crawler.starterurl.queryside.handler;

import com.realestate.crawler.starterurl.entity.StarterUrl;
import com.realestate.crawler.starterurl.queryside.query.GetStarterUrlByUrlQuery;
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
public class GetStarterUrlByUrlQueryHandler {
    private final StarterUrlQueryRepository starterUrlQueryRepository;

    @Autowired
    public GetStarterUrlByUrlQueryHandler(StarterUrlQueryRepository starterUrlQueryRepository) {
        this.starterUrlQueryRepository = starterUrlQueryRepository;
    }

    public StarterUrl getStarterUrlByUrl(GetStarterUrlByUrlQuery query) {
        List<Specification<StarterUrl>> specifications = getSpecifications(query);

        if (specifications.isEmpty()) {
            return null;
        }

        Specification<StarterUrl> conditions = specifications.stream().reduce(Specification::and).get();

        return starterUrlQueryRepository.findOne(conditions).orElse(null);
    }

    private List<Specification<StarterUrl>> getSpecifications(GetStarterUrlByUrlQuery query) {
        List<Specification<StarterUrl>> specifications = new LinkedList<>();

        if (query.getDataSourceId() > 0) {
            specifications.add(StarterUrlSpecifications.hasDataSource(query.getDataSourceId()));
        }

        if (!query.getUrl().isEmpty()) {
            specifications.add(StarterUrlSpecifications.hasCheckSumUrl(StarterUrl.createCheckSumUrl(query.getUrl())));
        }

        return specifications;
    }
}
