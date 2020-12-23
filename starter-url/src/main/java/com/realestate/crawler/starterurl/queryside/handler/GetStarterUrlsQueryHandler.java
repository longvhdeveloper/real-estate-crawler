package com.realestate.crawler.starterurl.queryside.handler;

import com.realestate.crawler.starterurl.entity.StarterUrl;
import com.realestate.crawler.starterurl.queryside.query.GetStarterUrlsQuery;
import com.realestate.crawler.starterurl.queryside.repository.StarterUrlQueryRepository;
import com.realestate.crawler.starterurl.queryside.specification.StarterUrlSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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

        List<Specification<StarterUrl>> specifications = getSpecifications(query);
        if (specifications.isEmpty()) {
            starterUrlQueryRepository.findAll().forEach(starterUrls::add);
        } else {
            Specification<StarterUrl> conditions = specifications.stream().reduce(Specification::and).get();
            starterUrls.addAll(starterUrlQueryRepository.findAll(Specification.where(conditions)));
        }
        return starterUrls;
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
