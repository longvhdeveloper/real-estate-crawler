package com.realestate.crawler.detailurl.queryside.handler;

import com.realestate.crawler.detailurl.entity.DetailUrl;
import com.realestate.crawler.detailurl.queryside.query.GetDetailUrlQuery;
import com.realestate.crawler.detailurl.queryside.repository.DetailUrlQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class GetDetailUrlQueryHandler {
    private final DetailUrlQueryRepository detailUrlQueryRepository;

    @Autowired
    public GetDetailUrlQueryHandler(DetailUrlQueryRepository detailUrlQueryRepository) {
        this.detailUrlQueryRepository = detailUrlQueryRepository;
    }

    public Optional<DetailUrl> handler(GetDetailUrlQuery query) {
        return detailUrlQueryRepository.findById(query.getId());
    }
}
