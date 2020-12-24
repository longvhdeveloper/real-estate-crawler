package com.realestate.crawler.detailurl.queryside.handler;

import com.realestate.crawler.detailurl.entity.DetailUrl;
import com.realestate.crawler.detailurl.queryside.query.GetDetailUrlByUrlQuery;
import com.realestate.crawler.detailurl.queryside.repository.DetailUrlQueryRepository;
import com.realestate.crawler.detailurl.queryside.specification.DetailUrlSpecifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@Slf4j
public class GetDetailUrlByUrlQueryHandler {

    private final DetailUrlQueryRepository detailUrlQueryRepository;

    @Autowired
    public GetDetailUrlByUrlQueryHandler(DetailUrlQueryRepository detailUrlQueryRepository) {
        this.detailUrlQueryRepository = detailUrlQueryRepository;
    }

    public DetailUrl handle(GetDetailUrlByUrlQuery query) {
        List<Specification<DetailUrl>> specifications = getSpecifications(query);

        if (specifications.isEmpty()) {
            return null;
        }

        Specification<DetailUrl> conditions = specifications.stream().reduce(Specification::and).get();

        return detailUrlQueryRepository.findOne(conditions).orElse(null);
    }

    private List<Specification<DetailUrl>> getSpecifications(GetDetailUrlByUrlQuery query) {
        List<Specification<DetailUrl>> specifications = new LinkedList<>();

        if (!query.getUrl().isEmpty()) {
            specifications.add(DetailUrlSpecifications.hasCheckSumUrl(DetailUrl.createCheckSumUrl(query.getUrl())));
        }

        return specifications;
    }
}
