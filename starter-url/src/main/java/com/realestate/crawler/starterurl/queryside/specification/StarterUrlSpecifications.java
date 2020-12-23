package com.realestate.crawler.starterurl.queryside.specification;

import com.realestate.crawler.starterurl.entity.StarterUrl;
import com.realestate.crawler.starterurl.entity.StarterUrl_;
import org.springframework.data.jpa.domain.Specification;

public class StarterUrlSpecifications {
    public static Specification<StarterUrl> hasDataSource(long dataSourceId) {
        return (starterUrl, cq, cb) -> cb.equal(starterUrl.get(StarterUrl_.DATA_SOURCE_ID), dataSourceId);
    }

    public static Specification<StarterUrl> hasStatus(int status) {
        return (starterUrl, cq, cb) -> {
            System.out.println(starterUrl);
            return cb.equal(starterUrl.get(StarterUrl_.STATUS_CODE), status);
        };
    }
}
