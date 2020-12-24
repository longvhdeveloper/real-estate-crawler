package com.realestate.crawler.detailurl.queryside.specification;

import com.realestate.crawler.detailurl.entity.DetailUrl;
import com.realestate.crawler.detailurl.entity.DetailUrl_;
import org.springframework.data.jpa.domain.Specification;

public class DetailUrlSpecifications {
    public static Specification<DetailUrl> hasCheckSumUrl(String checkSumUrl) {
        return (starterUrl, cq, cb) -> cb.equal(starterUrl.get(DetailUrl_.CHECK_SUM_URL), checkSumUrl);
    }
}
